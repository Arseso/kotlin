package org.example

import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) {

    parseAppArgs(args)
    val triangleList = triangleCSVReader(File(args[0]))
    val triangleStorage = TriangleStorage(triangleList)

    triangleStorage.triangleInfoWithFillColor(Color.valueOf(args[1]))


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

fun borderColorFilter(triangleList: List<Triangle>, color: Color) : List<Triangle> {
    return triangleList.filter { it.borderColor == color }
}
fun fillColorFilter(triangleList: List<Triangle>, color: Color) : List<Triangle> {
    return triangleList.filter { it.fillColor == color }
}
