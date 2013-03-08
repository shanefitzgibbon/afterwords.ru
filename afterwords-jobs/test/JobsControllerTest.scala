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
  //imports needed for secure social support
  import com.typesafe.plugin._
  import play.api.Play.current
  import securesocial.core.SecureSocial
  import securesocial.core.providers.utils.PasswordHasher
  
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
      //running(FakeApplication(additionalConfiguration = mongoTestDatabase(), additionalPlugins = Seq("securesocial.core.providers.utils.BCryptPasswordHasher"))) {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
        val request = FakeRequest().copy(body = requestNode).withHeaders(
            HeaderNames.CONTENT_TYPE -> "application/json",
            HeaderNames.AUTHORIZATION -> "Basic ZGVtbzpkZW1v").
            withSession(SecureSocial.ProviderKey -> "basicauth")
        //request.withFormUrlEncodedBody("username" -> "test@test.com", "password" -> "Passw0rd")
        val result = Jobs.newJob()(request)
        
        status(result) must equalTo(CREATED)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        
        val responseNode = Json.parse(contentAsString(result))
        (responseNode \ "status").as[String] must equalTo("created")
        (responseNode \ "id").as[String] must not be (None)
      }
    }
    
    "route the request and create a new job when a PUT request to the jobs is made" in{
      val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
      val request = FakeRequest(POST, "/api/jobs").copy(body = requestNode).withHeaders(
          HeaderNames.CONTENT_TYPE -> "application/json",
          HeaderNames.AUTHORIZATION -> "Basic ZGVtbzpkZW1v")
      val Some(result) = routeAndCall(request)
      
      status(result) must equalTo(CREATED)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
      
      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "status").as[String] must equalTo("created")
      (responseNode \ "id").as[String] must not be (None)
    }
    
    "reject a request (403 Forbidden) when a PUT request /api/jobs is made without credentials" in {
      val requestNode = Json.toJson(Map("text" -> "This text should not be saved"))
      val request = FakeRequest(POST, "/api/jobs").copy(body = requestNode).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
      val Some(result) = routeAndCall(request)
      
      status(result) must be equalTo(UNAUTHORIZED)
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