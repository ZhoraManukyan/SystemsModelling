package com.example.systemsmodeling.lab1.segment

import com.example.systemsmodeling.lab1.segment.models.SegmentResult
import com.example.systemsmodeling.statistics.Statistics

class SegmentUseCase {
    private val generator = SegmentDivisionGenerator()

    fun execute(input: String): SegmentResult {
        val numbers = input.split(",").map { it.trim().toInt() }

        val generated = generator.generate(numbers)
        val values = generated.map { it.randomValue }

        val mean = Statistics.mean(values)
        val meanSquare = Statistics.meanSquare(values)
        val meanSquared = mean * mean

        val isValid = mean in 0.4..0.6

        val variance = if (isValid) {
            meanSquare - meanSquared
        } else {
            null
        }

        return SegmentResult(
            values = generated,
            mean = mean,
            meanSquare = meanSquare,
            meanSquared = meanSquared,
            variance = variance,
            isMeanValid = isValid
        )
    }
}
