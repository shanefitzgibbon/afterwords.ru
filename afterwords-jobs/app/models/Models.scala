package models

import java.util.Date
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.novus.salat.annotations._
import Database.mongoDB

abstract class User{
  val email: String
  val name: String
  val password: String
}
case class Administrator(email: String, name: String, password: String) extends User
case class Editor(email: String, name: String, password: String) extends User
case class Customer(email: String, name: String, password: String, paymentMethods: List[PaymentMethod] = List.empty[PaymentMethod]) extends User

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

}

object Customer extends ModelCompanion[Customer, ObjectId] {
  
  val customersCollection = mongoDB("customers")
  
  val dao = new SalatDAO[Customer, ObjectId](collection = customersCollection){}
} 

object Document {

  def findById(id: Long): Document = null

  /**
   * Adds a document
   */
  def insert(document: Document) = null
}




