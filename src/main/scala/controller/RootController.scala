package controller

import service.SyukujitsuParser
import skinny._

import scala.io.Source

class RootController extends ApplicationController {

  def index = {
    val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu.csv"), "MS932")
    //val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu_err.csv"), "MS932")
    val lit_text = file.mkString
    SyukujitsuParser.parse(lit_text) match {
      case Right(result) => set("lstResult", result) // parse success
      case Left(msg) => { // parse error
        set("lstResult", List())
        flash += ("msg" -> msg)
      }
    }

    render("/root/index")
  }

}
