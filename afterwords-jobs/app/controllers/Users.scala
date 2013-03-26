package controllers

import models._
import play.api._
import play.api.mvc._
import libs.json.{Writes, JsValue, Reads, Json}

object Users extends Controller with service.JsonAuthentication {

  import Json._

  /**
   * Parses a User object - assumes type of user is customer
   */
  implicit object UserReads extends Reads[User] {
    def reads(json: JsValue) = Customer(
      (json \ "email").as[String],
      (json \ "firstName").as[String],
      (json \ "lastName").as[String],
      (json \ "password").as[String]
    )
  }

  implicit object UserWrites extends Writes[User] {
    def writes(u: User): JsValue = toJson(
      Map(
        "email" -> toJson(u.email),
        "firstName" -> toJson(u.firstName),
        "lastName" -> toJson(u.lastName)
      )
    )
  }

  def login = AuthenticatedAction { request =>
    Ok(toJson(request.user))
  }

}