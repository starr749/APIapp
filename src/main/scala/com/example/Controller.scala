package com.example

/**
 * Created by Sidus on 3/29/2015.
 */

object Router {
  def route(method: String, uri:String, passData: Map[String, Any] = "") : (Map[String, Any]) = (method, uri) match {
    case("GET", "/all") => Controller.allData()
    case("GET", uri.startsWith("/company/")) => Controller.filterCompany(method, uri, passData)

  }
}

object Controller {
  val data = {
    Map("Blizzard" -> List(("Monday", 5.5), ("Tuesday", 6.0), ("Wednesday", 8.8), ("Thursday", 5.7), ("Friday", 7.7),
                           ("Monday", 4.5), ("Tuesday", 4.3), ("Wednesday", 3.4), ("Thursday", 4.8), ("Friday", 5.4),
                           ("Monday", 7.7), ("Tuesday", 8.0), ("Wednesday", 8.1), ("Thursday", 9.1), ("Friday", 3.4)),
        "Nintendo" -> List(("Monday", 4.5), ("Tuesday", 4.3), ("Wednesday", 3.4), ("Thursday", 4.8), ("Friday", 5.4)),
        "Sony" -> List(("Monday", 7.7), ("Tuesday", 8.0), ("Wednesday", 8.1)), ("Thursday", 9.1), ("Friday", 3.4))
  }

  def avg(numbers: List[String, Double]): Double = {
    var sum = 0
    numbers.foreach { elem =>
      sum = sum + elem._2
    }
  }

  def allData(): (Int, String) = {
    return (200, data.toString())
  }
  def filterCompany(method: String, uri: String, passData: Map[String, Any] = ""): (Map[String, Any]) = {
    val filteredData = if(passData == "") data else passData
    val company = uri.replace("/company/", "")
    val filter = Map(company -> data.get(company))
    return (filter)
  }
  def averageData(method:String, uri: String, passData: Map[String, List[(String, Double)]] = ""): (Map[String, Any]) =
    val avgData = data.foreach { case (key, value) => key -> value.
}
