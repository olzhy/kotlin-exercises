package com.example.demo

import com.example.demo.plugins.configureHTTP
import com.example.demo.plugins.configureRouting
import com.example.demo.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureRouting()
}