package com.example.bmicalculator.domain.model

data class BmiResult(
    val bodyData: BodyData,
    val bmi: Double,
    val category: BmiCategory
)
