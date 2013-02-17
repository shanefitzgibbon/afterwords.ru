import play.api.{ Application, GlobalSettings }

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    
    
    InitialData.insert
  }
}

object InitialData {

  import models._
  import org.bson.types.ObjectId

  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

  def insert() = {
    println(Job.findAll.mkString)
    if (Job.findAll.isEmpty) {
      //Test Customer
      val testCustomer = new Customer(email="cust1@test.com", name="Test User", password="password", paymentMethods=List.empty[PaymentMethod])
      
      //Test Jobs
      Seq(
        Job(created=date("2013-01-31"), 
            createdBy="cust1@test.com", 
            completed=false, 
            dueDate=Some(date("2013-02-01")), 
            assignedTo=None, 
            reviewedBy=None, 
            originalDocument=Document(date("2013-01-31"), "cust1@test.com", "Text for document 1"), 
            versions=Version(1, 1, date("2013-01-31"), "cust1@test.com", 0, "http://cloud.net/doc/5646846454684654654684684684eteat684684") :: Nil),
        Job(created=date("2013-02-05"), 
            createdBy="cust2@test.com", 
            completed=false, 
            dueDate=Some(date("2013-02-06")), 
            assignedTo=None, 
            reviewedBy=None, 
            originalDocument=Document(date("2013-01-31"), "cust1@test.com", "Text for document 2"), 
            versions=List.empty[models.Version]),
        Job(created=date("2013-02-10"), 
            createdBy="cust3@test.com", 
            completed=false, 
            dueDate=Some(date("2013-02-11")), 
            assignedTo=None, 
            reviewedBy=None, 
            originalDocument=Document(date("2013-01-31"), "cust1@test.com", "Text for document 3"), 
            versions=List.empty[models.Version]),
        Job(created=date("2013-02-07"), 
            createdBy="cust1@test.com", 
            completed=false, 
            dueDate=Some(date("2013-02-08")), 
            assignedTo=None, 
            reviewedBy=None, 
            originalDocument=Document(date("2013-01-31"), "cust1@test.com", "Text for document 4"), 
            versions=List.empty[models.Version])
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