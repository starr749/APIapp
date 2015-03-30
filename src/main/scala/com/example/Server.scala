package com.example

import java.net.URI
import java.sql.{Connection, DriverManager}

import com.twitter.finagle.http.Response
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, Future}
import org.jboss.netty.handler.codec.http._

import scala.util.Properties

object Server {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    println("Starting on port: "+port)

    val server = Http.serve(":" + port, new Hello)
    Await.ready(server)
  }
}

class Hello extends Service[HttpRequest, HttpResponse] {
  def apply(request: HttpRequest): Future[HttpResponse] = {
    if (request.getUri.endsWith("/")) {
      showHome(request)
    } else {
      processRequest(request);
    }
  }

  def showHome(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    response.setStatusCode(200)
    response.setContentString("Hello from Scala!")
    Future(response)
  }

  def processRequest(request: HttpRequest): Future[HttpResponse] = {
    val response = Response()
    val (resCode, data) = Router.route(request.getMethod.toString, request.getUri)
    response.setStatusCode(resCode)
    response.setContentString(data)
    Future(response)
  }
}