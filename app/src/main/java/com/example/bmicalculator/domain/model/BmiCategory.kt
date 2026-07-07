package com.example.bmicalculator.domain.model

enum class BmiCategory(val label: String) {
    // enumの値とラベルを設定
    UNDERWEIGHT("低体重"),
    NORMAL("標準"),
    OBESE("肥満"),
    SEVERELY_OBESE("高度肥満");

    companion object {
        fun fromBmi(bmi: Double): BmiCategory = when {
            bmi < 18.5 -> UNDERWEIGHT
            bmi < 25.0 -> NORMAL
            bmi < 30.0 -> OBESE
            else -> SEVERELY_OBESE
        }
    }
}
