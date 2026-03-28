package com.example.systemsmodeling.lab2.models

data class Lab2Result(
    val sequence: List<Pair<Long, Double>>, // n_i & r_i
    val mean: Double,
    val meanSquare: Double,
    val meanSquared: Double,
    val variance: Double?,
    val isMeanValid: Boolean
)
