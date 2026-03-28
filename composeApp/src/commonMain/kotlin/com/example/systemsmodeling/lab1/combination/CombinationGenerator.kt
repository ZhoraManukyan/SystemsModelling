package com.example.systemsmodeling.lab1.combination

import com.example.systemsmodeling.lab1.combination.models.CombinationResultItem
import kotlin.math.pow

class CombinationGenerator {
    fun generateDetailed(values: List<Int>): List<CombinationResultItem> {
        return values.map { number ->

            val binary = number.toString(2).padStart(9, '0')

            val groups = binary.chunked(3)

            val digits = groups.map { it.toInt(2) }

            val octalString = "0." + digits.joinToString("")

            val value = digits.withIndex().sumOf { (i, d) ->
                d / 8.0.pow(i + 1)
            }

            CombinationResultItem(
                original = number,
                binary = binary,
                groups = groups,
                digits = digits,
                octalString = octalString,
                value = value
            )
        }
    }
}
