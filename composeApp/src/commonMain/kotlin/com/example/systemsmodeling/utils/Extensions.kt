package com.example.systemsmodeling.utils

import kotlin.math.round

fun Double.round3Double(): Double {
    return round(this * 1000) / 1000
}
