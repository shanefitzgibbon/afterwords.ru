package models

import java.util.Date
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.KeyedEntity
import org.squeryl.dsl.OneToMany
import org.squeryl.dsl.ManyToOne

//abstract class User(email: String, name: String, password: String)
//case class Editor(email: String, name: String, password: String) extends User(email, name, password)
//case class Customer(email: String, name: String, password: String) extends User(email, name, password)
//case class Administrator(email: String, name: String, password: String) extends User(email, name, password)

case class Job(
  id: Long,
  created: Date,
  createdBy: String,
  completed: Boolean,
  dueDate: Option[Date],
  assignedTo: Option[String],
  reviewedBy: Option[String]) extends KeyedEntity[Long]

case class Document(
  id: Long,
  jobId: Long,
  created: Date,
  createdBy: String) extends KeyedEntity[Long] {

  lazy val versions: OneToMany[Version] = Database.documentToVersions.left(this)
}

case class Version(
  id: Long,
  documentId: Long,
  created: Date,
  createdBy: String,
  versionNo: Int,
  uri: String) extends KeyedEntity[Long] {

  lazy val document: ManyToOne[Document] = Database.documentToVersions.right(this)
}

case class Payment(
  id: Long,
  jobId: Long,
  created: Date,
  estimatedAmount: BigDecimal,
  paymentAmount: Option[BigDecimal],
  paymentDate: Option[Date],
  paymentRef: Option[String]) extends KeyedEntity[Long]

object Job {
  import Database.jobsTable

  /**
   * Adds a job
   */
  def insert(job: Job) = inTransaction {
    jobsTable.insert(job.copy())
  }
  
  ///Queries
  
  def findAll = inTransaction{
    from (jobsTable)(job => select(job) orderBy(job.dueDate desc)).toList
  }
}

object Document {
  import Database.documentsTable
  
  def findById(id: Long): Document = null

  /**
   * Adds a document
   */
  def insert(document: Document) = inTransaction {
    documentsTable.insert(document.copy())
  }
}

object Version {
  import Database.versionsTable
  /**
   * Adds a version to document
   */
  def addVersion(version: Version, document: Document) = inTransaction {
    document.versions.associate(version.copy())
  }
  
  /**
   * Insert a version
   */
  def insert(version: Version) = inTransaction {
    versionsTable.insert(version.copy())
  }
}

object Payment {
  import Database.paymentsTable
  
  /**
   * Insert a payment
   */
  def insert(payment: Payment) = inTransaction {
    paymentsTable.insert(payment.copy())
  }
}


