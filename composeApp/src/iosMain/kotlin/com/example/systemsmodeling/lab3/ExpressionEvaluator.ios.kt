package com.example.systemsmodeling.lab3

actual class ExpressionEvaluator {
    actual fun evaluate(expression: String, x: Double): Double {
        throw UnsupportedOperationException("Expression parsing via exp4j is available on Android target")
    }
}
