package org.example

import kotlin.system.exitProcess


fun main(args: Array<String>) {

    print(args.size)
    val list = parseAppArgs(args)
    val triangleList = createListOfTriangles(list)

    triangleList.forEach {
        println(it.area)
        println(it.perimeter)
        println(it.maxSide)
        println(it.toString())
        println(it.type())
    }
}

fun parseAppArgs(args: Array<String>) : List<Double> {
    if (args.size % 3 != 0){
        println("args.size % 3 != 0")
        exitProcess(3)
    }

    val list: MutableList<Double> = mutableListOf()

    try{
        for (i in args){
            list.add(i.toDouble())
        }
    } catch (e: Exception){
        println(e.message)
        exitProcess(1)
    }

    return list
}


fun createListOfTriangles(list: List<Double>) : List<Triangle> {
    val triangleList: MutableList<Triangle> = mutableListOf()
    val k = list.size / 3

    for (i in 1..k){
        triangleList.add(Triangle(list[i*3 - 3], list[i*3 - 2], list[i*3 - 1]))
    }

    return triangleList
}

