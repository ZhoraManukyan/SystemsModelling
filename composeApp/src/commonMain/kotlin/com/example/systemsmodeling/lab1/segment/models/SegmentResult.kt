package com.example.systemsmodeling.lab1.segment.models

data class SegmentResult(
    val values: List<RandomValue>,
    val mean: Double,
    val meanSquare: Double,
    val meanSquared: Double,
    val variance: Double?,
    val isMeanValid: Boolean
)
