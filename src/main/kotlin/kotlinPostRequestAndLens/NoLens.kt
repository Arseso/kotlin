package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import org.http4k.core.*
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun noLens() {
    val userList = mutableListOf(
        User("Reetle", "reetle@inbox.ru"),
        User("Gelios", "dvoretskiy4752@mail.ru")
    )

    val app = routes(
        "/user" bind POST to { request ->
            try {
                val formData = request.bodyString().parseFormData()
                val login = formData["login"] ?: ""
                val email = formData["email"] ?: ""

                when {
                    login.isEmpty() || email.isEmpty() -> {
                        val errorResponse = ErrorResponse(
                            Login = FieldError(
                                Value = login,
                                Error = if (login.isEmpty()) "String is empty" else ""
                            ),
                            Email = FieldError(
                                Value = email,
                                Error = if (email.isEmpty()) "String is empty" else ""
                            )
                        )
                        Response(BAD_REQUEST)
                            .body(ObjectMapper().writeValueAsString(errorResponse))
                            .header("Content-Type", "application/json")
                    }

                    userList.any { it.login == login } -> {
                        Response(CONFLICT).body("login is used")
                    }

                    else -> {
                        userList.add(User(login, email))
                        Response(CREATED).body("Пользователь $login successful created")
                    }
                }
            } catch (e: Exception) {
                Response(BAD_REQUEST).body("Incorrect data: ${e.message}")
            }
        }
    )

    app.asServer(Netty(9000)).start()
}

fun String.parseFormData(): Map<String, String> {
    return this.split("&")
        .map { it.split("=") }
        .associate { it[0] to (it.getOrNull(1) ?: "") }
}