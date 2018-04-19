package com.rjs.myshows.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyshowsServerApplication

fun main(args: Array<String>) {
    runApplication<MyshowsServerApplication>(*args)
}
