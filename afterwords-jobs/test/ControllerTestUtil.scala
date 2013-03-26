import java.lang.String
import org.apache.commons.codec.binary.Base64
import play.api.http.HeaderNames
import play.api.libs.json.JsValue
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import scala.Predef._
import securesocial.core.SecureSocial
import models.Customer

/**
 * Created with IntelliJ IDEA.
 * User: shane
 * Date: 24/03/13
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
object ControllerTestUtil {

  def createAuthorizedFakeRequest(fr: FakeRequest[AnyContent], testCustomer: Customer, requestNode: JsValue) : FakeRequest[JsValue] = {
    val encoding = Base64.encodeBase64((testCustomer.email + ":" + testCustomer.password).getBytes())
    val authString = "Basic " + new String(encoding)
    fr.copy(body = requestNode).withHeaders(
      HeaderNames.CONTENT_TYPE -> "application/json",
      HeaderNames.AUTHORIZATION -> authString).
      withSession(SecureSocial.ProviderKey -> "basicauth")
  }

  def createAuthorizedFakeRequest(testCustomer: Customer, requestNode: JsValue) : FakeRequest[JsValue] = {
    createAuthorizedFakeRequest(FakeRequest(), testCustomer, requestNode)
  }

  def createAuthorizedFakeRequest(method: String, path: String,testCustomer: Customer, requestNode: JsValue) : FakeRequest[JsValue] ={
    createAuthorizedFakeRequest(FakeRequest(method, path), testCustomer, requestNode)
  }

}
