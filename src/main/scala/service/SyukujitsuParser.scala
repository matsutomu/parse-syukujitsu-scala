package service

import scala.io.Source
import util.parsing.combinator._
import org.joda.time.format.DateTimeFormat
import model.SyukujitsuBody

/**
 *  parse syukujitsu.csv
 *  -----------------------------
 *  head1: 平成....
 *  head2: 名称,月日,....
 *  body : 元日,2016/01/01,元日,2017/01/01,....
 *  tail1: .....
 *  tail2: コメント,.....
 *
 */
object SyukujitsuParser extends RegexParsers {

  private def head1 = """(平成|昭和|明治|大正).*,""".r
  private def head2 = """名称,月日(,)?""".r
  private def heads = rep(head1) ~ rep(head2)

  private def syuku_name = """.*?,""".r
  private def syuku_date = """[\d]{4}/[\d]{1,2}/[\d]{1,2}""".r
  private def syuku_name_and_date_split = syuku_name ~ syuku_date ^^
    {
      case name ~ date =>
        SyukujitsuBody(
          name.dropRight(1),
          DateTimeFormat.forPattern("yyyy/MM/dd").parseDateTime(date)
        )
    }
  private def tail1 = """.*,""".r
  private def comma = ","

  private def syuku_csv_rule = heads ~> rep(syuku_name_and_date_split <~ opt(comma)) <~ rep(tail1 ~ opt(comma))

  def parse(target: String): Either[String, List[SyukujitsuBody]] = parseAll(syuku_csv_rule, target) match {
    case Success(result, _) => Right(result.sortWith((a, b) => a.date.isBefore(b.date)))
    case Failure(msg, _) => Left(msg)
    case Error(msg, _) => Left(msg)
  }

  def convertMapYear(lst: List[SyukujitsuBody], mp: Map[Int, List[SyukujitsuBody]] = Map.empty[Int, List[SyukujitsuBody]]): Map[Int, List[SyukujitsuBody]] = {
    lst match {
      case Nil => mp
      case head :: tail => {
        if (mp isDefinedAt (head.date.getYear)) {
          convertMapYear(tail, mp.updated(head.date.getYear, mp.apply(head.date.getYear) ::: List(head)))
        } else
          convertMapYear(tail, mp + (head.date.getYear -> List(head)))
      }
    }
  }

}
