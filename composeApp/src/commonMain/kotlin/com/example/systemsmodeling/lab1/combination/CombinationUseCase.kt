package com.example.systemsmodeling.lab1.combination

import com.example.systemsmodeling.lab1.combination.models.CombinationResult
import com.example.systemsmodeling.statistics.Statistics

class CombinationUseCase {

    private val generator = CombinationGenerator()

    fun execute(input: String): CombinationResult {

        val numbers = input.split(",").map { it.trim().toInt() }

        val items = generator.generateDetailed(numbers)

        val values = items.map { it.value }

        val mean = Statistics.mean(values)
        val meanSquare = Statistics.meanSquare(values)
        val meanSquared = mean * mean

        val isValid = mean in 0.4..0.6

        val variance = if (isValid) {
            meanSquare - meanSquared
        } else null

        return CombinationResult(
            items = items,
            mean = mean,
            meanSquare = meanSquare,
            meanSquared = meanSquared,
            variance = variance,
            isMeanValid = isValid
        )
    }
}
