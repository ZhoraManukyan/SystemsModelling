package com.example.systemsmodeling.lab1.combination.models

data class CombinationResult(
    val items: List<CombinationResultItem>,
    val mean: Double,
    val meanSquare: Double,
    val meanSquared: Double,
    val variance: Double?,
    val isMeanValid: Boolean
)
