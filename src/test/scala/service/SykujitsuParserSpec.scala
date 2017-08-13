package service

import java.time.LocalDate

import org.scalatest._

import scala.collection.SortedMap
import model.SyukujitsuBody
import service.implicits.LocalDateOrderImplicits.LocalDateOrderImplicits

import scala.collection.immutable.ListMap

/**
  *
  */
class SykujitsuParserSpec extends FunSpec with Matchers {

  var testOK =
    """
      |平成28年（2016年）,,平成29年（2017年）,,平成30年（2018年）,
      |名称,月日,名称,月日,名称,月日
      |元日,2016/1/1,元日,2017/1/1,元日,2018/1/1
      |成人の日,2016/1/11,成人の日,2017/1/9,成人の日,2018/1/8
      |建国記念の日,2016/2/11,建国記念の日,2017/2/11,建国記念の日,2018/2/11
      |春分の日,2016/3/20,春分の日,2017/3/20,春分の日,2018/3/21
      |昭和の日,2016/4/29,昭和の日,2017/4/29,昭和の日,2018/4/29
      |憲法記念日,2016/5/3,憲法記念日,2017/5/3,憲法記念日,2018/5/3
      |みどりの日,2016/5/4,みどりの日,2017/5/4,みどりの日,2018/5/4
      |こどもの日,2016/5/5,こどもの日,2017/5/5,こどもの日,2018/5/5
      |海の日,2016/7/18,海の日,2017/7/17,海の日,2018/7/16
      |山の日,2016/8/11,山の日,2017/8/11,山の日,2018/8/11
      |敬老の日,2016/9/19,敬老の日,2017/9/18,敬老の日,2018/9/17
      |秋分の日,2016/9/22,秋分の日,2017/9/23,秋分の日,2018/9/23
      |体育の日,2016/10/10,体育の日,2017/10/9,体育の日,2018/10/8
      |文化の日,2016/11/3,文化の日,2017/11/3,文化の日,2018/11/3
      |勤労感謝の日,2016/11/23,勤労感謝の日,2017/11/23,勤労感謝の日,2018/11/23
      |天皇誕生日,2016/12/23,天皇誕生日,2017/12/23,天皇誕生日,2018/12/23
      |,,,,,
      |月日は表示するアプリケーションによって形式が異なる場合があります。,,,,,
    """.stripMargin

  val resRight = List(
    SyukujitsuBody("元日", LocalDate.of(2016, 1, 1)),
    SyukujitsuBody("成人の日", LocalDate.of(2016, 1, 11)),
    SyukujitsuBody("建国記念の日", LocalDate.of(2016, 2, 11)),
    SyukujitsuBody("春分の日", LocalDate.of(2016, 3, 20)),
    SyukujitsuBody("昭和の日", LocalDate.of(2016, 4, 29)),
    SyukujitsuBody("憲法記念日", LocalDate.of(2016, 5, 3)),
    SyukujitsuBody("みどりの日", LocalDate.of(2016, 5, 4)),
    SyukujitsuBody("こどもの日", LocalDate.of(2016, 5, 5)),
    SyukujitsuBody("海の日", LocalDate.of(2016, 7, 18)),
    SyukujitsuBody("山の日", LocalDate.of(2016, 8, 11)),
    SyukujitsuBody("敬老の日", LocalDate.of(2016, 9, 19)),
    SyukujitsuBody("秋分の日", LocalDate.of(2016, 9, 22)),
    SyukujitsuBody("体育の日", LocalDate.of(2016, 10, 10)),
    SyukujitsuBody("文化の日", LocalDate.of(2016, 11, 3)),
    SyukujitsuBody("勤労感謝の日", LocalDate.of(2016, 11, 23)),
    SyukujitsuBody("天皇誕生日", LocalDate.of(2016, 12, 23)),
    SyukujitsuBody("元日", LocalDate.of(2017, 1, 1)),
    SyukujitsuBody("成人の日", LocalDate.of(2017, 1, 9)),
    SyukujitsuBody("建国記念の日", LocalDate.of(2017, 2, 11)),
    SyukujitsuBody("春分の日", LocalDate.of(2017, 3, 20)),
    SyukujitsuBody("昭和の日", LocalDate.of(2017, 4, 29)),
    SyukujitsuBody("憲法記念日", LocalDate.of(2017, 5, 3)),
    SyukujitsuBody("みどりの日", LocalDate.of(2017, 5, 4)),
    SyukujitsuBody("こどもの日", LocalDate.of(2017, 5, 5)),
    SyukujitsuBody("海の日", LocalDate.of(2017, 7, 17)),
    SyukujitsuBody("山の日", LocalDate.of(2017, 8, 11)),
    SyukujitsuBody("敬老の日", LocalDate.of(2017, 9, 18)),
    SyukujitsuBody("秋分の日", LocalDate.of(2017, 9, 23)),
    SyukujitsuBody("体育の日", LocalDate.of(2017, 10, 9)),
    SyukujitsuBody("文化の日", LocalDate.of(2017, 11, 3)),
    SyukujitsuBody("勤労感謝の日", LocalDate.of(2017, 11, 23)),
    SyukujitsuBody("天皇誕生日", LocalDate.of(2017, 12, 23)),
    SyukujitsuBody("元日", LocalDate.of(2018, 1, 1)),
    SyukujitsuBody("成人の日", LocalDate.of(2018, 1, 8)),
    SyukujitsuBody("建国記念の日", LocalDate.of(2018, 2, 11)),
    SyukujitsuBody("春分の日", LocalDate.of(2018, 3, 21)),
    SyukujitsuBody("昭和の日", LocalDate.of(2018, 4, 29)),
    SyukujitsuBody("憲法記念日", LocalDate.of(2018, 5, 3)),
    SyukujitsuBody("みどりの日", LocalDate.of(2018, 5, 4)),
    SyukujitsuBody("こどもの日", LocalDate.of(2018, 5, 5)),
    SyukujitsuBody("海の日", LocalDate.of(2018, 7, 16)),
    SyukujitsuBody("山の日", LocalDate.of(2018, 8, 11)),
    SyukujitsuBody("敬老の日", LocalDate.of(2018, 9, 17)),
    SyukujitsuBody("秋分の日", LocalDate.of(2018, 9, 23)),
    SyukujitsuBody("体育の日", LocalDate.of(2018, 10, 8)),
    SyukujitsuBody("文化の日", LocalDate.of(2018, 11, 3)),
    SyukujitsuBody("勤労感謝の日", LocalDate.of(2018, 11, 23)),
    SyukujitsuBody("天皇誕生日", LocalDate.of(2018, 12, 23))
  )

  var testNG1 =
    """
      |平成28年（2016年）,,平成29年（2017年）,,平成30年（2018年）,
      |名称,月日,名称,月日,名称,月日
      |元日,2016/1/1,元日,2017/1/1,元日,2018/1/1,成人の日,9999
      |建国記念の日,2016/2/11,建国記念の日,2017/2/11,建国記念の日,2018/2/11
      |,,,,,
      |月日は表示するアプリケーションによって形式が異なる場合があります。,,,,,
    """.stripMargin

  var testNG2 =
    """
      |平成28年（2016年）,,平成29年（2017年）,,平成30年（2018年）,
      |名称,月日,名称,月日,名称,月日
      |元日,2016/1/1,元日,2017/1/1,元日,2018/1/1,成人の日,9999/99/9
      |建国記念の日,2016/2/11,建国記念の日,2017/2/11,建国記念の日,2018/2/11
      |,,,,,
      |月日は表示するアプリケーションによって形式が異なる場合があります。,,,,,
    """.stripMargin

  val resMap = SortedMap(
    2016 ->
      SortedMap(
        LocalDate.of(2016, 1, 1)   -> "元日",
        LocalDate.of(2016, 1, 11)  -> "成人の日",
        LocalDate.of(2016, 2, 11)  -> "建国記念の日",
        LocalDate.of(2016, 3, 20)  -> "春分の日",
        LocalDate.of(2016, 4, 29)  -> "昭和の日",
        LocalDate.of(2016, 5, 3)   -> "憲法記念日",
        LocalDate.of(2016, 5, 4)   -> "みどりの日",
        LocalDate.of(2016, 5, 5)   -> "こどもの日",
        LocalDate.of(2016, 7, 18)  -> "海の日",
        LocalDate.of(2016, 8, 11)  -> "山の日",
        LocalDate.of(2016, 9, 19)  -> "敬老の日",
        LocalDate.of(2016, 9, 22)  -> "秋分の日",
        LocalDate.of(2016, 10, 10) -> "体育の日",
        LocalDate.of(2016, 11, 3)  -> "文化の日",
        LocalDate.of(2016, 11, 23) -> "勤労感謝の日",
        LocalDate.of(2016, 12, 23) -> "天皇誕生日"
      ),
    2017 ->
      SortedMap(
        LocalDate.of(2017, 1, 1)   -> "元日",
        LocalDate.of(2017, 1, 9)   -> "成人の日",
        LocalDate.of(2017, 2, 11)  -> "建国記念の日",
        LocalDate.of(2017, 3, 20)  -> "春分の日",
        LocalDate.of(2017, 4, 29)  -> "昭和の日",
        LocalDate.of(2017, 5, 3)   -> "憲法記念日",
        LocalDate.of(2017, 5, 4)   -> "みどりの日",
        LocalDate.of(2017, 5, 5)   -> "こどもの日",
        LocalDate.of(2017, 7, 17)  -> "海の日",
        LocalDate.of(2017, 8, 11)  -> "山の日",
        LocalDate.of(2017, 9, 18)  -> "敬老の日",
        LocalDate.of(2017, 9, 23)  -> "秋分の日",
        LocalDate.of(2017, 10, 9)  -> "体育の日",
        LocalDate.of(2017, 11, 3)  -> "文化の日",
        LocalDate.of(2017, 11, 23) -> "勤労感謝の日",
        LocalDate.of(2017, 12, 23) -> "天皇誕生日"
      ),
    2018 ->
      SortedMap(
        LocalDate.of(2018, 1, 1)   -> "元日",
        LocalDate.of(2018, 1, 8)   -> "成人の日",
        LocalDate.of(2018, 2, 11)  -> "建国記念の日",
        LocalDate.of(2018, 3, 21)  -> "春分の日",
        LocalDate.of(2018, 4, 29)  -> "昭和の日",
        LocalDate.of(2018, 5, 3)   -> "憲法記念日",
        LocalDate.of(2018, 5, 4)   -> "みどりの日",
        LocalDate.of(2018, 5, 5)   -> "こどもの日",
        LocalDate.of(2018, 7, 16)  -> "海の日",
        LocalDate.of(2018, 8, 11)  -> "山の日",
        LocalDate.of(2018, 9, 17)  -> "敬老の日",
        LocalDate.of(2018, 9, 23)  -> "秋分の日",
        LocalDate.of(2018, 10, 8)  -> "体育の日",
        LocalDate.of(2018, 11, 3)  -> "文化の日",
        LocalDate.of(2018, 11, 23) -> "勤労感謝の日",
        LocalDate.of(2018, 12, 23) -> "天皇誕生日"
      )
  )

  describe("SykujitsuParserSpec") {
    it("parse Right") {
      val resultParse = SyukujitsuParser.parse(testOK)

      val res = resultParse match {
        case Right(result) => result
        case Left(msg)     => None
      }

      res should equal(resRight)
    }

    it("parse Failure") {
      val resultParse = SyukujitsuParser.parse(testNG1)

      val res = resultParse match {
        case Right(result) => result
        case Left(msg)     => msg
      }

      res should equal("string matching regex `.*,' expected but `9' found")
    }

    it("parse Exception") {

      a[java.time.format.DateTimeParseException] should be thrownBy {

        val resultParse = SyukujitsuParser.parse(testNG2)
        val res = resultParse match {
          case Right(result) => result
          case Left(msg)     => msg
        }
      }

    }

    it("convert to Map  by for ") {
      val resultParse = SyukujitsuParser.parse(testOK)
      val res = resultParse match {
        case Right(result) => SyukujitsuParser.convertSyukujitsuMap_for(result)
        case Left(msg)     => None
      }
      res should equal(resMap)
    }

    it("convert to Map  by foldLeft ") {
      val resultParse = SyukujitsuParser.parse(testOK)
      val res = resultParse match {
        case Right(result) => SyukujitsuParser.convertSyukujitsuMap_foldLeft(result)
        case Left(msg)     => None
      }
      res should equal(resMap)
    }

    it("convert to Map  by Recursive ") {
      val resultParse = SyukujitsuParser.parse(testOK)
      val res = resultParse match {
        case Right(result) => SyukujitsuParser.convertSyukujitsuMap_recursive(result)
        case Left(msg)     => None
      }
      res should equal(resMap)
    }

    it("convert to Map by builtin") {
      val resultParse = SyukujitsuParser.parse(testOK)
      val res: Option[Map[Int, Map[LocalDate, String]]] = resultParse match {
        case Right(result) => Some(SyukujitsuParser.convertSyukujitsuMap_builtin(result))
        case Left(msg)     => None
      }

      // println(ListMap(res.get.toSeq.sortBy(_._1):_*))
      // println(res)
      res.get should equal(resMap)
    }

  }

}
