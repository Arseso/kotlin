package org.example

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID

class TriangleStorage(val triangles: List<Triangle>) {

}

fun TriangleStorage.triangleInfoWithFillColor(color: Color){
    triangles.forEach {
        if (it.fillColor == color){
            println(it.id)
            println("${it.sideA} ${it.sideB} ${it.sideC}")
        }

    }
}

fun triangleCSVReader(file: File) : List<Triangle> {
    val reader = CsvReader()
    val data = reader.readAllWithHeader(file)
    val triangleList: MutableList<Triangle> = mutableListOf()

    data.forEach { map ->
        triangleList.add(Triangle(
            UUID.fromString(map["Id"]),
            map["SideA"]!!.toDouble(),
            map["SideB"]!!.toDouble(),
            map["SideC"]!!.toDouble(),
            LocalDateTime.parse(map["RegistrationDateTime"].toString(), ISO_LOCAL_DATE_TIME),
            Color.valueOf(map["FillColor"]!!.uppercase()),
            Color.valueOf(map["BorderColor"]!!.uppercase()),
            map["Description"].toString()
        ))
    }
    return triangleList
}