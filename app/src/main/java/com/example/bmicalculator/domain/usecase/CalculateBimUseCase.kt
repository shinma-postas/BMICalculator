package com.example.bmicalculator.domain.usecase

import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.HealthData
import kotlin.math.roundToInt

class CalculateBmiUseCase {
    // invokeに対しoperatorをつけることでクラスのインスタンスを関数のように呼び出し可能にする
    operator fun invoke(healthData: HealthData): BmiResult {
        // cmをmに変換
        val heightM = healthData.heightCm / 100.0

        // bimを計算
        val rawBmi = healthData.weightKg / (heightM * heightM)

        // 小数点を丸める
        val bmi = (rawBmi * 10).roundToInt() / 10.0

        // 結果からカテゴリを判定
        val category = when {
            bmi < 18.5 -> "低体重"
            bmi < 25.0 -> "標準"
            bmi < 30.0 -> "肥満"
            else -> "高度肥満"
        }

        // 結果を返す
        return BmiResult(bmi = bmi, category = category)
    }
}
