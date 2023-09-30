package com.example.demo.route

import com.example.demo.code.ErrorCodes
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

            val user = userService.getById(id) ?: return@get call.respond(
                    ErrorCodes.USER_NOT_FOUND.status,
                    ErrorCodes.USER_NOT_FOUND.toErrorResponse()
            )
            call.respond(user)
        }

        // update
        patch {
            val user = call.receive<User>()
            userService.getById(user.id) ?: return@patch call.respond(
                    ErrorCodes.USER_NOT_FOUND.status,
                    ErrorCodes.USER_NOT_FOUND.toErrorResponse()
            )

            userService.update(user)
            call.respond(HttpStatusCode.NoContent)
        }

        // save
        post {
            val user = call.receive<User>()
            userService.getById(user.id)?.let {
                return@post call.respond(
                        ErrorCodes.USER_ALREADY_EXISTS.status,
                        ErrorCodes.USER_ALREADY_EXISTS.toErrorResponse()
                )
            }

            userService.save(user)
            call.respond(HttpStatusCode.Created)
        }

        // delete by id
        delete(Regex("/(?<id>\\d+)")) {
            val id = call.parameters["id"]!!.toLong()
            userService.getById(id) ?: return@delete call.respond(
                    ErrorCodes.USER_NOT_FOUND.status,
                    ErrorCodes.USER_NOT_FOUND.toErrorResponse()
            )

            userService.deleteById(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}