package pl.umk.bugclassification.scmparser

import pl.umk.bugclassification.scmparser.gerrit.GerritSshEventsStreamListeningInvoker
import pl.umk.bugclassification.scmparser.git.parsers.CommitParser
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.git.ParserInvoker
import pl.umk.bugclassification.scmparser.messages.LearnOnAllProjects
import pl.umk.bugclassification.scmparser.messages.LearnOnProject
import pl.umk.bugclassification.scmparser.messages.PreprareAllProjects
import pl.umk.bugclassification.scmparser.training.ModelDAOImpl

object Launcher extends LoggingConf {

  def main(args: Array[String]): Unit = {
    val controller = new Controller(29418, "machina", "mfejzer", "/home/mfejzer/src/bonus", new ModelDAOImpl)
    val worker = new Worker(29418, "machina", controller)
    controller ! LearnOnProject("tmp")
    controller ! PreprareAllProjects
    controller ! LearnOnAllProjects
  }

  def tmp = {
    val listeningInvoker = new GerritSshEventsStreamListeningInvoker(29418, "localhost",
      project => ref => sha1 => {
        println(project)
        println(ref)
        println(sha1)
      })
  }

  def temporaryParserTest = {
    val parserInvker: ParserInvoker =
      new GitParserInvoker("kbc", "/home/mfejzer/projekt/kbc")
    //temporaryDiffTests(parser)
    temporaryBlameTests(parserInvker)
  }

  def temporaryBlameTests(parserInvker: ParserInvoker) = {
    val commitsParsed = parserInvker.listLoggedCommits()
    val tmpCommit = commitsParsed(0)

    val result = parserInvker.extractBlame(tmpCommit, tmpCommit.filenames(0))
    println(result)
    //    println(BlameParser.parse(BlameParser.blame,result.split("\n")(0)))
    //    BlameParser.parse(BlameParser.blameList,result).map(x => println(x))
    parserInvker.blameOnCommitParentForFile(tmpCommit, tmpCommit.filenames(0)).
      map(x => println(x))
  }

  def temporaryDiffTests(parserInvker: ParserInvoker) = {
    val commitsParsed = parserInvker.listLoggedCommits()
    //    commitsParsed.foreach(x => {
    //      println(x)
    //      println("Contains fix: " + x.containsFix())
    //    })
    val tmpCommit = commitsParsed(0)
    val result = parserInvker.extractDiffFromCommitForFile(tmpCommit, tmpCommit.filenames(0))
    println(result.mkString("\n"))
  }

  def temporaryCommitParserTests() = {
    println(CommitParser.parse(CommitParser.authorName, "Mikołaj"))
    //    println(CommitParser.parse(CommitParser.authorName, "Mikołaj\n"))
    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj\n"))
    //    println(CommitParser.parse(CommitParser.author, "Author: Mikołaj"))
    println(CommitParser.parse(CommitParser.sha1Text, "b15d78fd348c963d5df649a986b31c9b2dd36b43\n"))
    println(CommitParser.parse(CommitParser.dateValue, "Tue Nov 6 22:37:00 2012 +0100"))
    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100\n"))
    println(CommitParser.parse(CommitParser.date, "Date:   Tue Nov 6 22:37:00 2012 +0100"))
  }
}