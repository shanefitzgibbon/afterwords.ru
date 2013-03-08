package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def checkPreFlight(url: String) = Action { request =>
    import CORSHeaderNames._
    Ok("").withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "http://localhost:3501",
      ACCESS_CONTROL_ALLOW_METHODS -> "GET, POST, PUT, DELETE, OPTIONS",
      ACCESS_CONTROL_ALLOW_HEADERS -> "Content-Type, X-Requested-With, Accept, Authorization",
      ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true",
      // cache access control response for one day
      ACCESS_CONTROL_MAX_AGE -> (60 * 60 * 24).toString
    )
  }
  
}

object CORSHeaderNames {
  val ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  val ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
  val ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
  val ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
  val ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
  val ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  
  val ORIGIN = "Origin";
  val ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
  val ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
}
