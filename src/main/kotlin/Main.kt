import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import org.http4k.core.*
import org.http4k.routing.*
import org.http4k.server.Netty
import org.http4k.server.asServer
import java.io.File
import java.nio.file.DirectoryStream

fun main() {
    val reader = CsvReader()
    val file = File("some.csv")
    val fileJson = File("json.txt")
    val data = reader.readAllWithHeader(file = file)

    val objectMapper = ObjectMapper()
    val triangleList1: MutableList<Triangle> = readFromCSV(data)

    writeToJson(triangleList1, fileJson, objectMapper)
    val triangleList2 = JsonFileToList(fileJson, objectMapper)

    val app: HttpHandler = startServer(triangleList2)

    val server = app.asServer(Netty(9000)).start()

}

private fun startServer(triangleList: List<Triangle>): HttpHandler {
    val handler: HttpHandler = { request ->
        val id = request.query("id")
        Response(Status.OK).body("$id")
    }


//    val filter = Filter {handler -> {request ->
//        val id = request.query("id")?.toIntOrNull()
//        if (id == null) {Response.invoke(Status.OK).body("NT")}
//        else {
//            val triangle = triangleList.filter {
//                if (it.id == id) true
//                else false}
//            Response.invoke(Status.OK).body(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(triangle))
//        }
//
//    }}


    val app: HttpHandler = routes(
        "/" bind Method.GET to { Response(Status.OK).body("hello") },
        "/tris" bind Method.GET to handler,
        "/tri/{id}" bind Method.GET to //handler
                { request ->
            val id = request.path("id")?.toIntOrNull()
            if (id == null) {Response.invoke(Status.OK).body("NT")}
            else {
                val triangle = triangleList.filter {
                    if (it.id == id) true
                    else false}
                Response.invoke(Status.OK).body(ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(triangle))
            }
        },
    )//.withFilter(filter)
    return app
}

private fun JsonFileToList(fileJson: File, objectMapper: ObjectMapper) : List<Triangle> {
    val jsonString = fileJson.readText()
    val triangles: List<Triangle> = objectMapper.readValue(jsonString)
    return triangles
}

private fun readFromCSV(data: List<Map<String, String>>): MutableList<Triangle> {
    val triangleList: MutableList<Triangle> = mutableListOf()
    data.forEach { map ->
        triangleList.add(
            Triangle(
                map["Id"]?.toIntOrNull() ?: 0,
                map["SideA"]?.toIntOrNull() ?: 0,
                map["SideB"]?.toIntOrNull() ?: 0,
                map["SideC"]?.toIntOrNull() ?: 0,
                map["BorderColor"].toString(),
                map["FillColor"].toString(),
            )
        )
    }
    return triangleList
}

private fun writeToJson(
    triangleList: MutableList<Triangle>,
    fileJson: File,
    objectMapper: ObjectMapper
) {
    // Сериализуем весь список в JSON-массив
    val jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(triangleList)

    // Записываем JSON в файл
    fileJson.writeText(jsonString)
}

    data class Triangle(
        val id: Int = 0,
        val a: Int = 0,
        val b: Int = 0,
        val c: Int = 0,
        val borderColor: String = "",
        val fillColor: String = "",
    )