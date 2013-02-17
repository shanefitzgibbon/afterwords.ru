package models

import java.util.Date
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.novus.salat.annotations._
import Database.mongoDB

abstract class User(email: String, name: String, password: String)
case class Administrator(email: String, name: String, password: String) extends User(email, name, password)
case class Editor(email: String, name: String, password: String) extends User(email, name, password)
case class Customer(email: String, name: String, password: String, paymentMethods: List[PaymentMethod]) extends User(email, name, password)

case class PaymentMethod(name: String, lastFour: Int, cryptedNumber: String, expirationDate: Date)

case class Job(
  @Key("_id") id: ObjectId = new ObjectId,
  created: Date,
  createdBy: String,
  completed: Boolean,
  dueDate: Option[Date],
  assignedTo: Option[String],
  reviewedBy: Option[String],
  originalDocument: Document,
  versions: List[Version])

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

  //  def insert(job: Job) = insert(job, WriteConcern.Safe)

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




