package com.example.systemsmodeling.lab3.models

data class AreaStat(
    val areaIndex: Int,
    val left: Double,
    val right: Double,
    val sign: Int,
    val yLow: Double,
    val yHigh: Double,
    val pointsCount: Int,
    val hitsCount: Int,
    val rectangleArea: Double,
    val estimateContribution: Double
)

data class MonteCarloPoint(
    val areaIndex: Int,
    val areaSign: Int,
    val nx: Long,
    val rx: Double,
    val ny: Long,
    val ry: Double,
    val x: Double,
    val y: Double,
    val fx: Double,
    val isHit: Boolean,
    val contribution: Int
)

data class Lab3MonteCarloResult(
    val estimate: Double,
    val exact: Double,
    val absoluteError: Double,
    val yMin: Double,
    val yMax: Double,
    val area: Double,
    val areasCount: Int,
    /** Сколько значений nᵢ последовательности ЛКГ использовано (по методичке — 20). */
    val lcgOutputsUsed: Int,
    val hitsPositive: Int,
    val hitsNegative: Int,
    val areaStats: List<AreaStat>,
    val points: List<MonteCarloPoint>
)
