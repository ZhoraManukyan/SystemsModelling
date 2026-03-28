package com.example.systemsmodeling.lab1.combination.models

data class CombinationResultItem(
    val original: Int,
    val binary: String,
    val groups: List<String>,
    val digits: List<Int>,
    val octalString: String,
    val value: Double
)
