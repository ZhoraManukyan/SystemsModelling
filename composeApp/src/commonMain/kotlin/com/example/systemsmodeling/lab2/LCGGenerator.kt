package com.example.systemsmodeling.lab2

class LCGGenerator(
    private val lambda: Long,
    private val mu: Long,
    private val m: Long,
    private var seed: Long,
) {
    fun generate(count: Int): List<Pair<Long, Double>> {

        val result = mutableListOf<Pair<Long, Double>>()

        var current = seed

        repeat(count) {

            current = if (mu != 0L) {
                (lambda * current + mu) % m
            } else {
                (lambda * current) % m
            }

            val r = current.toDouble() / (m - 1)

            result.add(current to r)
        }

        return result
    }
}
