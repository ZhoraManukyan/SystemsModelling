package com.example.systemsmodeling.lab5

import com.example.systemsmodeling.lab2.Lab2UseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DiscreteDistributionsTest {

    @Test
    fun binomialCountsValuesNotGreaterThanP() {
        // From the app: p = 0.45, N = 4
        val r = listOf(0.19, 0.683, 0.889, 0.746, 0.063, 0.302, 0.762, 0.365)
        val result = BinomialGenerator().generate(p = 0.45, n = 4, realizations = 2, rValues = r)

        assertEquals(listOf(1, 3), result.map { it.x })
        assertEquals(listOf(0.19, 0.683, 0.889, 0.746), result[0].rValues)
    }

    @Test
    fun binomialValueIsBetweenZeroAndN() {
        val result = Lab5UseCase(Lab2UseCase()).generateBinomial(
            p = 0.45, n = 4, realizations = 50, l = 19, mu = 7, m = 64, seed = 7
        )

        assertEquals(50, result.size)
        assertTrue(result.all { it.x in 0..4 })
    }

    @Test
    fun poissonStopsWhenProductDropsBelowExpMinusLambda() {
        // lambda = 1 -> threshold e^-1 ~ 0.368
        // 0.5 -> S = 0.5; 0.5 -> S = 0.25 (stop) -> 2 values -> x = 1
        // 0.2 -> S = 0.2 (stop) -> 1 value -> x = 0
        val result = PoissonGenerator().generate(lambda = 1.0, rValues = listOf(0.5, 0.5, 0.2), count = 2)

        assertEquals(listOf(1, 0), result.map { it.x })
        assertEquals(listOf(0.5, 0.5), result[0].rValues)
    }

    @Test
    fun poissonValuesAreNonNegative() {
        val result = Lab5UseCase(Lab2UseCase()).generatePoisson(
            lambda = 2.5, realizations = 20, l = 19, mu = 7, m = 64, seed = 7
        )

        assertEquals(20, result.size)
        assertTrue(result.all { it.x >= 0 })
    }

    @Test
    fun itemsAreNumberedFromOne() {
        val result = PoissonGenerator().generate(lambda = 1.0, rValues = List(20) { 0.1 }, count = 3)

        assertEquals(listOf(1, 2, 3), result.map { it.index })
    }
}
