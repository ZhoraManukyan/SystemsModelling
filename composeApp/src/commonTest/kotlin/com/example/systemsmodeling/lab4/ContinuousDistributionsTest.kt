package com.example.systemsmodeling.lab4

import kotlin.math.E
import kotlin.math.exp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContinuousDistributionsTest {

    @Test
    fun exponentialInvertsTheCdf() {
        // x = -ln(r) / lambda, so r = e^-1 gives x = 1 / lambda
        val x = ExponentialGenerator().generate(lambda = 0.5, values = listOf(1 / E)).single()

        assertEquals(2.0, x, 1e-12)
    }

    @Test
    fun exponentialOfOneIsZero() {
        val x = ExponentialGenerator().generate(lambda = 0.18, values = listOf(1.0)).single()

        assertEquals(0.0, x, 1e-12)
    }

    @Test
    fun exponentialMatchesAppExample() {
        // From the app: lambda = 0.18, r1 = 0.19 -> x1 = 9.226
        val x = ExponentialGenerator().generate(lambda = 0.18, values = listOf(0.19)).single()

        assertEquals(9.226, x, 1e-3)
    }

    @Test
    fun normalReturnsMuWhenSumOfTwelveIsSix() {
        val x = NormalGenerator().generate(mu = 10.0, sigma = 2.0, rValues = List(12) { 0.5 }).single()

        assertEquals(10.0, x, 1e-12)
    }

    @Test
    fun normalUsesTwelveValuesPerSample() {
        val result = NormalGenerator().generate(mu = 0.0, sigma = 1.0, rValues = List(30) { 0.5 })

        assertEquals(2, result.size) // 30 values -> 2 full groups of 12, remainder ignored
    }

    @Test
    fun normalShiftsBySigma() {
        // sum = 7 -> x = mu + sigma * (7 - 6)
        val values = List(11) { 0.5 } + listOf(1.5)
        val x = NormalGenerator().generate(mu = 1.0, sigma = 3.0, rValues = values).single()

        assertEquals(4.0, x, 1e-12)
    }

    @Test
    fun logNormalIsExpOfNormal() {
        val x = LogNormalGenerator().generate(mu = 1.0, sigma = 2.0, rValues = List(12) { 0.5 }).single()

        assertEquals(exp(1.0), x, 1e-12)
    }

    @Test
    fun logNormalIsAlwaysPositive() {
        val values = List(120) { i -> ((i * 37) % 100) / 100.0 }
        val result = LogNormalGenerator().generate(mu = 0.0, sigma = 1.0, rValues = values)

        assertTrue(result.all { it > 0.0 })
    }

    @Test
    fun productMethodSumsLogsOfGroup() {
        // k = 2, r = e^-1 each -> product e^-2 -> x = 2 / alpha
        val x = ProductMethodGenerator().generate(alpha = 4.0, rValues = listOf(1 / E, 1 / E), k = 2).single()

        assertEquals(0.5, x, 1e-12)
    }

    @Test
    fun productMethodIgnoresIncompleteGroup() {
        val result = ProductMethodGenerator().generate(alpha = 1.0, rValues = List(7) { 0.5 }, k = 3)

        assertEquals(2, result.size)
    }
}
