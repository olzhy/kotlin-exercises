package com.example.demo.code

import com.example.demo.model.ErrorResponse
import io.ktor.http.*

enum class ErrorCodes(val status: HttpStatusCode, private val code: String, private val description: String) {
    USER_NOT_FOUND(HttpStatusCode.NotFound, "user_not_found", "user not found"),
    USER_ALREADY_EXISTS(HttpStatusCode.BadRequest, "user_already_exists", "user already exists");

    fun toErrorResponse(): ErrorResponse = ErrorResponse(code, description)
}