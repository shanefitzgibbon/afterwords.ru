package service

import play.api.Application
import play.api.mvc._
import play.api.Play.current
import securesocial.core._
import com.typesafe.plugin._
import securesocial.core.providers.utils.PasswordHasher

class BasicAuthIdentityProvider(application: Application) extends IdentityProvider(application) {
  
  def providerId = BasicAuthIdentityProvider.BasicAuth

  def authMethod = AuthenticationMethod("basicAuth")
  
  def doAuth[A]()(implicit request: Request[A]): Either[Result, SocialUser] = {
    request.headers.get("Authorization").flatMap { authorization =>
      authorization.split(" ").drop(1).headOption.map { encoded =>
        new String(org.apache.commons.codec.binary.Base64.decodeBase64(encoded.getBytes)).split(":").toList match {
          case u :: p :: Nil => {
            val userId = UserId(u, providerId)
            UserService.find(userId) match {
              //TODO Use hash algorithm here - use[PasswordHasher].matches(user.passwordInfo.get, p)
              case Some(user) if user.passwordInfo.isDefined && (user.passwordInfo.get == p) => Right(user)
              case _ => Left(Results.Unauthorized)
            }
          }
          case _ => Left(Results.Unauthorized)
        }
      }
    }.getOrElse {
      Left(Results.Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
    }
  }
  
  def fillProfile(user: SocialUser) = {
    // nothing to do for this provider, the user should already have everything because it
    // was loaded from the backing store
    user
  }
}

object BasicAuthIdentityProvider {
  val BasicAuth = "basicauth"
  private val key = "securesocial.basicauth.withBasicAuthSupport"
  private val sendWelcomeEmailKey = "securesocial.basicauth.sendWelcomeEmail"
  private val enableGravatarKey = "securesocial.basicauth.enableGravatarSupport"

  val withUserNameSupport = current.configuration.getBoolean(key).getOrElse(false)
  val sendWelcomeEmail = current.configuration.getBoolean(sendWelcomeEmailKey).getOrElse(true)
  val enableGravatar = current.configuration.getBoolean(enableGravatarKey).getOrElse(true)
}