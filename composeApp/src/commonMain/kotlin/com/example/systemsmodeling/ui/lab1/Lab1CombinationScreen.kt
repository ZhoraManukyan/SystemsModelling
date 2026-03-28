package com.example.systemsmodeling.ui.lab1

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
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
import com.example.systemsmodeling.lab1.combination.CombinationUseCase
import com.example.systemsmodeling.lab1.combination.models.CombinationResult
import com.example.systemsmodeling.ui.components.LabScreenLazyColumn
import com.example.systemsmodeling.ui.components.SectionCard
import com.example.systemsmodeling.utils.round3Double

@Composable
fun Lab1CombinationScreen() {
    val useCase = remember { CombinationUseCase() }

    val cells = remember {
        mutableStateListOf("34", "431", "510", "193", "274", "290")
    }
    var result by remember { mutableStateOf<CombinationResult?>(null) }
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
                            result = useCase.execute(input)
                            error = null
                        } catch (_: NumberFormatException) {
                            error = "Проверьте, что во всех заполненных ячейках целые числа"
                        }
                    }
                ) {
                    Text("Рассчитать")
                }
            }
        }

        result?.let { res ->
            item {
                SectionCard(title = "Сводка") {
                    Text("M(X) = ${res.mean.round3Double()}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        if (res.isMeanValid) "M(X) в диапазоне [0.4; 0.6]"
                        else "M(X) вне диапазона",
                        color = if (res.isMeanValid) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                    Text("M(X²) = ${res.meanSquare.round3Double()}")
                    Text("(M(X))² = ${res.meanSquared.round3Double()}")
                    Text(
                        if (res.variance != null)
                            "D(X) = ${res.variance.round3Double()}"
                        else
                            "D(X) не считается"
                    )
                }
            }

            item {
                Text(
                    "Пошаговый разбор",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            items(
                res.items,
                key = { it.original }
            ) { step ->
                SectionCard(title = "Число ${step.original}") {
                    Text("Бинарное: ${step.binary}")
                    Text("Группы: ${step.groups.joinToString(" | ")}")
                    Text("Десятичные: ${step.digits.joinToString(" | ")}")
                    Text("Восьмеричное: ${step.octalString}")
                    Text(
                        "Результат: ${step.value.round3Double()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
