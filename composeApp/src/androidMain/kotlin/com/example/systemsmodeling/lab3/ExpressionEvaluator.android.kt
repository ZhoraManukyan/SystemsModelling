package com.example.systemsmodeling.lab3

import net.objecthunter.exp4j.ExpressionBuilder

actual class ExpressionEvaluator {
    actual fun evaluate(expression: String, x: Double): Double {
        return ExpressionBuilder(expression)
            .variable("x")
            .build()
            .setVariable("x", x)
            .evaluate()
    }
}
