package controllers

import models._
import CORSHeaderNames._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Customers extends Controller  with service.JsonAuthentication{


  
  def find(email: String) = AuthenticatedAction { request =>
    //TODO check that auth credentials matches the supplied email
    val demoCustomer = Customer("demo@email.com", "demo", "password")
    
    //TODO after migrating to Play Framework 2.1 move this code to a separate filter
    Ok(Customer.toPrettyJson(demoCustomer)).withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
      ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true")
  }

}