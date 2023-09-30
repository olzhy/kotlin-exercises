package com.example.demo.routes

import com.example.demo.conf.kodein
import com.example.demo.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance

fun Route.userRouting() {
    val userService: UserService by kodein.instance()

    route("/users") {
        // list all
        get {
            val users = userService.listAll()
            call.respond(users)
        }

        // get user by id
        get(Regex("/(?<id>\\d+)")) {
            val id = call.parameters["id"]!!.toLong()

            val user = userService.getById(id) ?: call.respondText(
                    "user not exists",
                    status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        // update
        patch {

        }

        // save
        post {

        }

        // delete by id
        delete(Regex("/(?<id>\\d+)")) {

        }
    }
}