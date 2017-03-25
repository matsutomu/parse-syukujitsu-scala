package model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.json4s.CustomKeySerializer

import scala.collection.SortedMap

case class SyukujitsuBody(date_name: String, date: LocalDate)

/*
 *  for JSON
 */
case class JSONResponse(success: Boolean, msg: String, syukujitsu: Option[SortedMap[LocalDate, String]])
object LocalDateKeyJSonSerializer extends CustomKeySerializer[LocalDate](format => (
  {
    case s: String => {
      LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy/M/d"))
    }
  },
  {
    case x: LocalDate => {
      val obj = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")
      obj.format(x)
      x.toString
    }
  }
))

