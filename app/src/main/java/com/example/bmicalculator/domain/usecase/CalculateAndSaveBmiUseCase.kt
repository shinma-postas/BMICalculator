package com.example.bmicalculator.domain.usecase

import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.HealthData
import com.example.bmicalculator.domain.repository.HealthRepository
import kotlin.math.roundToInt

class CalculateAndSaveBmiUseCase(private val healthRepository: HealthRepository) {
    // invokeに対しoperatorをつけることでクラスのインスタンスを関数のように呼び出し可能にする
    operator fun invoke(healthData: HealthData): BmiResult {
        // cmをmに変換
        val heightM: Double = healthData.heightCm / 100.0

        // BMIを計算
        val rawBmi: Double = healthData.weightKg / (heightM * heightM)

        // 小数点第1位に丸める
        val bmi: Double = (rawBmi * 10).roundToInt() / 10.0

        // 結果からカテゴリを判定
        val category: String = when {
            bmi < 18.5 -> "低体重"
            bmi < 25.0 -> "標準"
            bmi < 30.0 -> "肥満"
            else -> "高度肥満"
        }

        val result: BmiResult = BmiResult(bmi = bmi, category = category)

        // 計算結果を保存(
        healthRepository.saveResult(result)

        return result
    }
}
