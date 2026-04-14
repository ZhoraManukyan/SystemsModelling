package com.example.systemsmodeling.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.systemsmodeling.lab3.ExpressionEvaluator
import com.example.systemsmodeling.lab3.Lab3MonteCarloUseCase
import com.example.systemsmodeling.lab3.models.Lab3MonteCarloResult
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.ui.utils.NumberField
import com.example.systemsmodeling.ui.utils.NumberFieldDouble
import com.example.systemsmodeling.utils.round3Double
import kotlin.math.max
import kotlin.math.min

@Composable
fun Lab3Screen() {
    val useCase = remember { Lab3MonteCarloUseCase() }

    var expression by remember { mutableStateOf("-x^2 + x + 12") }
    var x1 by remember { mutableStateOf("-2") }
    var x2 by remember { mutableStateOf("5") }
    var n0 by remember { mutableStateOf("7") }
    var lcgLambda by remember { mutableStateOf("19") }
    var lcgMu by remember { mutableStateOf("0") }
    var lcgM by remember { mutableStateOf("64") }

    var result by remember { mutableStateOf<Lab3MonteCarloResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LabScreenLazyColumn {
        item {
            Text(
                text = "Метод Монте-Карло для определенного интеграла " +
                        "f(x) на отрезке [x1, x2]. Формула вводится через exp4j.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SectionCard(title = "Параметры функции") {
                OutlinedTextField(
                    value = expression,
                    onValueChange = { expression = it },
                    label = { Text("f(x), например: -x^2 + x + 12") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                NumberFieldDouble("x1", x1) { x1 = it }
                NumberFieldDouble("x2", x2) { x2 = it }
            }
        }

        item {
            SectionCard(title = "Параметры ЛКГ и моделирования") {
                Text("Используется ровно 20 значений ЛКГ (n₁…n₂₀); на одну точку — два числа (x и y).")
                Text("Всего точек выборки: 10; при нескольких площадях они делятся между ними поровну (+1 к первым при остатке).")
                NumberField("n0", n0) { n0 = it }
                NumberField("λ", lcgLambda) { lcgLambda = it }
                NumberField("μ", lcgMu) { lcgMu = it }
                NumberField("m", lcgM) { lcgM = it }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val x1Val = x1.toDoubleOrNull()
                        val x2Val = x2.toDoubleOrNull()
                        val n0Val = n0.toLongOrNull()
                        val lambdaVal = lcgLambda.toLongOrNull()
                        val muVal = lcgMu.toLongOrNull()
                        val mVal = lcgM.toLongOrNull()

                        if (
                            expression.isBlank() ||
                            x1Val == null ||
                            x2Val == null ||
                            n0Val == null ||
                            lambdaVal == null ||
                            muVal == null ||
                            mVal == null
                        ) {
                            error = "Проверьте корректность введенных параметров"
                            return@Button
                        }

                        if (x1Val == x2Val) {
                            error = "x1 и x2 не должны совпадать"
                            return@Button
                        }
                        if (mVal <= 1L) {
                            error = "m должно быть больше 1"
                            return@Button
                        }

                        try {
                            error = null
                            result = useCase.execute(
                                expression = expression,
                                x1 = x1Val,
                                x2 = x2Val,
                                lcgSeed = n0Val,
                                lcgLambda = lambdaVal,
                                lcgMu = muVal,
                                lcgM = mVal
                            )
                        } catch (e: Exception) {
                            result = null
                            error = "Ошибка формулы/вычисления: ${e.message ?: "проверьте f(x)"}"
                        }
                    }
                ) {
                    Text("Рассчитать методом Монте-Карло")
                }
            }
        }

        error?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        result?.let { res ->
            item {
                SectionCard(title = "График функции и области интегрирования") {
                    FunctionAndIntegralSketch(
                        expression = expression,
                        x1 = x1.toDoubleOrNull() ?: -2.0,
                        x2 = x2.toDoubleOrNull() ?: 5.0,
                        result = res
                    )
                }
            }

            item {
                SectionCard(title = "Результаты") {
                    Text("Оценка интеграла: ${res.estimate.round3Double()}")
                    Text("Референс (трапеции): ${res.exact.round3Double()}")
                    Text("Абс. ошибка: ${res.absoluteError.round3Double()}")
                    Text("Количество площадей: ${res.areasCount}")
                    Text("Значений ЛКГ: ${res.lcgOutputsUsed}")
                    Text("Всего точек выборки: ${res.points.size}")
                    Text("Границы по y: [${res.yMin.round3Double()}, ${res.yMax.round3Double()}]")
                    Text("Площадь прямоугольника: ${res.area.round3Double()}")
                    Text("Попадания выше OX: ${res.hitsPositive}")
                    Text("Попадания ниже OX: ${res.hitsNegative}")
                }
            }

            item {
                SectionCard(title = "Как получаем площадь (как в тетради)") {
                    Text("Для каждой площади Si строим свой прямоугольник.")
                    Text("Sпрям_i = (xR - xL) * (yHigh - yLow)")
                    Text("I_i ≈ sign_i * Sпрям_i * m_i / N_i")
                    Text("где m_i — сколько точек попало в площадь, N_i — точек в этой площади")
                    Text("Итог: I ≈ Σ I_i")
                }
            }

            itemsIndexed(res.areaStats) { _, stat ->
                SectionCard(title = "Площадь S${stat.areaIndex}") {
                    Text("Интервал x: [${stat.left.round3Double()}, ${stat.right.round3Double()}]")
                    Text("Диапазон y: [${stat.yLow.round3Double()}, ${stat.yHigh.round3Double()}]")
                    Text("Знак: ${if (stat.sign > 0) "+" else "-"}")
                    Text("Sпрям = ${stat.rectangleArea.round3Double()}")
                    Text("Точек в площади N = ${stat.pointsCount}")
                    Text("Попало m = ${stat.hitsCount}")
                    Text("Вклад I${stat.areaIndex} ≈ ${stat.estimateContribution.round3Double()}")
                }
            }

            item {
                Text(
                    text = "Случайные точки",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            itemsIndexed(res.points) { index, point ->
                SectionCard(title = "Точка ${index + 1}") {
                    Text("Площадь S${point.areaIndex}")
                    Text("ЛКГ для x: n=${point.nx}, r=${point.rx.round3Double()}")
                    Text("ЛКГ для y: n=${point.ny}, r=${point.ry.round3Double()}")
                    Text("x = ${point.x.round3Double()}, y = ${point.y.round3Double()}")
                    Text("f(x) = ${point.fx.round3Double()}")
                    Text(
                        if (point.areaSign > 0) {
                            "Проверка: y <= f(x)"
                        } else {
                            "Проверка: y >= f(x)"
                        }
                    )
                    Text("Попадание: ${if (point.isHit) "да" else "нет"}")
                    Text("Вклад: ${point.contribution}")
                }
            }
        }
    }
}

@Composable
private fun FunctionAndIntegralSketch(
    expression: String,
    x1: Double,
    x2: Double,
    result: Lab3MonteCarloResult
) {
    val evaluator = remember { ExpressionEvaluator() }
    val left = min(x1, x2)
    val right = max(x1, x2)
    val intervalWidth = (right - left).coerceAtLeast(1e-6)

    // Expand visible x-range so full curve shape is readable.
    val plotLeft = left - intervalWidth * 0.35
    val plotRight = right + intervalWidth * 0.35

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .padding(top = 4.dp)
    ) {
        val w = size.width
        val h = size.height
        val pad = 24f
        val drawW = w - pad * 2
        val drawH = h - pad * 2

        fun evalSafe(x: Double): Double = runCatching { evaluator.evaluate(expression, x) }.getOrDefault(0.0)

        var plotYMin = min(0.0, result.yMin)
        var plotYMax = max(0.0, result.yMax)
        val scanSteps = 380
        repeat(scanSteps + 1) { i ->
            val x = plotLeft + (plotRight - plotLeft) * i.toDouble() / scanSteps.toDouble()
            val y = evalSafe(x)
            plotYMin = min(plotYMin, y)
            plotYMax = max(plotYMax, y)
        }
        val yRange = (plotYMax - plotYMin).coerceAtLeast(1e-6)
        val yPad = yRange * 0.12
        plotYMin -= yPad
        plotYMax += yPad

        fun mapX(x: Double): Float = (pad + ((x - plotLeft) / (plotRight - plotLeft)).toFloat() * drawW)
        fun mapY(y: Double): Float = (pad + (1f - ((y - plotYMin) / (plotYMax - plotYMin)).toFloat()) * drawH)

        val xAxisY = if (0.0 in plotYMin..plotYMax) mapY(0.0) else mapY(plotYMin)
        val yAxisX = if (0.0 in plotLeft..plotRight) mapX(0.0) else mapX(plotLeft)

        drawRect(
            color = Color(0xFFE8F1FF),
            topLeft = Offset(mapX(left), mapY(result.yMax)),
            size = androidx.compose.ui.geometry.Size(
                (mapX(right) - mapX(left)),
                (mapY(result.yMin) - mapY(result.yMax))
            )
        )

        drawLine(Color.Gray, Offset(pad, xAxisY), Offset(w - pad, xAxisY), strokeWidth = 2f)
        drawLine(Color.Gray, Offset(yAxisX, pad), Offset(yAxisX, h - pad), strokeWidth = 2f)

        drawLine(Color(0xFF1E88E5), Offset(mapX(left), pad), Offset(mapX(left), h - pad), strokeWidth = 2f)
        drawLine(Color(0xFF1E88E5), Offset(mapX(right), pad), Offset(mapX(right), h - pad), strokeWidth = 2f)

        // Shade integral parts: positive area (S1) and negative area (S2) with different colors.
        val integralSteps = 260
        val strokePx = ((mapX(right) - mapX(left)) / integralSteps.toFloat()).coerceAtLeast(1f)
        repeat(integralSteps + 1) { i ->
            val x = left + (right - left) * i.toDouble() / integralSteps.toDouble()
            val fx = evalSafe(x)
            val y0 = mapY(0.0)
            val yf = mapY(fx)

            if (fx >= 0.0) {
                drawLine(
                    color = Color(0x662E7D32),
                    start = Offset(mapX(x), y0),
                    end = Offset(mapX(x), yf),
                    strokeWidth = strokePx
                )
            } else {
                drawLine(
                    color = Color(0x66C62828),
                    start = Offset(mapX(x), y0),
                    end = Offset(mapX(x), yf),
                    strokeWidth = strokePx
                )
            }
        }

        val steps = 220
        var prev: Offset? = null
        repeat(steps + 1) { i ->
            val x = plotLeft + (plotRight - plotLeft) * i.toDouble() / steps.toDouble()
            val y = evalSafe(x)
            val p = Offset(mapX(x), mapY(y))
            prev?.let { drawLine(Color.Black, it, p, strokeWidth = 2.2f) }
            prev = p
        }

        result.points.take(300).forEach { point ->
            val c = when (point.contribution) {
                1 -> Color(0xFF2E7D32)
                -1 -> Color(0xFFC62828)
                else -> Color(0xFF9E9E9E)
            }
            drawCircle(
                color = c,
                radius = 3.3f,
                center = Offset(mapX(point.x), mapY(point.y))
            )
        }
    }
}
