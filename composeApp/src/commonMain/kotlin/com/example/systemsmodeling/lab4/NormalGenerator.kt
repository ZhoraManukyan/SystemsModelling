package com.example.systemsmodeling.lab4

import com.example.systemsmodeling.utils.round3Double

class NormalGenerator {
    fun generate(mu: Double, sigma: Double, rValues: List<Double>): List<Double> {

        val result = mutableListOf<Double>()

        var i = 0
        while (i < rValues.size) {

            val group = rValues.drop(i).take(12)

            if (group.size < 12) break

            val s = group.sum().round3Double()

            val x = (mu + sigma * (s - 6)).round3Double()

            result.add(x)

            i += 12
        }

        return result
    }
}
