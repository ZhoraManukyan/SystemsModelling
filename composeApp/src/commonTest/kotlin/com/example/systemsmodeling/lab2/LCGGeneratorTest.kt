package com.example.systemsmodeling.lab2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LCGGeneratorTest {

    @Test
    fun multiplicativeGeneratorProducesExpectedSequence() {
        // n(i+1) = (19 * n(i)) mod 64, n0 = 7
        val sequence = LCGGenerator(lambda = 19, mu = 0, m = 64, seed = 7).generate(4)

        assertEquals(listOf(5L, 31L, 13L, 55L), sequence.map { it.first })
    }

    @Test
    fun mixedGeneratorProducesExpectedSequence() {
        // n(i+1) = (19 * n(i) + 7) mod 64, n0 = 7
        val sequence = LCGGenerator(lambda = 19, mu = 7, m = 64, seed = 7).generate(5)

        assertEquals(listOf(12L, 43L, 56L, 47L, 4L), sequence.map { it.first })
    }

    @Test
    fun valuesStayWithinModulus() {
        val sequence = LCGGenerator(lambda = 19, mu = 7, m = 64, seed = 7).generate(200)

        assertTrue(sequence.all { (n, _) -> n in 0L until 64L })
    }

    @Test
    fun normalizedValuesStayInUnitInterval() {
        val sequence = LCGGenerator(lambda = 19, mu = 7, m = 64, seed = 7).generate(200)

        assertTrue(sequence.all { (_, r) -> r in 0.0..1.0 })
    }

    @Test
    fun normalizedValueIsNDividedByMMinusOne() {
        // Current implementation normalizes as r = n / (m - 1).
        val (n, r) = LCGGenerator(lambda = 19, mu = 0, m = 64, seed = 7).generate(1).first()

        assertEquals(n.toDouble() / 63, r, 1e-12)
    }

    @Test
    fun sameSeedGivesSameSequence() {
        val first = LCGGenerator(19, 7, 64, 7).generate(50)
        val second = LCGGenerator(19, 7, 64, 7).generate(50)

        assertEquals(first, second)
    }
}
