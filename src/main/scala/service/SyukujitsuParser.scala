package service

import scala.io.Source
import util.parsing.combinator._
import org.joda.time.format.DateTimeFormat
import model._

/**
 *
 * * http://www.mwsoft.jp/programming/scala/regexparsers.html
 */
object SyukujitsuParser extends RegexParsers {

  def head = """(平成|昭和|明治|大正).*,""".r
  def head2 = """名称,月日(,)?""".r
  def heads = rep(head) ~ rep(head2)

  def syuku_name = """.*?,""".r
  def syuku_date = """[\d]{4}/[\d]{1,2}/[\d]{1,2}""".r
  def syuku_name_and_date_split = syuku_name ~ syuku_date ^^
    {
      case name ~ date =>
        SyukujitsuBody(
          name.dropRight(1),
          DateTimeFormat.forPattern("yyyy/MM/dd").parseDateTime(date)
        )
    }
  def tail1 = """.*,""".r
  def comma = ","

  def test = heads ~> rep(syuku_name_and_date_split <~ opt(comma)) <~
    rep(tail1 ~ opt(comma))

  def parse(target: String): Either[String, List[SyukujitsuBody]] = parseAll(test, target) match {
    case Success(result, _) => Right(result.sortWith((a, b) => a.date.isBefore(b.date)))
    case Failure(msg, _) => Left(msg)
    case Error(msg, _) => Left(msg)

  }

}
