package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import org.http4k.client.ApacheClient
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.contentType
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer


fun withLens(){
    val userList: MutableList<User> = mutableListOf(
        User("Reetle", "reetle@inbox.ru"),
        User("Gelios", "dvoretskiy4752@mail.ru"),
        User("BombordiroCrocodilo", "arseso.main@gmail.com")
    )

    val loginField = FormField.optional("login")
    val emailField = FormField.optional("email")

    val formLens = Body.webForm(Validator.Feedback, loginField, emailField).toLens()

    val userPostHandler: HttpHandler = {request: Request ->
        val form = formLens.extract(request)

        if (form.errors.isNotEmpty()) {
            Response(BAD_REQUEST).body(form.errors.joinToString())
        }
        else{
            val login = loginField.extract(form)
            val email = emailField.extract(form)

            if (login == null || email == null || login.isEmpty() || email.isEmpty())
            {
                val errorResponse = ErrorResponse(
                    Login = FieldError(
                        Value = if (login == null) "" else login,
                        Error = if (login == null) "String is empty" else ""
                    ),
                    Email = FieldError(
                        Value = if (email == null) "" else email,
                        Error = if (email == null) "String is empty" else ""
                    )
                )

                Response(BAD_REQUEST).body(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse)).contentType(
                    ContentType.APPLICATION_JSON)
            }
            else if (checkUserLogin(login, userList) && checkUserEmail(email, userList)) {
                userList.add(User(login, email))
                Response(CREATED).body("User created with login: $login, email: $email")
            }
            else
                Response(CONFLICT).body("login or email already is used")
        }
    }

    val userGetHandler: HttpHandler = {
        Response(OK).body(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userList))
    }

    val app = routes(
        "/" bind GET to { Response(OK).body("It's a HomePage")},
        "/user" bind POST to userPostHandler,
        "/user" bind GET to userGetHandler
    )

    val server = app.asServer(Netty(9000)).start()

    val client = ApacheClient()
    val badRequest1 = Request(POST, Uri.of("http://localhost:9000/user")).body("login=&email=").contentType(
        ContentType.APPLICATION_FORM_URLENCODED)

    val badRequest2 = Request(POST, Uri.of("http://localhost:9000/user")).body("login=BombordiroCrocodilo&email=").contentType(
        ContentType.APPLICATION_FORM_URLENCODED)

    val badRequest3 = Request(POST, Uri.of("http://localhost:9000/user")).body("login=BombordiroCrocodilo&email=newemail@inbox.ru").contentType(
        ContentType.APPLICATION_FORM_URLENCODED)

    val goodRequest1 = Request(POST, Uri.of("http://localhost:9000/user")).body("login=Vovan228&email=vovan@mail.ru").contentType(
        ContentType.APPLICATION_FORM_URLENCODED)

//    print(client(badRequest3))
    print(client(goodRequest1))
}

fun checkUserLogin(login: String?, userList: MutableList<User>): Boolean {

    userList.forEach {
        if (it.login == login)
            return false
    }
    return true
}

fun checkUserEmail(email: String?, userList: MutableList<User>): Boolean {
    userList.forEach {
        if (it.email == email)
            return false
    }
    return true
}