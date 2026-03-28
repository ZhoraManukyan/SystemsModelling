package com.example.systemsmodeling.lab5

import com.example.systemsmodeling.lab5.models.Lab5Item

class BinomialGenerator {
    fun generate(
        p: Double,
        n: Int,
        realizations: Int,
        rValues: List<Double>
    ): List<Lab5Item> {
        val result = mutableListOf<Lab5Item>()
        var index = 0

        repeat(realizations) { i ->

            val group = rValues.subList(index, index + n)
            index += n

            var x = 0

            group.forEach {
                if (it <= p) x++
            }

            result.add(
                Lab5Item(
                    index = i + 1,
                    rValues = group,
                    x = x
                )
            )
        }

        return result
    }
}
