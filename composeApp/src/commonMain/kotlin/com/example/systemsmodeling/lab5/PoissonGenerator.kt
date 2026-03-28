package com.example.systemsmodeling.lab5

import com.example.systemsmodeling.lab5.models.Lab5Item
import kotlin.math.exp

class PoissonGenerator {
    fun generate(
        lambda: Double,
        rValues: List<Double>,
        count: Int
    ): List<Lab5Item> {

        val result = mutableListOf<Lab5Item>()

        var index = 0

        for (i in 0 until count) {

            val rList = mutableListOf<Double>()

            var S = 1.0
            var j = 0

            while (S > exp(-lambda)) {

                val r = rValues[index++]
                rList.add(r)

                S *= r
                j++
            }

            val x = j - 1

            result.add(
                Lab5Item(
                    index = i + 1,
                    rValues = rList,
                    x = x
                )
            )
        }

        return result
    }
}
