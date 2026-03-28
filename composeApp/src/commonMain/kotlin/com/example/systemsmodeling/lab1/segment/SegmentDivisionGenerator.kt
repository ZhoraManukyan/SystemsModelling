package com.example.systemsmodeling.lab1.segment

import com.example.systemsmodeling.lab1.segment.models.RandomValue
import kotlin.random.Random

class SegmentDivisionGenerator {
    fun generate(values: List<Int>, bits: Int = 4): List<RandomValue> {
        val m = 1 shl bits

        return values.map { value ->
            val binary = value.toString(2).padStart(bits, '0')

            val left = value / m.toDouble()
            val right = (value + 1) / m.toDouble()

            val randomValue = Random.Default.nextDouble(left, right)

            RandomValue(
                original = value,
                binary = binary,
                left = left,
                right = right,
                randomValue = randomValue
            )
        }
    }
}
