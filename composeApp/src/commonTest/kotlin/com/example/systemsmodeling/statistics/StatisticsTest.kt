package com.example.systemsmodeling.statistics

import com.example.systemsmodeling.utils.round3Double
import kotlin.test.Test
import kotlin.test.assertEquals

class StatisticsTest {

    @Test
    fun meanOfValues() {
        assertEquals(0.5, Statistics.mean(listOf(0.0, 0.5, 1.0)), 1e-12)
    }

    @Test
    fun meanSquareOfValues() {
        // (0 + 0.25 + 1) / 3
        assertEquals(1.25 / 3, Statistics.meanSquare(listOf(0.0, 0.5, 1.0)), 1e-12)
    }

    @Test
    fun round3RoundsToThreeDecimals() {
        assertEquals(0.079, (5.0 / 63).round3Double(), 1e-12)
        assertEquals(1.235, 1.23456.round3Double(), 1e-12)
    }
}
