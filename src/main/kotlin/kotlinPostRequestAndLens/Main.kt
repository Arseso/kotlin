package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.core.Method.*
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.auto
import org.http4k.lens.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer



data class User(val login: String, val email: String)
data class FieldError(val Value: String, val Error: String)
data class ErrorResponse(val Login: FieldError, val Email: FieldError)

fun main() {
    withLens()
    //noLens()
}