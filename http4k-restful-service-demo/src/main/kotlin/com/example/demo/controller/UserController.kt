package com.example.demo.controller

import com.example.demo.code.ErrorCodes
import com.example.demo.model.User
import com.example.demo.service.UserService
import jakarta.inject.Inject
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.lens.long

class UserController @Inject constructor(
        private val userService: UserService
) {
    companion object {
        private val usersLens = Body.auto<List<User>>().toLens()
        private val userLens = Body.auto<User>().toLens()
    }

    val routes: List<ContractRoute> = listOf(
            // listAll
            "/users" meta {
                summary = "list all users"
                returning(OK, usersLens to listOf(User(1, "Larry", 28)))
            } bindContract GET to ::listAll,

            // getById
            "/users" / Path.long().of("id") meta {
                summary = "get user by id"
                returning(OK, userLens to User(1, "Larry", 28))
                returning(ErrorCodes.USER_NOT_FOUND.status, ErrorCodes.USER_NOT_FOUND.toSampleResponse())
            } bindContract GET to { id -> { req -> getById(req, id) } },

            // update
            "/users" meta {
                summary = "update user"
                receiving(userLens to User(1, "Larry", 28))
                returning(NO_CONTENT)
                returning(ErrorCodes.USER_NOT_FOUND.status, ErrorCodes.USER_NOT_FOUND.toSampleResponse())
            } bindContract PATCH to { req -> update(req, userLens(req)) },

            // save
            "/users" meta {
                summary = "save user"
                receiving(userLens to User(1, "Larry", 28))
                returning(CREATED)
                returning(ErrorCodes.USER_ALREADY_EXISTS.status, ErrorCodes.USER_ALREADY_EXISTS.toSampleResponse())
            } bindContract POST to { req -> save(req, userLens(req)) },

            // deleteById
            "/users" / Path.long().of("id") meta {
                summary = "delete user by id"
                returning(NO_CONTENT)
                returning(ErrorCodes.USER_NOT_FOUND.status, ErrorCodes.USER_NOT_FOUND.toSampleResponse())
            } bindContract DELETE to { id -> { req -> deleteById(req, id) } }
    )

    private fun listAll(req: Request): Response {
        val users = userService.listAll()
        return Response(OK).with(usersLens of users)
    }

    private fun getById(req: Request, id: Long): Response {
        val user = userService.getById(id)
        return user?.let {
            Response(OK).with(userLens of it)
        } ?: ErrorCodes.USER_NOT_FOUND.toResponse()
    }

    private fun update(req: Request, user: User): Response {
        // exists?
        userService.getById(user.id) ?: return ErrorCodes.USER_NOT_FOUND.toResponse()

        // update
        userService.update(user)
        return Response(NO_CONTENT)
    }

    private fun save(req: Request, user: User): Response {
        // exists?
        val userStored = userService.getById(user.id)
        if (null != userStored) {
            return ErrorCodes.USER_ALREADY_EXISTS.toResponse()
        }

        // save
        userService.save(user)
        return Response(CREATED)
    }

    private fun deleteById(req: Request, id: Long): Response {
        // exists?
        userService.getById(id) ?: return ErrorCodes.USER_NOT_FOUND.toResponse()

        // delete
        userService.deleteById(id)
        return Response(NO_CONTENT)
    }
}