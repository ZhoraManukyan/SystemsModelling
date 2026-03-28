package com.example.systemsmodeling.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val ScreenPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
private val ItemSpacing = 12.dp

@Composable
fun LabScreenLazyColumn(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = ScreenPadding,
        verticalArrangement = Arrangement.spacedBy(ItemSpacing),
        content = content
    )
}
