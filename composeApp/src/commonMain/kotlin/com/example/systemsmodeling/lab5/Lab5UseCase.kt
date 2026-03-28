package com.example.systemsmodeling.lab5

import com.example.systemsmodeling.lab2.Lab2UseCase
import com.example.systemsmodeling.lab5.models.Lab5Item

class Lab5UseCase(
    private val lab2: Lab2UseCase
) {
    fun generateBinomial(
        p: Double,
        n: Int,
        realizations: Int,
        l: Long,
        mu: Long,
        m: Long,
        seed: Long
    ): List<Lab5Item> {

        val total = n * realizations

        val lcg = lab2.execute(l, mu, m, seed, total)

        val rValues = lcg.sequence.map { it.second }

        return BinomialGenerator().generate(p, n, realizations, rValues)
    }

    fun generatePoisson(
        lambda: Double,
        realizations: Int,
        l: Long,
        mu: Long,
        m: Long,
        seed: Long
    ): List<Lab5Item> {
        val lcg = lab2.execute(l, mu, m, seed, 1000)

        val rValues = lcg.sequence.map { it.second }

        return PoissonGenerator().generate(lambda, rValues, realizations)
    }
}
