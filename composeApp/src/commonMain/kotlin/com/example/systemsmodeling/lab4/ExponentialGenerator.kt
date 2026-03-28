package com.example.systemsmodeling.lab4

import kotlin.math.ln

class ExponentialGenerator {

    fun generate(lambda: Double, values: List<Double>): List<Double> {
        return values.map { r ->
            (-1.0 / lambda) * ln(r)
        }
    }
}
