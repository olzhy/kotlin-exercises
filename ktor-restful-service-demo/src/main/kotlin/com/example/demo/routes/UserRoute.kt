package com.example.demo.routes

import com.example.demo.conf.kodein
import com.example.demo.model.User
import com.example.demo.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
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

            val user = userService.getById(id) ?: return@get call.respondText(
                    "user not exists",
                    status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        // update
        patch {
            val user = call.receive<User>()
            userService.getById(user.id) ?: return@patch call.respondText(
                    "user not exists",
                    status = HttpStatusCode.NotFound
            )

            userService.update(user)
            call.respond(HttpStatusCode.NoContent)
        }

        // save
        post {
            val user = call.receive<User>()
            userService.getById(user.id)?.let {
                return@post call.respondText(
                        "user already exists",
                        status = HttpStatusCode.NotFound
                )
            }

            userService.save(user)
            call.respond(HttpStatusCode.Created)
        }

        // delete by id
        delete(Regex("/(?<id>\\d+)")) {
            val id = call.parameters["id"]!!.toLong()
            userService.getById(id) ?: return@delete call.respondText(
                    "user not exists",
                    status = HttpStatusCode.NotFound
            )

            userService.deleteById(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}