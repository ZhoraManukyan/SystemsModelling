package com.example.systemsmodeling.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.systemsmodeling.lab2.Lab2UseCase
import com.example.systemsmodeling.lab5.Lab5UseCase
import com.example.systemsmodeling.lab5.models.Lab5Item
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.ui.utils.NumberField
import com.example.systemsmodeling.ui.utils.NumberFieldDouble
import com.example.systemsmodeling.utils.round3Double

@Composable
fun Lab5Screen() {
    val lab5 = remember { Lab5UseCase(Lab2UseCase()) }

    var p by remember { mutableStateOf("0.45") }
    var n by remember { mutableStateOf("4") }
    var lambda by remember { mutableStateOf("2.5") }
    var realizations by remember { mutableStateOf("5") }

    var lcgLambda by remember { mutableStateOf("19") }
    var lcgMu by remember { mutableStateOf("7") }
    var lcgM by remember { mutableStateOf("64") }
    var lcgSeed by remember { mutableStateOf("7") }

    var result by remember { mutableStateOf<List<Lab5Item>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LabScreenLazyColumn {
        item {
            Text(
                "Биномиальное и пуассоновское распределения на базе ЛКГ.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SectionCard(title = "Параметры ЛКГ") {
                NumberField("n₀", lcgSeed) { lcgSeed = it }
                NumberField("λ (LCG)", lcgLambda) { lcgLambda = it }
                NumberField("μ", lcgMu) { lcgMu = it }
                NumberField("m", lcgM) { lcgM = it }
            }
        }

        item {
            SectionCard(title = "Число реализаций") {
                NumberField("", realizations) { realizations = it }
            }
        }

        item {
            SectionCard(title = "Биномиальное B(N, p)") {
                NumberFieldDouble("p", p) { p = it }
                NumberField("N", n) { n = it }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val pVal = p.toDoubleOrNull()
                        val nVal = n.toIntOrNull()
                        val l = lcgLambda.toLongOrNull()
                        val mu = lcgMu.toLongOrNull()
                        val m = lcgM.toLongOrNull()
                        val seed = lcgSeed.toLongOrNull()
                        val r = realizations.toIntOrNull()

                        if (pVal == null || nVal == null || l == null || mu == null || m == null || seed == null || r == null) {
                            error = "Проверьте ввод"
                            return@Button
                        }

                        result = lab5.generateBinomial(
                            pVal,
                            nVal,
                            r,
                            l,
                            mu,
                            m,
                            seed
                        )

                        error = null
                    }
                ) {
                    Text("Сгенерировать (биномиальное)")
                }
            }
        }

        item {
            SectionCard(title = "Пуассон Pois(λ)") {
                NumberFieldDouble("λ", lambda) { lambda = it }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val lambdaVal = lambda.toDoubleOrNull()
                        val l = lcgLambda.toLongOrNull()
                        val mu = lcgMu.toLongOrNull()
                        val m = lcgM.toLongOrNull()
                        val seed = lcgSeed.toLongOrNull()
                        val r = realizations.toIntOrNull()

                        if (lambdaVal == null || l == null || mu == null || m == null || seed == null || r == null) {
                            error = "Проверьте ввод"
                            return@Button
                        }

                        result = lab5.generatePoisson(
                            lambdaVal,
                            r,
                            l,
                            mu,
                            m,
                            seed
                        )

                        error = null
                    }
                ) {
                    Text("Сгенерировать (Пуассон)")
                }
            }
        }

        error?.let {
            item {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        result?.let { list ->
            item {
                Text("Результаты", style = MaterialTheme.typography.titleMedium)
            }

            items(
                list,
                key = { it.index }
            ) { item ->
                Column {
                    Text(
                        "x${item.index} = ${item.x}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "r: ${item.rValues.joinToString { it.round3Double().toString() }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider(
                        Modifier,
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
                }
            }
        }
    }
}
