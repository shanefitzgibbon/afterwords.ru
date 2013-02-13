import models.Job

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import play.api.test._
import play.api.test.Helpers._

import org.squeryl.PrimitiveTypeMode.inTransaction

class JobSpec extends FlatSpec with ShouldMatchers {
  
  "A Job" should "be insertable into the jobs table" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())){
      inTransaction{
        val job = Job.insert(Job(0, date("2013-01-31"), "JobSpec1@test.com", false, Some(date("2013-02-01")), None, None))
        job.id should not equal (0)
      }
    }
  }
  
  it should "on creation, create a document version and associate it with the job" is (pending)  
  
  it should "on creation, populate the created field with the current date and time" is (pending)
  
  it should "on creation, populate the 'created by' with the current user" is (pending)
  
  it should "on creation, initialise a payment process" is (pending)
  
  it should "be possible to get a list of pending jobs for a customer" is (pending)
  
  it should "be possible to get a list of completed jobs for a customer" is (pending)
  
  it should "be possible to get a list of assigned jobs for an editor" is (pending)
  
  it should "be possible to get a list of completed jobs for an editor" is (pending)
  
  it should "be possible to get a list of jobs pending review for an editor" is (pending)
  
  it should "be possible to get a list of jobs reviewed by an editor" is (pending)
  
  it should "be able to be completed by an editor" is (pending)
  
  it should "on completion have at least an original version and a final version" is (pending)
  
  it should "be able to be reviewed by an editor" is (pending)
  
  it should "not be able to be review by the same editor who completed the job" is (pending)
  
  it should "be escalated to an administrator if not completed by the due date" is (pending)
  
  def date(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

}