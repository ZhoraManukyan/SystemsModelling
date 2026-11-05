package com.example.systemsmodeling.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.systemsmodeling.lab4.ExponentialGenerator
import com.example.systemsmodeling.lab4.LogNormalGenerator
import com.example.systemsmodeling.lab4.NormalGenerator
import com.example.systemsmodeling.lab4.ProductMethodGenerator
import com.example.systemsmodeling.lab2.Lab2UseCase
import com.example.systemsmodeling.lab4.models.Lab4Item
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.ui.utils.NumberField
import com.example.systemsmodeling.ui.utils.NumberFieldDouble
import com.example.systemsmodeling.utils.DistributionType
import com.example.systemsmodeling.utils.round3Double

@Composable
fun Lab4Screen() {
    val lab2 = remember { Lab2UseCase() }

    var selected by remember { mutableStateOf(DistributionType.EXPONENTIAL) }

    var lambdaLcg by remember { mutableStateOf("19") }
    var muLcg by remember { mutableStateOf("7") }
    var mLcg by remember { mutableStateOf("64") }
    var seed by remember { mutableStateOf("7") }

    var lambda by remember { mutableStateOf("0.18") }
    var alpha by remember { mutableStateOf("0.18") }
    var k by remember { mutableStateOf("3") }
    var mu by remember { mutableStateOf("6") }
    var sigma by remember { mutableStateOf("2") }

    var realizations by remember { mutableStateOf("5") }

    var error by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<List<Lab4Item>?>(null) }

    LabScreenLazyColumn {
        item {
            SectionCard(title = "Генератор ЛКГ (база)") {
                NumberFieldDouble("n₀", seed) { seed = it }
                NumberFieldDouble("λ (LCG)", lambdaLcg) { lambdaLcg = it }
                NumberFieldDouble("μ (LCG)", muLcg) { muLcg = it }
                NumberFieldDouble("m", mLcg) { mLcg = it }
            }
        }

        item {
            SectionCard(title = "Распределение") {
                Column {
                    DistributionType.entries.forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selected == type,
                                onClick = { selected = type }
                            )
                            Text(
                                text = type.labelRu,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        when (selected) {
            DistributionType.EXPONENTIAL -> {
                item {
                    SectionCard(title = "Параметры: экспоненциальное") {
                        NumberFieldDouble("λ", lambda) { lambda = it }
                    }
                }
            }

            DistributionType.PRODUCT -> {
                item {
                    SectionCard(title = "Параметры: произведение") {
                        NumberFieldDouble("α", alpha) { alpha = it }
                        NumberFieldDouble("k", k) { k = it }
                    }
                }
            }

            DistributionType.NORMAL -> {
                item {
                    SectionCard(title = "Параметры: нормальное") {
                        NumberFieldDouble("μ", mu) { mu = it }
                        NumberFieldDouble("σ", sigma) { sigma = it }
                    }
                }
            }

            DistributionType.LOG_NORMAL -> {
                item {
                    SectionCard(title = "Параметры: логнормальное") {
                        NumberFieldDouble("μ", mu) { mu = it }
                        NumberFieldDouble("σ", sigma) { sigma = it }
                    }
                }
            }
        }

        item {
            SectionCard(title = "Выборка") {
                NumberField("Количество реализаций", realizations) { realizations = it }
                Spacer(Modifier.height(4.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val lcgLambda = lambdaLcg.toLongOrNull()
                        val lcgMu = muLcg.toLongOrNull()
                        val lcgM = mLcg.toLongOrNull()
                        val lcgSeed = seed.toLongOrNull()
                        val n = realizations.toIntOrNull()

                        if (lcgLambda == null || lcgMu == null || lcgM == null || lcgSeed == null || n == null) {
                            error = "Ошибка ввода"
                            return@Button
                        }

                        val multiplier = when (selected) {
                            DistributionType.EXPONENTIAL -> 1
                            DistributionType.PRODUCT -> 3
                            DistributionType.NORMAL -> 12
                            DistributionType.LOG_NORMAL -> 12
                        }

                        val totalR = n * multiplier

                        val lcgResult = lab2.execute(
                            lcgLambda,
                            lcgMu,
                            lcgM,
                            lcgSeed,
                            totalR
                        )

                        val pairs = lcgResult.sequence

                        val rValues = pairs.map { it.second.round3Double() }

                        val xValues = when (selected) {
                            DistributionType.EXPONENTIAL -> {
                                val l = lambda.toDoubleOrNull() ?: return@Button
                                ExponentialGenerator().generate(l, rValues)
                            }

                            DistributionType.PRODUCT -> {
                                val a = alpha.toDoubleOrNull() ?: return@Button
                                ProductMethodGenerator().generate(a, rValues, 3)
                            }

                            DistributionType.NORMAL -> {
                                val m = mu.toDoubleOrNull() ?: return@Button
                                val s = sigma.toDoubleOrNull() ?: return@Button
                                NormalGenerator().generate(m, s, rValues)
                            }

                            DistributionType.LOG_NORMAL -> {
                                val m = mu.toDoubleOrNull() ?: return@Button
                                val s = sigma.toDoubleOrNull() ?: return@Button
                                LogNormalGenerator().generate(m, s, rValues)
                            }
                        }.take(n)

                        result = xValues.mapIndexed { i, x ->
                            val (ni, ri) = pairs[i]

                            Lab4Item(
                                index = i + 1,
                                n = ni,
                                r = ri,
                                x = x
                            )
                        }

                        error = null
                    }
                ) {
                    Text("Рассчитать")
                }
            }
        }

        error?.let {
            item {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
            }
        }

        result?.let { list ->
            item {
                Text(
                    "Результат: n → r → x",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(
                list,
                key = { it.index }
            ) { row ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            "Шаг ${row.index}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (selected == DistributionType.PRODUCT || selected == DistributionType.EXPONENTIAL) {
                            Text("n${row.index} = ${row.n}")
                            Text("r${row.index} = ${row.r.round3Double()}")
                        }
                        Text("x${row.index} = ${row.x.round3Double()}")
                    }
                }
            }
        }
    }
}
