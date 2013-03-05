import org.specs2.mutable._
import play.api._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class JobsControllerTest extends Specification {
  
  import controllers.Jobs
  import play.api.libs.json._
  import play.api.http.HeaderNames
  
  def mongoTestDatabase() = {
    Map("mongodb.default.db" -> "afterwords-test")
  }
  
  step {
    import com.mongodb.casbah.Imports._
    import models.Job
    running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
      Job.remove(MongoDBObject.empty)
    }
  }
  
  "The jobs controller" should {
    "create a new job when a PUT request to the jobs is made" in{
      val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
      val request = FakeRequest().copy(body = requestNode).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
      val result = Jobs.newJob()(request)
      
      status(result) must equalTo(CREATED)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
      
      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "status").as[String] must equalTo("created")
      (responseNode \ "id").as[String] must not be (None)
    }
    
    "route the request and create a new job when a PUT request to the jobs is made" in{
      val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
      val request = FakeRequest(PUT, "/jobs").copy(body = requestNode).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
      val Some(result) = routeAndCall(request)
      
      status(result) must equalTo(CREATED)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
      
      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "status").as[String] must equalTo("created")
      (responseNode \ "id").as[String] must not be (None)
    }
  }
  
//  "respond to the register Action" in {
//    val requestNode = Json.toJson(Map("name" -> "Testname"))
//    val request = FakeRequest().copy(body = requestNode)
//        .withHeaders(HeaderNames.CONTENT_TYPE -> "application/json");
//    val result = controllers.Users.register()(request)
//
//    status(result) must equalTo(OK)
//    contentType(result) must beSome("application/json")
//    charset(result) must beSome("utf-8")
//
//    val responseNode = Json.parse(contentAsString(result))
//    (responseNode \ "success").as[Boolean] must equalTo(true)
//  }

}