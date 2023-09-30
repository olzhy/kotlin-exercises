package com.example.demo

import com.example.demo.plugin.configureRouting
import com.example.demo.plugin.configureSerialization
import com.example.demo.plugin.configureSwagger
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureSwagger()
}