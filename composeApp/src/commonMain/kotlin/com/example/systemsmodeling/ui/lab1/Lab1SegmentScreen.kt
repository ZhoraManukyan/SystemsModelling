package com.example.systemsmodeling.ui.lab1

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.systemsmodeling.lab1.segment.SegmentUseCase
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.utils.round3Double

@Composable
fun Lab1SegmentScreen() {
    val useCase = remember { SegmentUseCase() }

    val cells = remember {
        mutableStateListOf("4", "5", "8", "9", "15", "1")
    }
    var resultText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LabScreenLazyColumn {
        item {
            SectionCard(title = "Вход: набор чисел") {
                Text(
                    text = "Каждое число — отдельная ячейка; «+» добавляет новую.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Lab1NumberCellsRow(cells = cells)
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val input = commaSeparatedFromCells(cells)
                        if (input.isEmpty()) {
                            error = "Введите хотя бы одно число"
                            return@Button
                        }
                        try {
                            val result = useCase.execute(input)
                            error = null
                            resultText = buildString {
                                appendLine("M(X) = ${result.mean.round3Double()}")

                                if (result.isMeanValid) {
                                    appendLine("M(X) ∈ [0.4; 0.6]")
                                } else {
                                    appendLine("M(X) ∉ [0.4; 0.6]")
                                }

                                appendLine("M(X^2) = ${result.meanSquare.round3Double()}")
                                appendLine("(M(X))^2 = ${result.meanSquared.round3Double()}")

                                if (result.variance != null) {
                                    appendLine("D(X) = ${result.variance.round3Double()}")
                                } else {
                                    appendLine("D(X) не рассчитывается")
                                }

                                appendLine()
                                appendLine("Значения:")

                                result.values.forEach {
                                    appendLine("${it.original} -> ${it.binary} -> [${it.left}; ${it.right}] -> ${it.randomValue.round3Double()}")
                                }
                            }
                        } catch (_: NumberFormatException) {
                            error = "Проверьте, что во всех заполненных ячейках целые числа"
                        }
                    }
                ) {
                    Text("Рассчитать")
                }
            }
        }

        if (resultText.isNotBlank()) {
            item {
                SectionCard(title = "Результат") {
                    SelectionContainer {
                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
