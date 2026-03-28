package com.example.systemsmodeling.lab4

import kotlin.math.ln

class ProductMethodGenerator {

    fun generate(alpha: Double, rValues: List<Double>, k: Int): List<Double> {

        val result = mutableListOf<Double>()

        var i = 0
        var s: Double
        while (i < rValues.size) {

            val group = rValues.drop(i).take(k)

            if (group.size < k) break

            s = group.reduce { acc, v -> acc * v }

            val x = (-1.0 / alpha) * ln(s)

            result.add(x)

            i += k
        }

        return result
    }
}
