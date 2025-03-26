package org.example

import kotlin.math.pow

class PolynomialCalculator(private val coefficients: List<Double>) : DoubleTransformer {
    override fun transform(number: Double): Double{
        var result = 0.0
        for (i in coefficients.indices) {
            result += coefficients[i] * number.pow(i)
        }
        return result
    }
}