package com.example.bmicalculator.domain.usecase

import androidx.annotation.StringRes
import com.example.bmicalculator.R
import com.example.bmicalculator.domain.model.BmiCategory
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.BodyData
import com.example.bmicalculator.domain.repository.BmiRepository
import kotlin.math.roundToInt

// 計算結果を返すsealed class
sealed class BmiCalculationResult {
    data class Success(val result: BmiResult) : BmiCalculationResult()
    data class ValidationError(
        // @StringRes: strings.xmlの文字列のidを指すことを示す。
        @StringRes val heightError: Int? = null,
        @StringRes val weightError: Int? = null,
    ) : BmiCalculationResult()
}

// バリデーションと計算と保存を行う。
class CalculateBmiUseCase(private val bmiRepository: BmiRepository) {
    // invokeに対しoperatorをつけることでクラスのインスタンスを関数のように呼び出し可能にする
    suspend operator fun invoke(heightText: String, weightText: String): BmiCalculationResult {
        // 空白バリデーション
        if (heightText.isBlank()) {
            return BmiCalculationResult.ValidationError(
                heightError = R.string.please_enter_height
            )
        }
        if (weightText.isBlank()) {
            return BmiCalculationResult.ValidationError(
                weightError = R.string.please_enter_weight
            )
        }

        // 数値バリデーション
        val heightCm = heightText.toDoubleOrNull()
            ?: return BmiCalculationResult.ValidationError(
                heightError = R.string.please_enter_height_as_num
            )
        val weightKg = weightText.toDoubleOrNull()
            ?: return BmiCalculationResult.ValidationError(
                weightError = R.string.please_enter_weight_as_num // ※元のコードのバグ(heightErrorになっていたの)も修正
            )

        // 正の値バリデーション
        if (heightCm <= 0) {
            return BmiCalculationResult.ValidationError(
                heightError = R.string.height_must_greater_than_0
            )
        }
        if (weightKg <= 0) {
            return BmiCalculationResult.ValidationError(
                weightError = R.string.weight_must_greater_than_0
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
        val result: BmiResult = BmiResult(bodyData = bodyData, bmi = bmi, category = category)

        // 計算結果を保存
        bmiRepository.saveResult(result)

        return BmiCalculationResult.Success(result)
    }
}
