package pl.umk.bugclassification.scmparser

import scala.actors.Actor

import pl.umk.bugclassification.scmparser.messages.Classify
import pl.umk.bugclassification.scmparser.messages.ClassifyOnProject
import pl.umk.bugclassification.scmparser.messages.LearnOnAllProjects
import pl.umk.bugclassification.scmparser.messages.LearnOnProject
import pl.umk.bugclassification.scmparser.messages.LearnOnProjectAndBranch
import pl.umk.bugclassification.scmparser.messages.PreprareAllProjects
import pl.umk.bugclassification.scmparser.messages.PreprareMissingProjects
import pl.umk.bugclassification.scmparser.training.ModelDAO

class Controller(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String,
  private val modelDao: ModelDAO) extends Actor {

  var projectInvokers = new ProjectPreparer(port, hostname, user, directory, modelDao).allProjectInvokers
  start

  def act() = {
    loop {
      receive {
        case PreprareAllProjects => {
          projectInvokers = new ProjectPreparer(port, hostname, user, directory, modelDao).allProjectInvokers
        }
        case PreprareMissingProjects => {
          projectInvokers.++(new ProjectPreparer(port, hostname, user, directory, modelDao).missingProjectInvokers)
        }
        case LearnOnAllProjects => {
          val learner = new Learner(projectInvokers)
          learner.trainAll
        }
        case LearnOnProject(project: String) => {
          val learner = new Learner(projectInvokers)
          learner.trainOnProject(project)
        }
        case LearnOnProjectAndBranch(project: String, branch: String) => {
          val learner = new Learner(projectInvokers)
          learner.trainOnProject(project, branch)
        }
        case ClassifyOnProject(project: String, ref: String, sha1: String) => {
          projectInvokers.get(project).foreach(projectInvoker => projectInvoker ! Classify(ref, sha1))
        }
      }
    }
  }

}