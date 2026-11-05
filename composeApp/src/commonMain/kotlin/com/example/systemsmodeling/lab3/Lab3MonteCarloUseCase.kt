package com.example.systemsmodeling.lab3

import com.example.systemsmodeling.lab2.LCGGenerator
import com.example.systemsmodeling.lab3.models.AreaStat
import com.example.systemsmodeling.lab3.models.Lab3MonteCarloResult
import com.example.systemsmodeling.lab3.models.MonteCarloPoint
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Lab3MonteCarloUseCase {
    private val evaluator = ExpressionEvaluator()

    companion object {
        /** Число членов последовательности ЛКГ; на одну точку выборки уходит 2 значения (x и y). */
        private const val LcgOutputsTotal = 20
    }

    fun execute(
        expression: String,
        x1: Double,
        x2: Double,
        lcgSeed: Long,
        lcgLambda: Long,
        lcgMu: Long,
        lcgM: Long
    ): Lab3MonteCarloResult {
        require(x1 != x2) { "x1 and x2 must be different" }
        require(lcgM > 1) { "m must be > 1" }

        val left = min(x1, x2)
        val right = max(x1, x2)

        var yMin = 0.0
        var yMax = 0.0
        val scanSteps = 1000
        repeat(scanSteps + 1) { i ->
            val x = left + (right - left) * i.toDouble() / scanSteps.toDouble()
            val y = eval(expression, x)
            yMin = min(yMin, y)
            yMax = max(yMax, y)
        }

        if (yMax == yMin) {
            yMax += 1.0
        }

        val segments = buildSignSegments(expression, left, right)
        val areasCount = segments.size.coerceAtLeast(1)
        val totalSamplePoints = LcgOutputsTotal / 2
        val pointsPerSegment = distributeCounts(totalSamplePoints, areasCount)

        val width = right - left
        val area = width * (yMax - yMin)
        val lcg = LCGGenerator(
            lambda = lcgLambda,
            mu = lcgMu,
            m = lcgM,
            seed = lcgSeed
        )
        val randomPairs = lcg.generate(LcgOutputsTotal)

        val points = ArrayList<MonteCarloPoint>(totalSamplePoints)
        val areaStats = ArrayList<AreaStat>(areasCount)
        var estimateSum = 0.0
        var hitsPositive = 0
        var hitsNegative = 0
        var randomIndex = 0

        segments.forEachIndexed { areaIdx, segment ->
            val pointsThisArea = pointsPerSegment[areaIdx]
            val segWidth = segment.right - segment.left
            var segHits = 0
            val segArea = segWidth * (segment.yHigh - segment.yLow)

            repeat(pointsThisArea) {
                val (nx, rxRaw) = randomPairs[randomIndex++]
                val (ny, ryRaw) = randomPairs[randomIndex++]
                val rx = rxRaw.coerceIn(0.0, 1.0)
                val ry = ryRaw.coerceIn(0.0, 1.0)

                val x = segment.left + rx * segWidth
                val y = segment.yLow + ry * (segment.yHigh - segment.yLow)
                val fx = eval(expression, x)

                val hit = if (segment.sign > 0) y <= fx else y >= fx
                val contribution = when {
                    !hit -> 0
                    segment.sign > 0 -> 1
                    else -> -1
                }

                if (contribution > 0) hitsPositive++
                if (contribution < 0) hitsNegative++
                if (hit) segHits++

                points.add(
                    MonteCarloPoint(
                        areaIndex = areaIdx + 1,
                        areaSign = segment.sign,
                        nx = nx,
                        rx = rx,
                        ny = ny,
                        ry = ry,
                        x = x,
                        y = y,
                        fx = fx,
                        isHit = hit,
                        contribution = contribution
                    )
                )
            }
            val segSign = if (segment.sign > 0) 1.0 else -1.0
            val segEstimate = if (pointsThisArea == 0) {
                0.0
            } else {
                segSign * segArea * segHits.toDouble() / pointsThisArea.toDouble()
            }
            estimateSum += segEstimate
            areaStats.add(
                AreaStat(
                    areaIndex = areaIdx + 1,
                    left = segment.left,
                    right = segment.right,
                    sign = segment.sign,
                    yLow = segment.yLow,
                    yHigh = segment.yHigh,
                    pointsCount = pointsThisArea,
                    hitsCount = segHits,
                    rectangleArea = segArea,
                    estimateContribution = segEstimate
                )
            )
        }

        val estimate = estimateSum
        val exact = referenceIntegral(expression, left, right, 20_000)

        return Lab3MonteCarloResult(
            estimate = estimate,
            exact = exact,
            absoluteError = abs(estimate - exact),
            yMin = yMin,
            yMax = yMax,
            area = area,
            areasCount = areasCount,
            lcgOutputsUsed = LcgOutputsTotal,
            hitsPositive = hitsPositive,
            hitsNegative = hitsNegative,
            areaStats = areaStats,
            points = points
        )
    }

    /** Разбивает total на parts целых слагаемых, отличающихся не более чем на 1 (остаток в начало). */
    private fun distributeCounts(total: Int, parts: Int): List<Int> {
        require(parts >= 1)
        val base = total / parts
        val rem = total % parts
        return List(parts) { i -> base + if (i < rem) 1 else 0 }
    }

    private data class SignSegment(
        val left: Double,
        val right: Double,
        val sign: Int,
        val yLow: Double,
        val yHigh: Double
    )

    private fun buildSignSegments(expression: String, left: Double, right: Double): List<SignSegment> {
        val steps = 800
        val eps = 1e-8
        val xs = ArrayList<Double>(steps + 1)
        val ys = ArrayList<Double>(steps + 1)
        repeat(steps + 1) { i ->
            val x = left + (right - left) * i.toDouble() / steps.toDouble()
            xs.add(x)
            ys.add(eval(expression, x))
        }

        val roots = mutableListOf<Double>()
        for (i in 0 until steps) {
            val y1 = ys[i]
            val y2 = ys[i + 1]
            if (y1 == 0.0) roots.add(xs[i])
            if (y1 * y2 < 0.0) {
                val x1 = xs[i]
                val x2 = xs[i + 1]
                val root = x1 - y1 * (x2 - x1) / (y2 - y1)
                roots.add(root)
            }
        }

        val bounds = mutableListOf(left)
        bounds.addAll(roots.filter { it > left + eps && it < right - eps }.sorted())
        bounds.add(right)

        val segments = mutableListOf<SignSegment>()
        for (i in 0 until bounds.lastIndex) {
            val segLeft = bounds[i]
            val segRight = bounds[i + 1]
            if (segRight - segLeft <= eps) continue
            val mid = (segLeft + segRight) / 2.0
            val midY = eval(expression, mid)
            val sign = when {
                midY > eps -> 1
                midY < -eps -> -1
                else -> 0
            }
            if (sign == 0) continue

            var segYMin = 0.0
            var segYMax = 0.0
            val localSteps = 120
            repeat(localSteps + 1) { k ->
                val x = segLeft + (segRight - segLeft) * k.toDouble() / localSteps.toDouble()
                val y = eval(expression, x)
                segYMin = min(segYMin, y)
                segYMax = max(segYMax, y)
            }

            val yLow = if (sign > 0) 0.0 else segYMin
            val yHigh = if (sign > 0) segYMax else 0.0
            if (yHigh - yLow <= eps) continue

            segments.add(
                SignSegment(
                    left = segLeft,
                    right = segRight,
                    sign = sign,
                    yLow = yLow,
                    yHigh = yHigh
                )
            )
        }

        if (segments.isNotEmpty()) return segments

        val fallbackSign = if (eval(expression, (left + right) / 2.0) >= 0.0) 1 else -1
        val fallbackYLow = if (fallbackSign > 0) 0.0 else yMinOn(expression, left, right)
        val fallbackYHigh = if (fallbackSign > 0) yMaxOn(expression, left, right) else 0.0
        return listOf(SignSegment(left, right, fallbackSign, fallbackYLow, fallbackYHigh))
    }

    private fun yMinOn(expression: String, left: Double, right: Double): Double {
        var value = 0.0
        repeat(240) { i ->
            val x = left + (right - left) * i.toDouble() / 239.0
            value = min(value, eval(expression, x))
        }
        return value
    }

    private fun yMaxOn(expression: String, left: Double, right: Double): Double {
        var value = 0.0
        repeat(240) { i ->
            val x = left + (right - left) * i.toDouble() / 239.0
            value = max(value, eval(expression, x))
        }
        return value
    }

    private fun eval(expression: String, x: Double): Double {
        val value = evaluator.evaluate(expression, x)
        require(value.isFinite()) { "f(x) returns non-finite value on interval" }
        return value
    }

    private fun referenceIntegral(expression: String, left: Double, right: Double, steps: Int): Double {
        val h = (right - left) / steps.toDouble()
        var sum = 0.0
        var x = left
        repeat(steps) {
            val xNext = x + h
            val fx = eval(expression, x)
            val fxNext = eval(expression, xNext)
            sum += (fx + fxNext) * h * 0.5
            x = xNext
        }
        return sum
    }
}
