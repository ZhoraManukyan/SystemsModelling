package com.example.systemsmodeling.lab2

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Lab2UseCaseTest {

    private val useCase = Lab2UseCase()

    @Test
    fun returnsRequestedNumberOfSteps() {
        val result = useCase.execute(lambda = 19, mu = 7, m = 64, seed = 7, count = 10)

        assertEquals(10, result.sequence.size)
    }

    @Test
    fun statisticsMatchSequence() {
        val result = useCase.execute(lambda = 19, mu = 7, m = 64, seed = 7, count = 20)
        val r = result.sequence.map { it.second }

        assertEquals(r.average(), result.mean, 1e-12)
        assertEquals(r.map { it * it }.average(), result.meanSquare, 1e-12)
        assertEquals(result.mean * result.mean, result.meanSquared, 1e-12)
    }

    @Test
    fun varianceIsCalculatedOnlyWhenMeanIsValid() {
        val result = useCase.execute(lambda = 19, mu = 7, m = 64, seed = 7, count = 64)

        if (result.isMeanValid) {
            assertNotNull(result.variance)
            assertEquals(result.meanSquare - result.meanSquared, result.variance!!, 1e-12)
        } else {
            assertNull(result.variance)
        }
    }
}
