package org.example

import kotlin.system.exitProcess


fun main(args: Array<String>) {

    val triangle1 = Triangle(3.0, 4.0, 5.0, Color.Зеленый, Color.Ораньжевый)
    val triangle2 = Triangle(3.0, 2.0, 5.0, Color.Синий, Color.Черный)
    val triangle3 = Triangle(1.0, 2.0, 5.0, Color.Зеленый, Color.Красный)

    val triangleList = listOf(triangle1, triangle2, triangle3)

    triangleList.forEach {
        println(it.type())
        println(it.fillColor)
        println(it.borderColor)
        println()
    }

    val filteredBorderList = borderColorFilter(triangleList, Color.Черный)
    val filteredFillList = fillColorFilter(triangleList, Color.Зеленый)

    filteredFillList.forEach { println(it) }
    filteredBorderList.forEach { println(it) }

}

fun borderColorFilter(triangleList: List<Triangle>, color: Color) : List<Triangle> {
    return triangleList.filter { it.borderColor == color }
}
fun fillColorFilter(triangleList: List<Triangle>, color: Color) : List<Triangle> {
    return triangleList.filter { it.fillColor == color }
}

//fun parseAppArgs(args: Array<String>) : List<Double> {
//    if (args.size % 3 != 0){
//        println("args.size % 3 != 0")
//        exitProcess(3)
//    }
//
//    val list: MutableList<Double> = mutableListOf()
//
//    try{
//        for (i in args){
//            list.add(i.toDouble())
//        }
//    } catch (e: Exception){
//        println(e.message)
//        exitProcess(1)
//    }
//
//    return list
//}

//fun createListOfTriangles(list: List<Double>) : List<Triangle> {
//    val triangleList: MutableList<Triangle> = mutableListOf()
//    val k = list.size / 3
//
//    for (i in 1..k){
//        triangleList.add(Triangle(list[i*3 - 3], list[i*3 - 2], list[i*3 - 1]))
//    }
//
//    return triangleList
//}

