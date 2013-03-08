package controllers

import models._
import CORSHeaderNames._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Jobs extends Controller with service.JsonAuthentication {
  
  def index = Action {
    //val jobs = Job.findAll.toList
    //for (j <- jobs) yield print(Job.toPrettyJson(j))
    Ok(views.html.jobs.index(Job.findAll))
  }

  def newJob = AuthenticatedAction(parse.json) { request =>
    val json = request.body
    val submittedText = (json \ "text").as[String]
    //TODO replace demo customer with real authenticated user
    val demoCustomer = Customer("demo@email.com", "demo", "password")
    Job.create(submittedText)(demoCustomer).map { id =>
      val success = Map(
          "status" -> "created", 
          "id" -> id.toString())
      Created(Json.toJson(success)).withHeaders(
            ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
            ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true") 
    }.getOrElse(BadRequest)    
  }
  
}