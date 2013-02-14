import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class MongoConfigurationSpec extends Specification {
  
  "Given vcap services env var exists, the mongo configuration" should {
    "create a url from the vcap services json" in {
      getMongoUrl must_== "mongodb://ae9daaf8-cbd9-4f34-a098-4e19f811e236:c9c50e46-19c1-428a-b7de-9cca6b210bc2@127.0.0.1:25001/db"
    }
    "extract the db name from the vcap services json" in {
      pending
    }
    "extract the username and password from the vcap services json" in {
      pending
    }
    "perform autheticate on the mongo DB with the supplied credentials" in {
      pending
    }
  }

  val vcapServicesJson: JsValue = Json.parse("""
      {"mongodb-2.0":[{
          "name":"mongodb-b2b4d",
          "label":"mongodb-2.0",
          "plan":"free",
          "tags":["nosql","document","mongodb-2.0","mongodb"],
          "credentials": {
              "hostname":"127.0.0.1",
              "host":"127.0.0.1",
              "port":25001,
              "username":"ae9daaf8-cbd9-4f34-a098-4e19f811e236",
              "password":"c9c50e46-19c1-428a-b7de-9cca6b210bc2",
              "name":"1e49621d-1c45-4420-a81b-a9a26b8ebca4",
              "db":"db",
              "url":"mongodb://ae9daaf8-cbd9-4f34-a098-4e19f811e236:c9c50e46-19c1-428a-b7de-9cca6b210bc2@127.0.0.1:25001/db"}
          }
      ]}
      """)
      
    def getMongoUrl: String = {
      val urlString = ((vcapServicesJson \ "mongodb-2.0")(0) \ "credentials" \ "url").as[String]
      println("URL: " + urlString)
      urlString
    }
}