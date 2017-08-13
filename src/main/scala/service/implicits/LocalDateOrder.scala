package service.implicits

import java.text.SimpleDateFormat
import java.time.LocalDate

import org.json4s.DefaultFormats

object LocalDateOrderImplicits {
  implicit class LocalDateOrderImplicits(val ld: LocalDate) extends Ordered[LocalDate] {
    def compare(that: LocalDate): Int = ld.compareTo(that)
  }

}
