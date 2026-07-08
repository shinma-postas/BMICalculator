package com.example.bmicalculator.domain.usecase

import com.example.bmicalculator.domain.model.BmiCategory
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.BodyData
import com.example.bmicalculator.domain.repository.BmiRepository
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
class CalculateBmiUseCase(private val bmiRepository: BmiRepository) {
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
        val bodyData: BodyData = BodyData(heightCm = heightCm, weightKg = weightKg)

        // cmをmに変換
        val heightM: Double = bodyData.heightCm / 100.0

        // BMIを計算
        val rawBmi: Double = bodyData.weightKg / (heightM * heightM)

        // 小数点第1位に丸める
        val bmi: Double = (rawBmi * 10).roundToInt() / 10.0

        // 結果からカテゴリを判定
        val category: BmiCategory = BmiCategory.fromBmi(bmi)

        // modelのインスタンスを作成
        val result: BmiResult = BmiResult(bodyData = bodyData,  bmi = bmi, category = category)

        // 計算結果を保存
        bmiRepository.saveResult(result)

        return BmiCalculationResult.Success(result)
    }
}
