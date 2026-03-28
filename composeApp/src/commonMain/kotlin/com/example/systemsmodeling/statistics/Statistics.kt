package com.example.systemsmodeling.statistics

object Statistics {
    fun mean(values: List<Double>): Double {
        return values.average()
    }

    fun meanSquare(values: List<Double>): Double {
        return values.map { it * it }.average()
    }
}
