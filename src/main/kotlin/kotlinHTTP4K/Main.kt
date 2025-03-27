package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    //firstTask(args)

    val client = ApacheClient()
    val request = Request(Method.GET, Uri.of("https://lms.crafted.su/web-app-development/2024-2-it-3/docs/course/02-http-introduction/10-basic-http-server/triangles.json"))
    val response = client(request)
    val jsonString = response.body.toString()

    val triangleList: List<Triangle> = ObjectMapper().readValue(jsonString)
    val colorStats = triangleList.groupingBy { it.borderColor }.eachCount()
    println("Color stats:")
    colorStats.toSortedMap().forEach{ (key, value) ->
        println("$key : $value")
    }

    // Статистика по длине периметров треугольников
    val perimeterStats = mutableMapOf<Int, Int>()
    triangleList.forEach { triangle ->
        val range = (triangle.perimeter / 100).toInt()
        perimeterStats[range] = perimeterStats.getOrDefault(range, 0) + 1
    }

    println("Статистика по длине периметров:")
    perimeterStats.toSortedMap().forEach { (range, count) ->
        val start = range * 100 + 1
        val end = (range + 1) * 100
        println("* От $start до $end: $count")
    }

}

private fun firstTask(args: Array<String>) {
    if (args.size < 2) println("not enough arguments").also { exitProcess(1) }

    val pathToFile = args[0]
    val pathToSite = args[1]

    val file: File
    try {
        file = File(pathToFile)
        file.createNewFile()
    } catch (e: Exception) {
        println(e.message)
        exitProcess(2)
    }

    val client = ApacheClient()
    val request = Request(Method.GET, pathToSite)
    val response: Response
    try {
        response = client(request)
    } catch (e: Exception) {
        println(e.message)
        exitProcess(3)
    }

    val data: String
    if (response.status.code in 200..299) {
        data = response.body.toString()
        file.writeText(data)
    } else {
        println(response.status)
        println(response.headers)

    }
}

data class Triangle(
    val id: String = " ",
    val sideA: Double = -1.0,
    val sideB: Double = -1.0,
    val sideC: Double = -1.0,
    val borderColor: String = " ",
){
    val perimeter: Double
        get() = sideA + sideB + sideC
}
