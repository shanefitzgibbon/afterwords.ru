package models

import java.util.Date
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.novus.salat.annotations._
import Database.mongoDB

//abstract class User(email: String, name: String, password: String)
//case class Editor(email: String, name: String, password: String) extends User(email, name, password)
//case class Customer(email: String, name: String, password: String) extends User(email, name, password)
//case class Administrator(email: String, name: String, password: String) extends User(email, name, password)

case class Job(
  @Key("_id") id: ObjectId = new ObjectId,
  created: Date,
  createdBy: String,
  completed: Boolean,
  dueDate: Option[Date],
  assignedTo: Option[String],
  reviewedBy: Option[String],
  originalDocument: Document/*,
  versions: List[Version]*/)

case class Document(
  created: Date,
  createdBy: String,
  text: String  ){

//  lazy val versions: OneToMany[Version] = Database.documentToVersions.left(this)
}

case class Version(
  id: Long,
  documentId: Long,
  created: Date,
  createdBy: String,
  versionNo: Int,
  uri: String){

//  lazy val document: ManyToOne[Document] = Database.documentToVersions.right(this)
}

case class Payment(
  id: Long,
  jobId: Long,
  created: Date,
  estimatedAmount: BigDecimal,
  paymentAmount: Option[BigDecimal],
  paymentDate: Option[Date],
  paymentRef: Option[String])

object Job extends ModelCompanion[Job, ObjectId]{

  val jobsCollection = mongoDB("jobs")
  
  val dao = new SalatDAO[Job, ObjectId](collection = jobsCollection) {}
  
//  def insert(job: Job) = insert(job, WriteConcern.Safe)
  
  ///Queries
  
}

object Document {
  
  def findById(id: Long): Document = null

  /**
   * Adds a document
   */
  def insert(document: Document) = null
}

object Version {
  /**
   * Adds a version to document
   */
  def addVersion(version: Version, document: Document) = null
  
  /**
   * Insert a version
   */
  def insert(version: Version) = null
}

object Payment {
  /**
   * Insert a payment
   */
  def insert(payment: Payment) = null
}


