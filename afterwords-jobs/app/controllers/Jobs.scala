package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Jobs extends Controller {
  
  def index = Action{
    //val jobs = Job.findAll.toList
    //for (j <- jobs) yield print(Job.toPrettyJson(j))
    Ok(views.html.jobs.index(Job.findAll))
  }

  def newJob = Action(parse.json){ request =>
    val json = request.body
    val submittedText = (json \ "text").as[String]
    //TODO replace demo customer with real authenticated user
    val demoCustomer = Customer("demo@email.com", "demo", "password")
    Job.create(submittedText)(demoCustomer).map { id =>
      val success = Map(
          "status" -> "created", 
          "id" -> id.toString())
      Created(Json.toJson(success)).withHeaders(("Access-Control-Allow-Origin", "*")) 
    }.getOrElse(BadRequest)    
  }
  
}