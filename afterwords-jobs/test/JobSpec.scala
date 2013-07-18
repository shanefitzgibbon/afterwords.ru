import org.specs2.mutable._
import play.api._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JobSpec extends Specification {
  
  import models._
  import com.mongodb.casbah.Imports._
  import org.bson.types.ObjectId

  def mongoTestDatabase() = {
    Map("mongodb.default.db" -> "afterwords-test")
  }
  
  step {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        Job.remove(MongoDBObject.empty)
      }
  }
  
  "A Job" should {
    
    "be insertable into the jobs table" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val testJob = Job(
          created=date("2013-01-31"),
          createdBy="jobspectest",
          completed=false,
          dueDate=Some(date("2013-02-01")),
          assignedTo=None,
          reviewedBy=None,
          originalDocument=Document(
            created=date("2013-01-31"),
            createdBy="jobspectest",
            text= testText  
          ),
          versions= List.empty[Version])
        val newJobId = Job.insert(testJob)
        newJobId should not be (None)
        val foundJob = Job.findOneById(newJobId.get)
        foundJob should not be (None)
      }
    }
    
    "on creation, create a document version and associate it with the job" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val testCustomer = Customer("jobspec_test@email.com", "Bob", "JobSpecTest", "password")
        val createdJobId = Job.create(testText)(testCustomer)
        createdJobId should not be (None)
        val foundJob = Job.findOneById(createdJobId.get)
        foundJob should not be (none)
        foundJob.get.created should not be null
        foundJob.get.createdBy must beEqualTo("jobspec_test@email.com") 
        foundJob.get.originalDocument.text must be equalTo(testText)
        foundJob.get.originalDocument.created must not be null
        foundJob.get.originalDocument.createdBy must be equalTo("jobspec_test@email.com")
      }
    }  
    
    "on creation, initialise a payment process" in { pending }
    
    "be possible to get a list of pending jobs for a customer" in {
      val testCustomer = Customer("jobspec_test@email.com", "Bob", "JobSpecTest", "password")
      val createdJobId1 = Job.create(testText)(testCustomer)
      val createdJobId2 = Job.create(testText)(testCustomer)

      val pendingJobs = Job.findAllPending(testCustomer).toList

      pendingJobs should not be (none)
      pendingJobs must haveOneElementLike { case job: Job => job.id must_== createdJobId1.get }
      pendingJobs must haveOneElementLike { case job: Job => job.id must_== createdJobId2.get }
      pendingJobs must haveAllElementsLike { case job: Job => {
        job.createdBy must_== testCustomer.email
        job.completed must beFalse
      }}
    }
    
    "be possible to get a list of completed jobs for a customer" in {
      val testCustomer = Customer("jobspec_test@email.com", "Bob", "JobSpecTest", "password")
      val createdJobId1 = Job.create(testText)(testCustomer)
      val createdJobId2 = Job.create(testText)(testCustomer)

      //update one of the jobs to completed
      val job2 = Job.findOneById(createdJobId2.get).get
      Job.save(job2.copy(completed = true))

      val completedJobs = Job.findAllCompleted(testCustomer).toList
      completedJobs should not be (none)
      completedJobs must haveAllElementsLike { case job: Job => {
        job.createdBy must_== testCustomer.email
        job.completed must beTrue
      }}
      completedJobs must haveOneElementLike { case job: Job => job.id must_== createdJobId2.get }
    }
    
    "be possible to get a list of all pending jobs in the system" in {
      val testCustomer = Customer("jobspec_test@email.com", "Bob", "JobSpecTest", "password")
      val createdJobId1 = Job.create(testText)(testCustomer)
      val createdJobId2 = Job.create(testText)(testCustomer)

      val pendingJobs = Job.list(completed = false).items

      pendingJobs should not be (none)
      pendingJobs must haveOneElementLike { case job: Job => job.id must_== createdJobId1.get }
      pendingJobs must haveOneElementLike { case job: Job => job.id must_== createdJobId2.get }

    }

    "be possible to get a list of assigned jobs for an editor" in { pending }
    
    "be possible to get a list of completed jobs for an editor" in { pending }
    
    "be possible to get a list of jobs pending review for an editor" in { pending }
    
    "be possible to get a list of jobs reviewed by an editor" in { pending }
    
    "be able to be completed by an editor" in { pending }
    
    "on completion have at least an original version and a final version" in { pending }
    
    "be able to be reviewed by an editor" in { pending }
    
    "not be able to be review by the same editor who completed the job" in { pending }
    
    "be escalated to an administrator if not completed by the due date" in { pending }
  }
  
  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)
  
  val testText = """
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris non accumsan dolor. Morbi mollis justo ut mi tempus sollicitudin. Duis gravida, sem nec molestie vulputate, ligula purus tincidunt sapien, et lacinia mi velit posuere eros. In condimentum semper lectus eu pellentesque. Ut sollicitudin consequat neque vulputate porta. Etiam semper dapibus tempus. Morbi ut lectus nec nunc ultricies ullamcorper ut eu sem. Donec hendrerit diam porta lacus fringilla quis convallis quam faucibus. Suspendisse lectus nulla, vulputate vel adipiscing sed, convallis ac justo. Phasellus pellentesque tempor leo, sit amet porttitor dolor ultrices non. Maecenas aliquet mattis nisl. Sed euismod urna sed est euismod viverra. Integer consectetur tortor id est ultricies venenatis. Nullam egestas dolor fringilla tellus aliquet fringilla eu ut nisl. Aenean vitae dolor odio, quis posuere enim. Etiam quam odio, sodales ut accumsan in, malesuada in nulla.
  
      Curabitur purus mauris, bibendum eget consectetur nec, feugiat non nisl. Duis vulputate rhoncus felis, nec ultricies felis vehicula eget. Aenean ullamcorper gravida ipsum sed ultrices. Praesent consectetur nulla eu nisi sagittis dapibus. Ut in ipsum in quam pellentesque tempus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Proin aliquet auctor posuere. Pellentesque vitae urna sit amet nunc semper interdum.
  
      Mauris nisl massa, ultrices eget molestie sit amet, dapibus non felis. Pellentesque adipiscing magna et nunc bibendum nec malesuada turpis sollicitudin. Fusce aliquet posuere interdum. Pellentesque vulputate tempor dignissim. Nulla facilisi. In auctor ligula mollis libero molestie mollis. Nam ut sodales felis. Suspendisse in vulputate dui. Integer lacinia ultrices dignissim. Nunc luctus bibendum eros quis tempus. Curabitur varius facilisis eleifend.
    """

}