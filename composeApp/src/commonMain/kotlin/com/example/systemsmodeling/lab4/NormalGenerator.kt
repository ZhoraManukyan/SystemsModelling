package com.example.systemsmodeling.lab4

import com.example.systemsmodeling.utils.round3Double

class NormalGenerator {
    fun generate(mu: Double, sigma: Double, rValues: List<Double>): List<Double> {

        val result = mutableListOf<Double>()

        var i = 0
        while (i < rValues.size) {

            val group = rValues.drop(i).take(12)
            print("group: $group\n")

            if (group.size < 12) break

            val s = group.sum().round3Double()
            print("s: $s\n")

            val x = (mu + sigma * (s - 6)).round3Double()

            result.add(x)
            print("x: $x\n")

            i += 12
        }

        return result
    }
}
