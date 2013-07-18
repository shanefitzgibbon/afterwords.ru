package controllers

import models._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._

object Jobs extends Controller with service.JsonAuthentication {

  implicit object DateFormat extends Format[java.util.Date] {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def reads(json:JsValue): java.util.Date = format.parse(json.as[String])
    def writes(date:java.util.Date) = JsString(format.format(date))
  }

  implicit object DocumentWrites extends Writes[Document] {
    def writes(d: Document): JsValue = toJson(
      Map(
        "created" -> toJson(d.created),
        "createdBy" -> toJson(d.createdBy),
        "text" -> toJson(d.text)
      )
    )
  }

  implicit object JobWrites extends Writes[Job] {
    def writes(j: Job): JsValue = toJson(
      Map(
        "id" -> toJson(j.id.toString),
        "created" -> toJson(j.created),
        "createdBy" -> toJson(j.createdBy),
        "completed" -> toJson(j.completed),
        "dueDate" -> toJson(j.dueDate),
        "originalDocument" -> toJson(j.originalDocument)
      )
    )
  }
  
  def index = Action {
    //val jobs = Job.findAll.toList
    //for (j <- jobs) yield print(Job.toPrettyJson(j))
    Ok(views.html.jobs.index(Job.findAll))
  }

  def list(page:Int, orderBy: Int, state: String) = Action {
    Ok(views.html.jobs.list(Job.list(page = page, orderBy = orderBy, state = state), orderBy))
  }

  def newJob = AuthenticatedAction(parse.json) { request =>
    val json = request.body
    val submittedText = (json \ "text").as[String]
    request.user match {
      case customer : Customer => {
        Job.create(submittedText)(customer).map { id =>
          val success = Map(
              "status" -> "created",
              "id" -> id.toString())
          Created(Json.toJson(success))
        }.getOrElse(BadRequest)
      }
      case _ => BadRequest
    }
  }

  def pendingJobs = AuthenticatedAction { request =>
    request.user match {
      case customer : Customer => Ok(Json.toJson(Job.findAllPending(customer).toList))
      case _ => BadRequest
    }
  }

  def completedJobs = AuthenticatedAction { request =>
    request.user match {
      case customer : Customer => Ok(Json.toJson(Job.findAllCompleted(customer).toList))
      case _ => BadRequest
    }
  }


  
}