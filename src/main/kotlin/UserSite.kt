import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import java.io.File

data class User(val login: String, val email: String)

data class Error(val errorMessage: String)
data class Success(val message: String)
data class DelUser(val login: String)

fun makeJson(obj: Any): String {
    val jsonString = jacksonObjectMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(obj)

    if (jsonString == null){
        return jacksonObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(Error(
                "something went wrong"
            ))
    }
    return jsonString
}

fun getDataFromCSV(): MutableList<User>{
    val users: MutableList<User> = mutableListOf()

    csvReader().readAllWithHeader(File("users.csv")).forEach { it ->
        users.add(User(
            it["login"] ?: "undefined",
                it["email"] ?: "undefined",
            )
        )
    }
    return users
}
fun parseQueryString(query: String): Map<String, String> {
    return query.split("&")
        .associate { param ->
            val (name, value) = param.split("=", limit = 2)
            name to (value.ifEmpty { "" })
        }
}

fun main(){

    val users: MutableList<User> = getDataFromCSV()
    println(users.size)
    val router = routes(
        "/" bind Method.GET to {
            Response(Status.OK).body(makeJson(users))
        },
        "/id/{id}" bind Method.GET to {
            request ->
            val id = request.path("id")?.toIntOrNull() ?: -1
            if(id == -1){
                Response(Status.BAD_REQUEST).body(
                    makeJson(
                        Error("why are you so dumb, fucking idiot?")
                    )
                )
            } else {
                if (users.getOrNull(id) == null) {
                    Response(Status.NOT_FOUND).body(makeJson(Error("not found")))
                } else {
                    Response(Status.OK).body(makeJson(users[id]))
                }
            }
        },
        "/addUser" bind Method.POST to {
            request ->

            try {
                val userRegistrationLens = Body.auto<User>().toLens()
                val user = userRegistrationLens.extract(request)
                users.add(user)
                Response(Status.OK).body(makeJson(Success("Item successfully added")))

            } catch (e: Exception){
                Response(Status.BAD_REQUEST).body(
                    makeJson(
                        Error("why are you so dumb, fucking idiot?")
                    )
                )
            }
        },
        "/delUser" bind Method.POST to { request ->
            try {
                val userRegistrationLens = Body.auto<DelUser>().toLens()
                val user = userRegistrationLens.extract(request)
                println(users.remove(users.find { it.login == user.login }))
                Response(Status.OK).body(makeJson(Success("Item successfully deleted")))

            } catch (e: Exception) {
                Response(Status.BAD_REQUEST).body(
                    makeJson(
                        Error("why are you so dumb, fucking idiot?")
                    )
                )
            }
        },
        "/delById" bind Method.POST to {
            request ->
            try {
                val form = parseQueryString(request.bodyString())
                val id = form["id"]?.toIntOrNull() ?: -1
                if(id == -1) Response(Status.BAD_REQUEST).body(
                    makeJson(
                        Error("why are you so dumb, fucking idiot?")
                    )
                )
                else {
                    users.removeAt(id)
                    Response(Status.OK).body(makeJson(Success("Item successfully deleted")))
                }


            } catch (e: Exception){
                Response(Status.BAD_REQUEST).body(
                    makeJson(
                        Error("Element not found")
                    )
                )
            }

        }
    ).asServer(Netty(9000))
    router.start()
}

