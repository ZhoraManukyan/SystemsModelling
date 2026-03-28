package com.example.systemsmodeling.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.systemsmodeling.lab2.Lab2UseCase
import com.example.systemsmodeling.lab2.models.Lab2Result
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.ui.utils.NumberField
import com.example.systemsmodeling.utils.round3Double

@Composable
fun Lab2Screen() {
    val useCase = remember { Lab2UseCase() }

    var lambda by remember { mutableStateOf("19") }
    var mu by remember { mutableStateOf("0") }
    var m by remember { mutableStateOf("64") }
    var seed by remember { mutableStateOf("7") }
    var count by remember { mutableStateOf("10") }

    var error by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<Lab2Result?>(null) }

    LabScreenLazyColumn {
        item {
            Text(
                "Линейный конгруэнтный генератор: " +
                        "\n\tМультипликативный -> nᵢ₊₁ = (λ·nᵢ) mod m, " +
                        "\n\tСмешанный -> nᵢ₊₁ = (λ·nᵢ + μ) mod m, " +
                        "\n\trᵢ = nᵢ/m.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SectionCard(title = "Параметры ЛКГ") {
                NumberField("n₀ (начальное)", seed) { seed = it }
                NumberField("λ", lambda) { lambda = it }
                NumberField("μ", mu) { mu = it }
                NumberField("m", m) { m = it }
                NumberField("Количество шагов", count) { count = it }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val l = lambda.toLongOrNull()
                        val muVal = mu.toLongOrNull()
                        val mVal = m.toLongOrNull()
                        val s = seed.toLongOrNull()
                        val c = count.toIntOrNull()

                        if (l == null || muVal == null || mVal == null || s == null || c == null) {
                            error = "Введите корректные числа"
                            return@Button
                        }

                        error = null

                        result = useCase.execute(
                            l,
                            muVal,
                            mVal,
                            s,
                            c
                        )
                    }
                ) {
                    Text("Сгенерировать последовательность")
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
                Text("Последовательность (n, r)", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(
                res.sequence,
                key = { index, _ -> index }
            ) { index, (n, r) ->
                SectionCard(title = "Шаг ${index + 1}") {
                    Text("n${index + 1} = $n")
                    Text("r${index + 1} = ${r.round3Double()}")
                }
            }
        }
    }
}
