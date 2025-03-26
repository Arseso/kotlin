package org.example

import kotlin.math.max
import kotlin.math.sqrt
import kotlin.math.pow

class Triangle(
    private val sideA: Double,
    private val sideB: Double,
    private val sideC: Double,
    ) {


    val maxSide: Double
        get() = max(max(sideA, sideB), sideC)

    val perimeter: Double
        get() = sideA + sideB + sideC

    val area: Double
        get() {
            val p = (sideA + sideB + sideC) / 2
            return sqrt(p*(p-sideA)*(p-sideB)*(p-sideC))}

    override fun toString(): String {
        val list = listOf(sideA, sideB, sideC).sortedDescending()
        return "${list[0]} ${list[1]} ${list[2]}"
    }

    fun sides() : List<Double> {
        val list: List<Double> = listOf(sideA, sideB, sideC)
        return list.sorted()
    }

}

fun Triangle.type() : String {

    val sides = sides()
    if (sides[2] > sides[0] + sides[1]){
        return "некорректный"
    }

    return if (sides[2] == sides[0] + sides[1]){
        "отрезок"
    }

    else if (sides[2].pow(2) < sides[0].pow(2) + sides[1].pow(2)){
        "остроугольный"
    }

    else if (sides[2].pow(2) == sides[0].pow(2) + sides[1].pow(2)){
        "прямоугольный"
    }

    else "тупоугольный"

}