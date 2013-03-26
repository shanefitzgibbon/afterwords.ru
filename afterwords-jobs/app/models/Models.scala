package models

import java.util.Date
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.novus.salat.annotations._
import Database.mongoDB
import com.mongodb

@Salat
sealed abstract class User{
  val email: String
  val firstName: String
  val lastName: String
  val password: String
}
case class Administrator(email: String, firstName: String, lastName: String, password: String) extends User
case class Editor(email: String, firstName: String, lastName: String, password: String) extends User
case class Customer(email: String, firstName: String, lastName: String, password: String, paymentMethods: List[PaymentMethod] = List.empty[PaymentMethod]) extends User

case class PaymentMethod(name: String, lastFour: Int, cryptedNumber: String, expirationDate: Date)

case class Job(
  @Key("_id") id: ObjectId = new ObjectId,
  created: Date,
  createdBy: String,
  completed: Boolean,
  dueDate: Option[Date],
  assignedTo: Option[String] = None,
  reviewedBy: Option[String] = None,
  originalDocument: Document,
  versions: List[Version] = List.empty[Version])

case class Document(
  created: Date,
  createdBy: String,
  text: String)

case class Version(
  id: Long,
  documentId: Long,
  created: Date,
  createdBy: String,
  versionNo: Int,
  uri: String)

case class Payment(
  id: Long,
  jobId: Long,
  created: Date,
  estimatedAmount: BigDecimal,
  paymentAmount: Option[BigDecimal],
  paymentDate: Option[Date],
  paymentRef: Option[String])

object Job extends ModelCompanion[Job, ObjectId] {

  val jobsCollection = mongoDB("jobs")

  val dao = new SalatDAO[Job, ObjectId](collection = jobsCollection) {}

  def create(documentText: String)(implicit user: User) = {
    val newDocument = Document(created=new Date, createdBy=user.email, text=documentText)
    val newJob = Job(
        created=new Date,
        createdBy=user.email,
        completed=false,
        dueDate=None, //should calculate +2 business days here
        originalDocument = newDocument
    )
    insert(newJob)
  }

  ///Queries
  def findAllForCustomer(customer: Customer) = {
    dao.find(MongoDBObject("createdBy" -> customer.email))
      .sort(orderBy = MongoDBObject("created" -> -1))
  }

  def findAllPending(customer: Customer) = {
    dao.find(MongoDBObject("createdBy" -> customer.email, "completed" -> false))
      .sort(orderBy = MongoDBObject("created" -> -1))
  }

  def findAllCompleted(customer: Customer) = {
    dao.find(MongoDBObject("createdBy" -> customer.email, "completed" -> true))
      .sort(orderBy = MongoDBObject("created" -> -1))
  }

}

object User extends ModelCompanion[User, ObjectId] {

  val usersCollection = mongoDB("users")
  usersCollection.ensureIndex( MongoDBObject("email" -> 1 ) , "email_index", true )
  val dao = new SalatDAO[User, ObjectId](collection = usersCollection){}

  def findOneByEmail(email: String) = dao.findOne(MongoDBObject("email" -> email))
}

object Customer {
  
  def register(newCustomer: Customer) = {
    //TODO add checks to ensure that no two customers have same email address
    try{
      User.insert(newCustomer, WriteConcern.Safe)
    }
    catch {
      case ex: mongodb.MongoException.DuplicateKey => throw new DuplicateCustomerException(ex.getMessage)
      case ex: mongodb.MongoException.Network => None
    }
  }
} 

object Document {

  def findById(id: Long): Document = null

  /**
   * Adds a document
   */
  def insert(document: Document) = null
}

case class DuplicateCustomerException(message: String) extends Exception




