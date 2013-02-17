package controllers

import models._
import play.api._
import play.api.mvc._

object Jobs extends Controller {
  
  def index = Action{
    val jobs = Job.findAll.toList
    //for (j <- jobs) yield print(Job.toPrettyJson(j))
    Ok(views.html.jobs.index(jobs))
  }


}