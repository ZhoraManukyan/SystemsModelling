package com.example.systemsmodeling.lab4

import kotlin.math.exp

class LogNormalGenerator {

    fun generate(mu: Double, sigma: Double, rValues: List<Double>): List<Double> {

        val result = mutableListOf<Double>()

        var i = 0
        while (i < rValues.size) {

            val group = rValues.drop(i).take(12)

            if (group.size < 12) break

            val S = group.sum()

            val x = exp(mu + sigma * (S - 6))

            result.add(x)

            i += 12
        }

        return result
    }
}
