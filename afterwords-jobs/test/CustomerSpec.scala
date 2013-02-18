import org.specs2.mutable._
import play.api._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CustomerSpec extends Specification {
  
  import models._
  import com.mongodb.casbah.Imports._
  import org.bson.types.ObjectId

  def mongoTestDatabase() = {
    Map("mongodb.default.db" -> "afterwords-test")
  }
  
  step {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        Customer.remove(MongoDBObject.empty)
      }
  }
  
  "A Customer" should {    
    "be insertable into the customers collection" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val testCustomer = new Customer(email="test@smfsoftware.com.au", name="Test User", password="password", paymentMethods=List.empty[PaymentMethod])
        val newCustomerId = Customer.insert(testCustomer)
        newCustomerId should not be (None)
        val foundCustomer = Customer.findOneById(newCustomerId.get)
        foundCustomer should not be (None)
      }
    }
  }
}