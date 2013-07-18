package service

import models.User
import controllers.CORSHeaderNames._
import play.api.mvc._
import play.api.http.HeaderNames
import scala.Left
import scala.Right
import play.api.Logger
import play.api.mvc.Results._

/**
 * A request that adds the User for the current call
 */
case class SecuredRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

trait JsonAuthentication {
  
  def readQueryString(request: Request[_]):
  Option[Either[Result, (String, String)]] = {

    request.queryString.get("user").map { user =>
      request.queryString.get("password").map { password =>
        Right((user.head, password.head))
      }.getOrElse {
        Left(BadRequest("Password not specified"))
      }
    }
  }

  def authenticate(user: String, password: String): Option[User] = {
    //TODO move from plain password to bcrypt hashing
    //https://github.com/bnoguchi/mongoose-auth/blob/master/lib/modules/password/plugin.js
    Logger.info("authenticate " + user)
    if (user.length == 0) None
    if (password.length == 0) None
    val currentUser = User.findOneByEmail(user)
    currentUser match {
      case Some(u) => if (u.password == password)  Some(u) else None
      case None => None
    }
  }

  def readBasicAuthentication(headers: Headers):
    Option[Either[Result, (String, String)]] = {

    headers.get(HeaderNames.AUTHORIZATION).map {
      header =>
        val BasicHeader = "Basic (.*)".r
        header match {
          case BasicHeader(base64) => {
            try {
              import org.apache.commons.codec.binary.Base64
              val decodedBytes = Base64.decodeBase64(base64.getBytes)
              val credentials = new String(decodedBytes).split(":", 2)
              if (credentials.length != 2) {
                Left(BadRequest("Invalid basic authentication"))
              } else {
                val (user, password) = (credentials(0), credentials(1))
                Right((user, password))
              }
            }
          }
          case _ => Left(BadRequest("Invalid Authorization header"))
        }
    }
  }

  def AuthenticatedAction[A](bodyParser: BodyParser[A])(f: SecuredRequest[A] => Result): Action[A] = {
    Action(bodyParser) {
      request =>
        val maybeCredentials = readQueryString(request) orElse
          readBasicAuthentication(request.headers)
        val ALLOWED_ORIGIN: String = "http://test.afterwords.ru.s3-website-eu-west-1.amazonaws.com"
        maybeCredentials.map { resultOrCredentials =>
          resultOrCredentials match {
            case Left(errorResult) => errorResult match {
              case r:SimpleResult[AnyContent] => r.withHeaders(
                ACCESS_CONTROL_ALLOW_ORIGIN -> ALLOWED_ORIGIN,
                ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
              case r => r
            }
            case Right(credentials) => {
              val (user, password) = credentials
              val maybeUser = authenticate(user, password)
              maybeUser match {
                case Some(user) => f(SecuredRequest(user, request)) match {
                  case r:SimpleResult[AnyContent] => r.withHeaders(
                    ACCESS_CONTROL_ALLOW_ORIGIN -> ALLOWED_ORIGIN,
                    ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
                  case r => r
                }
                case None => Unauthorized("Invalid user name or password").withHeaders(
                  ACCESS_CONTROL_ALLOW_ORIGIN -> ALLOWED_ORIGIN,
                  ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
              }
            }
          }
        }.getOrElse {
          Unauthorized.withHeaders(HeaderNames.WWW_AUTHENTICATE -> "Basic",
          ACCESS_CONTROL_ALLOW_ORIGIN -> ALLOWED_ORIGIN,
          ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
        }
    }
  }
  
  def AuthenticatedAction(f: SecuredRequest[AnyContent] => Result): Action[AnyContent] = AuthenticatedAction(BodyParsers.parse.anyContent)(f)
  
  def AuthenticatedAction(f: => Result): Action[AnyContent] = AuthenticatedAction(_ => f)


  def currentUser[A](implicit  request: Request[A]): Option[User] = {
    val maybeCredentials = readQueryString(request) orElse readBasicAuthentication(request.headers)
    maybeCredentials.map { resultOrCredentials =>
      resultOrCredentials match {
        case Left(errorResult) => None
        case Right(credentials) => {
          val (user, password) = credentials
          User.findOneByEmail(user)
        }
      }
    }.getOrElse {
      None
    }
  }

}