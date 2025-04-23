package org.example

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonFactoryBuilder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileWriter
import java.util.UUID
import kotlin.system.exitProcess


fun main(args: Array<String>) {

    val triangleStorage = TriangleStorage(triangleCSVReader(File("./triangles.csv")))

    //lowLevelJSONFormater(triangleStorage.triangles)

    val filteredListTriangleByBorderColor = borderColorFilter(triangleStorage.triangles, Color.RED)
    val triangleBorderColorFilterClass = TriangleBorderColorFilterClass(Color.RED, filteredListTriangleByBorderColor)
    highLevelJSONFormater(triangleBorderColorFilterClass)

}

fun parseAppArgs(args: Array<String>){
    if (args.size < 2) exitProcess(1)
    if (!File(args[0]).exists()) exitProcess(2)
    try {
        Color.valueOf(args[1])
    } catch (e: Exception){
        println(e.message)
        exitProcess(3)
    }
}

fun lowLevelJSONFormater(triangleList: List<Triangle>) : File {
    val file = File("./lowLevelTriangleJOSN.json")
    if (!file.exists()) file.createNewFile()
    val fileWriter = FileWriter(file)

    val factory: JsonFactory = JsonFactoryBuilder().build()
    val outputGenerator: JsonGenerator = factory.createGenerator(fileWriter)
    outputGenerator.prettyPrinter = DefaultPrettyPrinter()

    with(outputGenerator){
        writeStartObject() // превая {
        writeArrayFieldStart("triangles") // "triangles" : [

        for (triangle in triangleList){
            writeStartObject()
            writeStringField("description", triangle.description)
            writeStringField("id", triangle.id.toString())
            writeStringField("registrationDateTime", triangle.registrationDateTime.toString())
            writeEndObject()
        }

        writeEndArray()
        writeEndObject()
        close()
    }
    return file
}

fun highLevelJSONFormater(triangleBorderColorFilterClass: TriangleBorderColorFilterClass) : File {
    val file = File("./highLevelTriangleJSON.json")
    if (!file.exists()) file.createNewFile()

    val jsonString = ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(triangleBorderColorFilterClass)
    file.writeText(jsonString)
    return file
}

data class TriangleBorderColorFilterClass(val borderColor: Color, val triangles: List<TriangleClassForJSON>)

data class TriangleClassForJSON(val id: UUID, val sideA: Double, val sideB: Double, val sideC: Double)

fun borderColorFilter(triangleList: List<Triangle>, color: Color) : List<TriangleClassForJSON> {
    val triangleFilteredList = triangleList.filter { it.borderColor == color }
    val triangleClassForJSONList: MutableList<TriangleClassForJSON> = mutableListOf()
    triangleFilteredList.forEach { triangleClassForJSONList.add(TriangleClassForJSON(it.id, it.sideA, it.sideB, it.sideC)) }
    return triangleClassForJSONList
}

fun fillColorFilter(triangleList: List<Triangle>, color: Color) : List<Triangle> {
    return triangleList.filter { it.fillColor == color }
}
