package pl.umk.bugclassification.scmparser.git.parsers.results

class Blame(val sha1: String, val filename: String, val metadata: String, val line: String) {

  override def toString = {
    "Blame\n" +
      "Sha1: " + sha1 + "\n" +
      "File: " + filename + "\n" +
      "Metadata: " + metadata + "\n" +
      "Line:" + line + "\n"
  }
}