package controller

import scala.io.Source

import skinny.controller.feature.JSONFeature

import model.SyukujitsuBody
import service.SyukujitsuParser

class RootController extends ApplicationController with JSONFeature {

  def index = {
    getSyukujitsuList match {
      case Right(result) => set("lstResult", result) // parse success
      case Left(msg) => {
        // parse error
        set("lstResult", List())
        flash += ("msg" -> msg)
      }
    }
    render("/root/index")
  }

  def syukujitsu = params.getAs[Int]("y").map { y =>
    val opMap = getSyukujitsuList match {
      case Right(result) => SyukujitsuParser.convertYearMonthMap_reculsive(result).get(y)
      case Left(msg) => None
    }
    contentType = "application/json"
    toJSONString(opMap)
  }

  def getSyukujitsuList(): Either[String, List[SyukujitsuBody]] = {
    try {
      val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu.csv"), "MS932")
      //val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu_err.csv"), "MS932")
      val lit_text = file.mkString
      SyukujitsuParser.parse(lit_text)
    } catch {
      case e: Exception => Left("read file error")
    }
  }

}
