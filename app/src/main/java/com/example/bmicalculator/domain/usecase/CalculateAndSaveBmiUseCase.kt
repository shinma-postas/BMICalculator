package com.example.bmicalculator.domain.usecase

import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.HealthData
import com.example.bmicalculator.domain.repository.HealthRepository
import kotlin.math.roundToInt

// 計算結果を返すsealed class
sealed class BmiCalculationResult {
    data class Success(val result: BmiResult) : BmiCalculationResult()
    data class ValidationError(
        val heightError: String? = null,
        val weightError: String? = null
    ) : BmiCalculationResult()
}

// バリデーションと計算と保存を行う。
class CalculateAndSaveBmiUseCase(private val healthRepository: HealthRepository) {
    // invokeに対しoperatorをつけることでクラスのインスタンスを関数のように呼び出し可能にする
    operator fun invoke(heightText: String, weightText: String): BmiCalculationResult {
        // 空白バリデーション
        if (heightText.isBlank()) {
            return BmiCalculationResult.ValidationError(
                heightError = "身長(cm)を入力してください"
            )
        }
        if (weightText.isBlank()) {
            return BmiCalculationResult.ValidationError(
                weightError = "体重(kg)を入力してください"
            )
        }

        // 数値バリデーション(Stringから数値への変換。失敗時はnullが返る)
        val heightCm = heightText.toDoubleOrNull()
            ?: return BmiCalculationResult.ValidationError(
                heightError = "身長(cm)は数値で入力してください"
            )
        val weightKg = weightText.toDoubleOrNull()
            ?: return BmiCalculationResult.ValidationError(
                weightError = "体重(kg)は数値で入力してください"
            )

        // 正の値バリデーション
        if (heightCm <= 0) {
            return BmiCalculationResult.ValidationError(
                heightError = "身長は0より大きい値である必要があります"
            )
        }
        if (weightKg <= 0) {
            return BmiCalculationResult.ValidationError(
                weightError = "体重は0より大きい値である必要があります"
            )
        }

        // modelのインスタンスを作成
        val healthData: HealthData = HealthData(heightCm = heightCm, weightKg = weightKg)

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

        // modelのインスタンスを作成
        val result: BmiResult = BmiResult(bmi = bmi, category = category)

        // 計算結果を保存
        healthRepository.saveResult(result)

        return BmiCalculationResult.Success(result)
    }
}
