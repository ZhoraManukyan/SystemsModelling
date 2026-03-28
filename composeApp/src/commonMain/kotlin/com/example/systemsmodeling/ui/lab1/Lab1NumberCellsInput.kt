package com.example.systemsmodeling.ui.lab1

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val CellsRowHeight = 104.dp

/**
 * Горизонтальный «табличный» ввод: ячейки в фиксированной по высоте полосе с горизонтальной прокруткой;
 * «+» добавляет пустую ячейку, «−» удаляет последнюю (если ячеек больше одной).
 */
@Composable
fun Lab1NumberCellsRow(
    cells: SnapshotStateList<String>,
    modifier: Modifier = Modifier,
    addButtonContentDescription: String = "Добавить ячейку",
    removeButtonContentDescription: String = "Удалить последнюю ячейку"
) {
    val scroll = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(CellsRowHeight)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .horizontalScroll(scroll),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cells.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { new ->
                        cells[index] = new.filter { it.isDigit() }
                    },
                    modifier = Modifier.width(88.dp),
                    singleLine = true,
                    label = { Text("${index + 1}", style = MaterialTheme.typography.labelSmall) },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
            if (cells.size > 1) {
                FilledTonalIconButton(
                    onClick = { cells.removeAt(cells.lastIndex) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = removeButtonContentDescription
                    )
                }
            }
            FilledTonalIconButton(
                onClick = { cells.add("") }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = addButtonContentDescription
                )
            }
        }
    }
}

fun commaSeparatedFromCells(cells: List<String>): String =
    cells.asSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(",")
