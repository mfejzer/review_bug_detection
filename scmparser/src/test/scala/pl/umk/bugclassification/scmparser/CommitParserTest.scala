package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.utils.CommitParser
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommitParserSuite extends FunSuite {
  def test_it = execute()

  test("log parsing test") {
    val log = """commit b15d78fd348c963d5df649a986b31c9b2dd36b43
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Tue Nov 6 22:37:00 2012 +0100

    Removed ArgsState, added AlgorithmStatus
    
    kb,kbc and persistance layer changed to use new type

commit 1ccdd6fc09cd8cfebeb5e5a796f644294ac46208
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Mon Nov 5 21:36:13 2012 +0100

    Changed ReductionRule to use record syntax

commit ff9b7ce7810330984eb346437a58f80b4f6c33ed
Author: Mikołaj Fejzer <mfejzer@gmail.com>
Date:   Sun Sep 30 19:49:00 2012 +0200

    Fixed kb to remove axioms with lhs and rhs equal

"""
    val result = CommitParser.parse(CommitParser.commitList, log)
    assert(result.successful)
  }
}