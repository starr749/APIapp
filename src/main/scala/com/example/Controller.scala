package com.example

import spray.json._
import DefaultJsonProtocol._

/**
 * Created by Sidus on 3/29/2015.
 */

// Defining my own Router here. Using some basic Pattern Matching to direct Get Requests
object Router {

  // This is a map where we can store our routes
  // In a framework, we'd load this from like a 'routes' file like in Rails of Django
  // I'm storing it in a Map here because I think it shows how the pattern works better
  val routes = Map(
  "/all"      -> Controller.allData _,
  "/company"  -> Controller.filterCompany _,
  "/avg"      -> Controller.averageData _,
  "/sum"      -> Controller.sumData _,
  "/groupavg" -> Controller.averageGroup _
  )

  // The routes Map make the router object pretty small, and easy to understand
  def route(method: String, uri:String) : (Int, String) = (method, uri) match {
    case("GET", uri) if (routes.keySet.exists(_ == uri.toString)) => routes.get(uri).get(method, uri)
    case _ => (401, "Resource Not Found")
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
  def allData(method:String, uri:String): (Int, String) = {
    return (200, data.toJson.prettyPrint)
  }

  // Return data object with averaged data over the the "unit of time"
  def averageGroup(method:String, uri:String): (Int, String) = {
    val avgData = group_avg(data)

    return (200, avgData.toJson.prettyPrint)
  }

  // Filter Data by Company. Originally I wanted to chain this method with others,
  // I planned on using aonymous functions to acconplish this, just ran out of time
  def filterCompany(method: String, uri: String): (Int, String) = {
    val company = uri.replace("/company/", "")
    val filter = Map(company -> data.get(company))
    return (200, filter.toJson.prettyPrint)
  }

  // This returns averaged data
  def averageData(method: String, uri: String): (Int, String) = {
    val avgData:Map[String, Double] = calc_avg(data)

    return (200, avgData.toJson.prettyPrint)
  }
  // iterates, returns sum of every 'company'
  def sumData(method:String, uri: String): (Int, String) = {
    val sumData:Map[String, Double] = (for ((key, value) <- data) yield key -> (sum(value) ))

    return (200, sumData.toJson.prettyPrint)
  }
}
