package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoURI
import play.api.libs.json._
import java.util.{Date, Locale}
import java.text.DateFormat
import java.text.DateFormat._
import java.util.Random

object Database {
  val rand = new Random
  val df = new java.text.SimpleDateFormat("dd-MM-yyyy")
  
  val mongoDB : MongoDB = Option(System.getenv("VCAP_SERVICES")) match {
      case Some(s) =>
        println(s)
        val vcapServicesJson = Json.parse(s)
        val credentials = ((vcapServicesJson \ "mongodb-2.0")(0) \ "credentials")
        val url = (credentials \ "url").as[String]
        val db = (credentials \ "db").as[String]
        println("mongodbdb: " + db + " url: " + url)
        val mongodb = MongoConnection(MongoURI(url))(db)
        val username = (credentials \ "username").as[String]
        val password = (credentials \ "password").as[String]
        val success = mongodb.authenticate(username, password)
        println("Auth successful? " + success)
        mongodb
      case _ => 
        println("Using localhost")
        MongoConnection()("sampleapp")
  }
  
  def createUniqueSlug(text: String) : String = text + df.format(new Date) + rand.nextLong
  
}