import com.mongodb.casbah.Imports._
import controllers.CORSHeaderNames
import models.Customer
import org.apache.commons.codec.binary.Base64
import org.specs2.mutable._
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.test._
import play.api.test.FakeApplication
import play.api.test.FakeApplication
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.Some
import scala.Some

@RunWith(classOf[JUnitRunner])
class CustomersControllerSpec extends Specification {

  import controllers.Customers
  import models._
  import com.mongodb.casbah.Imports._

  def mongoTestDatabase() = {
    Map("mongodb.default.db" -> "afterwords-test")
  }
  
  step {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        User.remove(MongoDBObject.empty)
      }
  }

  "The customers controller" should {
    "create a new customer when a POST request to register the customer is made" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        //do not need authorization for this request
        val requestNode = Json.toJson(Map(
          "email" -> "test.user0@mytest.ru",
          "firstName" -> "test",
          "lastName" -> "user",
          "password" -> "password"
        ))
        val request = FakeRequest().copy(body = requestNode);
        val result = Customers.register()(request)

        status(result) must equalTo(CREATED)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")

        val responseNode = Json.parse(contentAsString(result))
        (responseNode \ "status").as[String] must equalTo("created")
        (responseNode \ "id").as[String] must not be (None)
        (responseNode \ "email").as[String] must equalTo("test.user0@mytest.ru")
        (responseNode \ "firstName").as[String] must equalTo("test")
        (responseNode \ "lastName").as[String] must equalTo("user")
      }
    }

    "return an error with a request to register a customer with the same email as an existing user" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        //create first user
        val requestNode = Json.toJson(Map(
          "email" -> "test.user@mytest.ru",
          "firstName" -> "test",
          "lastName" -> "user",
          "password" -> "password"
        ))
        val request = FakeRequest().copy(body = requestNode);
        val result = Customers.register()(request)
        status(result) must equalTo(CREATED)
        //create another user with the same email
        val requestNode2 = Json.toJson(Map(
          "email" -> "test.user@mytest.ru",
          "firstName" -> "test2",
          "lastName" -> "user2",
          "password" -> "password2"
        ))
        val request2 = FakeRequest().copy(body = requestNode2);
        val result2 = Customers.register()(request2)
        status(result2) must equalTo(NOT_MODIFIED)
      }
    }

    "allow a customer to login and return the user object that matches the authorization" in {
      val testCustomer = new Customer("login.test@test.com", "Login", "Tester", "password");
      Customer.register(testCustomer);

      val encoding = Base64.encodeBase64((testCustomer.email + ":" + testCustomer.password).getBytes())
      val authString = "Basic " + new String(encoding)

      val request = FakeRequest(GET, "/api/login").withHeaders(
        HeaderNames.CONTENT_TYPE -> "application/json",
        HeaderNames.AUTHORIZATION -> authString)


      val Some(result) = routeAndCall(request);

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
      header(CORSHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, result) must not be (None)
      header(CORSHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, result) must not be (None)

      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "email").as[String] must equalTo(testCustomer.email)
      (responseNode \ "firstName").as[String] must equalTo(testCustomer.firstName)
      (responseNode \ "lastName").as[String] must equalTo(testCustomer.lastName)
    }


    "route the request and create a new customer when a POST request to register the customer is made" in {
      val requestNode = Json.toJson(Map(
        "email" -> "test.user3@mytest.ru",
        "firstName" -> "test",
        "lastName" -> "user",
        "password" -> "password"
      ))
      val request = FakeRequest(POST, "/api/users/register").copy(body = requestNode);
      val Some(result) = routeAndCall(request)

      status(result) must equalTo(CREATED)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")

      val responseNode = Json.parse(contentAsString(result))
      (responseNode \ "status").as[String] must equalTo("created")
      (responseNode \ "id").as[String] must not be (None)
      (responseNode \ "email").as[String] must equalTo("test.user3@mytest.ru")
      (responseNode \ "firstName").as[String] must equalTo("test")
      (responseNode \ "lastName").as[String] must equalTo("user")
    }



  }
}
