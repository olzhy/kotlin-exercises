package com.example.demo.code

import com.example.demo.model.ErrorResponse
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.BiDiBodyLens

enum class ErrorCodes(val status: Status, private val code: String, private val description: String) {
    USER_NOT_FOUND(Status.NOT_FOUND, "user_not_found", "user not found"),
    USER_ALREADY_EXISTS(Status.BAD_REQUEST, "user_already_exists", "user already exists");

    fun toResponse(): Response =
            Response(status).with(Body.auto<ErrorResponse>().toLens() of ErrorResponse(code, description))

    fun toSampleResponse(): Pair<BiDiBodyLens<ErrorResponse>, ErrorResponse> =
            Body.auto<ErrorResponse>().toLens() to ErrorResponse(code, description)
}