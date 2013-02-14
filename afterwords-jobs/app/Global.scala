import play.api.{ Application, GlobalSettings }
import models.Database
import models.Job
import models.Document
import models.Version
import models.Payment
import org.bson.types.ObjectId
import models.Version$

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    InitialData.insert
  }
}

object InitialData {

  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

  def insert() = {
    println(Job.findAll.mkString)
    if (Job.findAll.isEmpty) {
      //Test Jobs
      Seq(
        Job(new ObjectId, date("2013-01-31"), "cust1@test.com", false, Some(date("2013-02-01")), None, None, 
            Document(date("2013-01-31"), "cust1@test.com", "Text for document 1")/*, 
            Version(1, 1, date("2013-01-31"), "cust1@test.com", 0, "http://cloud.net/doc/5646846454684654654684684684eteat684684") :: Nil*/),
        Job(new ObjectId, date("2013-02-05"), "cust2@test.com", false, Some(date("2013-02-06")), None, None, 
            Document(date("2013-01-31"), "cust1@test.com", "Text for document 2")/*, List.empty[models.Version]*/),
        Job(new ObjectId, date("2013-02-10"), "cust3@test.com", false, Some(date("2013-02-11")), None, None, 
            Document(date("2013-01-31"), "cust1@test.com", "Text for document 3")/*, List.empty[models.Version]*/),
        Job(new ObjectId, date("2013-02-07"), "cust1@test.com", false, Some(date("2013-02-08")), None, None, 
            Document(date("2013-01-31"), "cust1@test.com", "Text for document 4")/*, List.empty[models.Version]*/)
      ).foreach(Job.insert)
//
//      //Test Versions
//      Seq(
//        Version(1, 1, date("2013-01-31"), "cust1@test.com", 0, "http://cloud.net/doc/5646846454684654654684684684eteat684684"),
//        Version(2, 1, date("2013-01-31"), "cust1@test.com", 0, "http://cloud.net/doc/5646846454684654654retdtdrt654654654654"),
//        Version(3, 2, date("2013-02-05"), "cust2@test.com", 0, "http://cloud.net/doc/564684645466546541864684tdrt65465dsffefes")
//      ).foreach(Version.insert)
//
//      //Test Payments
//      Seq(
//        Payment(1, 1, date("2013-01-31"), 850, None, None, None),
//        Payment(2, 2, date("2013-02-05"), 1200, Some(1200), Some(date("2013-02-07")), Some("THEUURBNKDDSYEERT"))        
//      ).foreach(Payment.insert)
    }
  }
}