package com.example.systemsmodeling.lab2

import com.example.systemsmodeling.lab2.models.Lab2Result
import com.example.systemsmodeling.statistics.Statistics

class Lab2UseCase {
    fun execute(
        lambda: Long,
        mu: Long,
        m: Long,
        seed: Long,
        count: Int
    ): Lab2Result {
        val generator = LCGGenerator(lambda, mu, m, seed)

        val pairs = generator.generate(count)

        val values = pairs.map { it.second }

        val mean = Statistics.mean(values)
        val meanSquare = Statistics.meanSquare(values)
        val meanSquared = mean * mean

        val isValid = mean in 0.4..0.6

        val variance = if (isValid) {
            meanSquare - meanSquared
        } else null

        return Lab2Result(
            sequence = pairs,
            mean = mean,
            meanSquare = meanSquare,
            meanSquared = meanSquared,
            variance = variance,
            isMeanValid = isValid
        )
    }
}
