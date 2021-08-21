package com.giuseppe.pinto.blaze.bank.reader.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    println("ciao")
    runApplication<Application>(*args)
}