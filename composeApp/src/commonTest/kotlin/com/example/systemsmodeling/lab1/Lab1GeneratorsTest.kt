package com.example.systemsmodeling.lab1

import com.example.systemsmodeling.lab1.combination.CombinationGenerator
import com.example.systemsmodeling.lab1.segment.SegmentDivisionGenerator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Lab1GeneratorsTest {

    @Test
    fun segmentDivisionPutsValueInsideItsSegment() {
        val values = SegmentDivisionGenerator().generate(listOf(0, 5, 15), bits = 4)

        values.forEach { v ->
            assertEquals(v.original / 16.0, v.left, 1e-12)
            assertEquals((v.original + 1) / 16.0, v.right, 1e-12)
            assertTrue(v.randomValue >= v.left && v.randomValue < v.right)
        }
    }

    @Test
    fun segmentDivisionPadsBinaryToBitCount() {
        val value = SegmentDivisionGenerator().generate(listOf(5), bits = 4).single()

        assertEquals("0101", value.binary)
    }

    @Test
    fun combinationMethodConvertsBinaryGroupsToOctalFraction() {
        // 81 = 001 010 001 -> digits 1, 2, 1 -> 0.121 (octal) = 1/8 + 2/64 + 1/512
        val item = CombinationGenerator().generateDetailed(listOf(81)).single()

        assertEquals("001010001", item.binary)
        assertEquals(listOf("001", "010", "001"), item.groups)
        assertEquals(listOf(1, 2, 1), item.digits)
        assertEquals("0.121", item.octalString)
        assertEquals(1.0 / 8 + 2.0 / 64 + 1.0 / 512, item.value, 1e-12)
    }

    @Test
    fun combinationValueIsInUnitInterval() {
        val items = CombinationGenerator().generateDetailed((0 until 512).toList())

        assertTrue(items.all { it.value >= 0.0 && it.value < 1.0 })
    }
}
