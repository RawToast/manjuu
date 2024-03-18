# Manjuu Server

![ci](https://github.com/rawtoast/http4s-twirl-example/actions/workflows/ci.yml/badge.svg)

This application is a simple backend example using Http4s, Redis, and Circe -- including some example unit tests. 
The initial goal was to rework my old [http4s-twirl-example](https://github.com/RawToast/http4s-twirl-example) 
to use Scala 3, as a backend for some frontend work.


## Running this app

To start: `sbt run` this will run the service on port 8080


## Running tests

An alias has been provided to run all tests with coverage: `sbt codeCoverage` the html report can be located
at `target/scala-3.4.0/scoverage-report/index.html`


## Tech Stack

This api is built on the following Scala stack:

* [Scala 3](https://www.scala-lang.org)
* [Http4s](http://http4s.org)
* [Circe](https://circe.github.io/circe)
* [Cats](http://typelevel.org/cats)
* [Cats Effect](http://typelevel.org/cats-effect)
* [Redis4Cats](http://redis4cats.profunktor.dev)
* [Munit](http://scalameta.org/munit)
