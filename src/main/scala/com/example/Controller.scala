package com.example

/**
 * Created by Sidus on 3/29/2015.
 */

// Defining my own Router here. Using some basic Pattern Matching to direct Get Requests
object Router {
  def route(method: String, uri:String) : (Int, String) = (method, uri) match {
    case("GET", "/all") => Controller.allData()
    case("GET", uri) if (uri.startsWith("/company/")) => Controller.filterCompany(method, uri)
    case("GET", uri) if (uri.contains("/avg")) => Controller.averageData(method, uri)
    case("GET", uri) if (uri.contains("/sum")) => Controller.sumData(method, uri)
    case("GET", uri) if (uri.contains("/groupavg")) => Controller.averageGroup(method, uri)
    case _ => (401, "No resource Found")
  }
}

// Here's the controller. Ideally of course this and the router would be Actors, but I didn't feel like I could
// learn Akka reliably in my time crunch
object Controller {

  // Defining the data object. Made to look like something out of a REDIS store or something similar.
  // I kind of wanted this to look kind of messy, because I wanted to see how scala does list comprehensions
  val data: Map[String, List[(String, Double)]] = {
    Map("Blizzard" -> List(("Monday", 5.5), ("Tuesday", 6.0), ("Wednesday", 8.8), ("Thursday", 5.7), ("Friday", 7.7),
                           ("Monday", 4.5), ("Tuesday", 4.3), ("Wednesday", 3.4), ("Thursday", 4.8), ("Friday", 5.4),
                           ("Monday", 7.7), ("Tuesday", 8.0), ("Wednesday", 8.1), ("Thursday", 9.1), ("Friday", 3.4)),
      "Nintendo" -> List(("Monday", 4.5), ("Tuesday", 4.3), ("Wednesday", 3.4), ("Thursday", 4.8), ("Friday", 5.4),
                         ("Monday", 7.7), ("Tuesday", 8.0), ("Wednesday", 8.1), ("Thursday", 9.1), ("Friday", 3.4)),
      "Activision" -> List(("January", 7.7), ("Febuary", 8.0), ("March", 8.1), ("April", 9.1), ("May", 3.4)))
  }

  // I decided I needed a function to calculate the sum of all the tuples
  def sum(numbers: List[(String, Double)]): Double = {
    val sumTuples = numbers.map(_._2).sum
    return sumTuples
  }

  // This groups the tuples by the first element, and calculates the average returning a Map of the result
  def groupByAndAvg[T, U](ts: Iterable[(T, U)])(implicit num: Numeric[U]) = {
    ts.groupBy(_._1).map {
      case (key, pairs) =>
        val values = pairs.map(_._2)
        key -> (num.toDouble(values.sum) / values.size)
    }
  }

  // Decided I wanted to seperate this into a function, Iterate over the data and calls that groupByAndAvg function
  def group_avg(data_list: Map[String, List[(String, Double)]]): Map[String, Map[String, Double]] = {
    return ( for ((key, value) <- data_list) yield key -> groupByAndAvg(value))
  }

  // Sa,e thing, this iterates over the map returning the average
  def calc_avg(data_list: Map[String, List[(String, Double)]]): Map[String, Double] = {
    return (for ((key, value) <- data_list) yield key -> (sum(value) / value.length))
  }

  // Return the complete Data object
  def allData(): (Int, String) = {
    return (200, data.toString())
  }

  // Return data object with averaged data over the the "unit of time"
  def averageGroup(method:String, uri:String): (Int, String) = {
    val avgData = group_avg(data)

    return (200, avgData.toString)
  }

  // Filter Data by Company. Originally I wanted to chain this method with others,
  // I planned on using aonymous functions to acconplish this, just ran out of time
  def filterCompany(method: String, uri: String): (Int, String) = {
    val company = uri.replace("/company/", "")
    val filter = Map(company -> data.get(company))
    return (200, filter.toString())
  }

  // This returns averaged data
  def averageData(method: String, uri: String): (Int, String) = {
    val avgData:Map[String, Double] = calc_avg(data)

    return (200, avgData.toString)
  }
  // iterates, returns sum of every 'company'
  def sumData(method:String, uri: String): (Int, String) = {
    val sumData:Map[String, Double] = (for ((key, value) <- data) yield key -> (sum(value) ))

    return (200, sumData.toString)
  }
}
