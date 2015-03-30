package com.example

import java.net.URI
import java.sql.{Connection, DriverManager}

import com.twitter.finagle.http.Response
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http._

import scala.util.Properties

// Look an http server!
object Server {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port: "+port)

    val server = Http.serve(":" + port, new Hello)
    Await.ready(server)
  }
}

// Our Service, thank you heroku for the boilerplate
class Hello extends Service[HttpRequest, HttpResponse] {

  // If it's a home page request showHome, otherwise send it to our router
  def apply(request: HttpRequest): Future[HttpResponse] = {
    if (request.getUri.endsWith("/")) {
      showHome(request)
    } else {
      processRequest(request);
    }
  }

  // Displays the homepage with valid api calls
  def showHome(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(200)
    response.setContentString("API Home.\n\n Possible API Calls: \n\n/all     (Returns all of the Data)\n/sum     (Returns the Sum of all of each \"Company\")\n/avg     (Returns the Average of all of each \"Company\")\n/groupavg (Returns the Average of each \"Company\"'s \"Unit Of Time\") ")
    Future(response)
  }

  // forwards request to the router, and processes the response
  def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    val (resCode, data) = Router.route(request.getMethod.toString, request.getUri)
    response.setStatusCode(resCode)
    response.setContentString(data)
    Future(response)
  }
}