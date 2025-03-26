package org.example

import kotlin.math.pow

fun main() {
//    val expression: (Int) -> Unit = { x ->
//        if (x % 2 == 0) println("четное")
//        else println("нечетное")
//    }
//    callTwice(expression)
//
//    val functionType: DoubleTransformer = object : DoubleTransformer {
//        override fun transform(number: Double): Double {
//            return number.pow(2)
//        }
//    }
//    callAndPrint(functionType)
//
//    callAndPrint(createMultiplier(10.0))
//    callAndPrint(createMultiplier(7.0))

    val polynomial = PolynomialCalculator(listOf(7.0, -5.0, 2.0))
    callAndPrint(polynomial)

}

fun callTwice(expression: (Int) -> Unit) {
    expression(4)
    expression(41)
}

fun callAndPrint(transformer: DoubleTransformer) {
    println(transformer.transform(25.0))
    println(transformer.transform(16.0))
    println(transformer.transform(157.0))
}

fun createMultiplier(coefficient: Double): DoubleTransformer {
    return object : DoubleTransformer {
        override fun transform(number: Double): Double {
            return number * coefficient
        }
    }
}
