package service

import java.time._
import java.time.format.DateTimeFormatter

import scala.collection.SortedMap
import scala.util.parsing.combinator._

import model.SyukujitsuBody
import service.implicits.LocalDateOrderImplicits.LocalDateOrderImplicits

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
          LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d"))
        )
    }
  private def tail1 = """.*,""".r
  private def comma = ","
  private def syuku_csv_rule = heads ~> rep(syuku_name_and_date_split <~ opt(comma)) <~ rep(tail1 ~ opt(comma))

  /*
   * parse Syukujitsu CSV
   */
  def parse(target: String): Either[String, List[SyukujitsuBody]] = parseAll(syuku_csv_rule, target) match {
    case Success(result, _) => Right(result.sortWith((a, b) => a.date.isBefore(b.date)))
    case Failure(msg, _) => Left(msg)
    case Error(msg, _) => Left(msg)
  }

  /**
   * *********************************************************************
   *
   *  List[SyukujitsuBody] to Map[Int, Map[LocalDate, String]
   *   ex.
   *    ・for
   *    ・foldLeft
   *    ・reculsive
   *
   *
   *  List[SyukujitsuBody] to Map[Int, List[SyukujitsuBody]]
   *   ex.
   *    ・reculsive
   *
   *
   */

  def convertSyukujitsuMap_for(lst: List[SyukujitsuBody]): SortedMap[Int, SortedMap[LocalDate, String]] = {
    var ret = SortedMap.empty[Int, SortedMap[LocalDate, String]]
    var tmp = SortedMap.empty[LocalDate, String]

    for (itm <- lst) {
      if (ret.isDefinedAt(itm.date.getYear)) {
        tmp = ret.get(itm.date.getYear).get
        if (tmp.isDefinedAt(itm.date)) {
          tmp.updated(itm.date, itm.date_name)
        } else {
          tmp = tmp + (itm.date -> itm.date_name)
          ret = ret.updated(itm.date.getYear, tmp)
        }
      } else {
        tmp = SortedMap.empty[LocalDate, String]
        tmp = tmp + (itm.date -> itm.date_name)
        ret = ret + (itm.date.getYear -> tmp)
      }
    }
    ret
  }

  def convertSyukujitsuMap_foldLeft(lst: List[SyukujitsuBody]): SortedMap[Int, SortedMap[LocalDate, String]] = {
    lst.foldLeft(SortedMap.empty[Int, SortedMap[LocalDate, String]]) { (r, itm) =>
      r.get(itm.date.getYear) match {
        case Some(smap_item) => {
          r.updated(
            itm.date.getYear,
            smap_item.get(itm.date) match {
              case Some(v) => smap_item.updated(itm.date, itm.date_name)
              case None => smap_item + (itm.date -> itm.date_name)
            }
          )
        }
        case None => {
          r.updated(itm.date.getYear, SortedMap(itm.date -> itm.date_name))
        }
      }
    }
  }

  def convertSyukujitsuMap_recursive(
    lst: List[SyukujitsuBody],
    mp: SortedMap[Int, SortedMap[LocalDate, String]] = SortedMap.empty[Int, SortedMap[LocalDate, String]]
  ): SortedMap[Int, SortedMap[LocalDate, String]] = lst match {
    case Nil => mp
    case head :: tail => {
      if (mp isDefinedAt (head.date.getYear)) {
        convertSyukujitsuMap_recursive(
          tail,
          mp.updated(
            head.date.getYear,
            mp.apply(head.date.getYear) + (head.date -> head.date_name)
          )
        )
      } else
        convertSyukujitsuMap_recursive(
          tail,
          mp + (head.date.getYear -> SortedMap(head.date -> head.date_name))
        )
    }
  }

  def convertSyukujitsuMap_builtin(lst: List[SyukujitsuBody]) =
    lst.groupBy(_.date.getYear).map {
      case (year, shukujitsuBodyList) =>
        year -> shukujitsuBodyList.map { s =>
          s.date -> s.date_name
        }.toMap
    }

  def convertYearMapList(
    lst: List[SyukujitsuBody],
    mp: Map[Int, List[SyukujitsuBody]] = Map.empty[Int, List[SyukujitsuBody]]
  ): Map[Int, List[SyukujitsuBody]] = lst match {
    case Nil => mp
    case head :: tail => {
      if (mp isDefinedAt (head.date.getYear)) {
        convertYearMapList(tail, mp.updated(head.date.getYear, mp.apply(head.date.getYear) ::: List(head)))
      } else
        convertYearMapList(tail, mp + (head.date.getYear -> List(head)))
    }
  }

}
