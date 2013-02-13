package models

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

object Database extends Schema {
  val jobsTable = table[Job]("jobs")
  val documentsTable = table[Document]("documents")
  val paymentsTable = table[Payment]("payments")
  val versionsTable = table[Version]("versions")
  
  val documentToVersions = oneToManyRelation(documentsTable, versionsTable).via((d, v) => d.id === v.documentId)
  
}