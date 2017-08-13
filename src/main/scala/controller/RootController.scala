package controller

import scala.io.Source
import org.json4s._
import skinny.controller.feature.JSONFeature

import model._
import service.SyukujitsuParser

class RootController extends ApplicationController with JSONFeature {

  /*
   * from local file , to List
   */
  private[this] def getSyukujitsuList(): Either[String, List[SyukujitsuBody]] = {
    try {
      val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu.csv"), "MS932")
      //val file = Source.fromFile(servletContext.getRealPath("/assets/csv/syukujitsu_err.csv"), "MS932")
      val lit_text = file.mkString
      SyukujitsuParser.parse(lit_text)
    } catch {
      case e: Exception => Left("read file error")
    }
  }

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

  /*
   * JSON response
   */
  implicit override val jsonFormats: Formats = DefaultFormats + LocalDateKeyJSonSerializer
  def syukujitsu = params.getAs[Int]("y").map { y =>
    contentType = "application/json"
    getSyukujitsuList match {
      case Right(result) => {
        SyukujitsuParser.convertSyukujitsuMap_recursive(result).get(y) match {
          case Some(v) => toJSONString(JSONResponse(true, "", Some(v)))
          case None    => toJSONString(JSONResponse(false, "no data", None))
        }
      }
      case Left(msg) => toJSONString(JSONResponse(false, msg, None))
    }

  }

}
