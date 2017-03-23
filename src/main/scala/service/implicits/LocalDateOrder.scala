package service.implicits

import java.time.LocalDate

object LocalDateOrderImplicits {
  implicit class LocalDateOrderImplicits(val ld: LocalDate)
      extends Ordered[LocalDate] {
    def compare(that: LocalDate): Int = ld.compareTo(that)
  }
}
