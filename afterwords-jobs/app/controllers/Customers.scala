package controllers

import models._
import CORSHeaderNames._
import play.api._
import play.api.mvc._
import libs.json.{JsValue, Reads, Json}

object Customers extends Controller  with service.JsonAuthentication{

  /**
   * Parses a JSON object
   */
  implicit object CustomerReads extends Reads[Customer] {
    def reads(json: JsValue) = Customer(
      (json \ "email").as[String],
      (json \ "firstName").as[String],
      (json \ "lastName").as[String],
      (json \ "password").as[String]
    )
  }

//  def find(email: String) = AuthenticatedAction { request =>
//    //TODO
//    val demoCustomer = Customer("demo@email.com", "demo", "user", "password")
//
//    //TODO after migrating to Play Framework 2.1 move this code to a separate filter
//    Ok(Customer.toPrettyJson(demoCustomer)).withHeaders(
//      ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
//      ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
//  }

  /**
   * Create a new customer in the database then return the user details
   * @return
   */
  def register = Action(parse.json) { request =>
    val customerJson = request.body
    val newCustomer = customerJson.as[Customer]
    try {
      Customer.register(newCustomer).map {
        id =>
          val created = User.findOneById(id).get
          val success = Map(
            "status" -> "created",
            "id" -> id.toString(),
            "email" -> created.email,
            "firstName" -> created.firstName,
            "lastName" -> created.lastName
          )
          Created(Json.toJson(success)).withHeaders(
            ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
            ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
      }.getOrElse(BadRequest)
    }
    catch {
      case ex: DuplicateCustomerException => NotModified.withHeaders(
        ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
        ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
    }

  }
}