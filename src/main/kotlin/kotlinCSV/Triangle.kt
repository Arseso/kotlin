package org.example

import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.math.pow

data class Triangle(
    val id: UUID,
    val sideA: Double,
    val sideB: Double,
    val sideC: Double,
    val registrationDateTime: LocalDateTime,
    val fillColor: Color,
    val borderColor: Color,
    val description: String,
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

fun Triangle.type() : TriangleType {

    val sides = sides()
    if (sides[2] > sides[0] + sides[1]){
        return TriangleType.некорректный
    }

    return if (sides[2] == sides[0] + sides[1]){
        TriangleType.отрезок
    }

    else if (sides[2].pow(2) < sides[0].pow(2) + sides[1].pow(2)){
        TriangleType.остроугольный
    }

    else if (sides[2].pow(2) == sides[0].pow(2) + sides[1].pow(2)){
        TriangleType.прямоугольный
    }

    else TriangleType.тупоугольный

}