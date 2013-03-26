import org.apache.commons.codec.binary.Base64
import org.specs2.mutable._
import play.api._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.Some
import test.FakeApplication


@RunWith(classOf[JUnitRunner])
class JobsControllerTest extends Specification {

  import models._
  import controllers.Jobs
  import play.api.libs.json._
  import play.api.http.HeaderNames
  import ControllerTestUtil._
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
    running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
      Job.remove(MongoDBObject.empty)
      User.remove(MongoDBObject.empty)
    }
  }
  
  "The jobs controller" should {
    "create a new job when a PUT request to the jobs is made" in{
      //running(FakeApplication(additionalConfiguration = mongoTestDatabase(), additionalPlugins = Seq("securesocial.core.providers.utils.BCryptPasswordHasher"))) {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        //insert a user for this test
        val testCustomer = new Customer("job.test1@test.com", "Jobs", "Tester", "password");
        Customer.register(testCustomer);

        try {
          val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
          val request = createAuthorizedFakeRequest(testCustomer, requestNode)
          //request.withFormUrlEncodedBody("username" -> "test@test.com", "password" -> "Passw0rd")
          val result = Jobs.newJob()(request)

          status(result) must equalTo(CREATED)
          contentType(result) must beSome("application/json")
          charset(result) must beSome("utf-8")

          val responseNode = Json.parse(contentAsString(result))
          (responseNode \ "status").as[String] must equalTo("created")
          (responseNode \ "id").as[String] must not be (None)
        }
        finally {
          User.remove(testCustomer)
        }
      }
    }
    
    "route the request and create a new job when a PUT request to the jobs is made" in{
      val testCustomer = new Customer("job.test2@test.com", "Jobs", "Tester", "password");
      Customer.register(testCustomer);
      try {
        val requestNode = Json.toJson(Map("text" -> "Lorem ipsum, this text needs to edited"))
        val request = createAuthorizedFakeRequest(POST, "/api/jobs", testCustomer, requestNode)
        val Some(result) = routeAndCall(request)

        status(result) must equalTo(CREATED)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")

        val responseNode = Json.parse(contentAsString(result))
        (responseNode \ "status").as[String] must equalTo("created")
        (responseNode \ "id").as[String] must not be (None)
      }
      finally {
        User.remove(testCustomer)
      }
    }
    
    "reject a request (403 Forbidden) when a PUT request /api/jobs is made without credentials" in {
      val requestNode = Json.toJson(Map("text" -> "This text should not be saved"))
      val request = FakeRequest(POST, "/api/jobs").copy(body = requestNode).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
      val Some(result) = routeAndCall(request)
      
      status(result) must be equalTo(UNAUTHORIZED)
    }

    "return a list of pending jobs for the current logged in customer" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val testCustomer = new Customer("job.test3@test.com", "Jobs", "Tester", "password");
        Customer.register(testCustomer);
        val createdJobId1 = (Job.create("Lorem ipsum, this text needs to edited")(testCustomer)).get

        try {
          val request = createAuthorizedFakeRequest(GET, "/api/jobs/pending",testCustomer, JsNull)
          val response = routeAndCall(request)
          response should not be (none)
          val Some(result) = response

          status(result) must equalTo(OK)
          contentType(result) must beSome("application/json")
          charset(result) must beSome("utf-8")

          val responseNode = Json.parse(contentAsString(result))
          responseNode must not be (none)
        }
        finally {
          User.remove(testCustomer)
          Job.removeById(createdJobId1)
        }
      }
    }

    "return a list of completed jobs for the current logged in customer" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val testCustomer = new Customer("job.test4@test.com", "Jobs", "Tester", "password");
        Customer.register(testCustomer);
        val createdJobId1 = (Job.create("Lorem ipsum, this text needs to edited")(testCustomer)).get
        val job = Job.findOneById(createdJobId1).get
        Job.save(job.copy(completed = true))
        try {
          val request = createAuthorizedFakeRequest(GET, "/api/jobs/completed",testCustomer, JsNull)
          val response = routeAndCall(request)
          response should not be (none)
          val Some(result) = response

          status(result) must equalTo(OK)
          contentType(result) must beSome("application/json")
          charset(result) must beSome("utf-8")

          val responseNode = Json.parse(contentAsString(result))
          responseNode must not be (none)
        }
        finally {
          User.remove(testCustomer)
          Job.removeById(createdJobId1)
        }
      }
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