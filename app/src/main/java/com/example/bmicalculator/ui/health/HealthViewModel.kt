package com.example.bmicalculator.ui.health

import androidx.lifecycle.ViewModel
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.HealthData
import com.example.bmicalculator.domain.repository.HealthRepository
import com.example.bmicalculator.domain.usecase.CalculateBmiUseCase
import com.example.bmicalculator.data.repository.HealthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

// 画面が取りうる状態を型で管理
// sealed: 各状態ごとにデータ構造を型として表現でき、それぞれ固有のデータが持てる
sealed class HealthUiState {
    data object Idle : HealthUiState()
    data class Success(val result: BmiResult) : HealthUiState()
    data class Error(val heightError: String?, val weightError: String?) : HealthUiState()
}

class HealthViewModel(
    private val calculateBmiUseCase: CalculateBmiUseCase,
    private val healthRepository: HealthRepository
) : ViewModel() {
    // 画面状態(デフォルトIdel)
    private val _uiState = MutableStateFlow<HealthUiState>(HealthUiState.Idle)

    // 外部から参照され、読み取り専用なstate
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    // 入力値バリデーションと計算呼び出し
    fun onCalculateClicked(heightText: String, weightText: String) {
        // 空白バリデーション
        if (heightText.isBlank()) {
            _uiState.value = HealthUiState.Error(
                heightError = "身長(cm)を入力してください",
                weightError = null
            )
            return
        }
        if (weightText.isBlank()) {
            _uiState.value = HealthUiState.Error(
                heightError = null,
                weightError = "体重(kg)を入力してください"
            )
            return
        }

        // Stringから数値への変換。失敗時はnullが返る
        val heightCm: Double? = heightText.toDoubleOrNull()
        val weightKg: Double? = weightText.toDoubleOrNull()

        // 数値バリデーション
        if (heightCm == null) {
            // 画面状態をエラーに更新
            _uiState.value = HealthUiState.Error(
                heightError = "身長(cm)は数値で入力してください",
                weightError = null
            )
            return
        }
        if (weightKg == null) {
            _uiState.value = HealthUiState.Error(
                heightError = null,
                weightError = "体重(kg)は数値で入力してください"
            )
            return
        }

        // UseCaseを呼び出しBMI計算し結果取得
        val result = calculateBmiUseCase(HealthData(heightCm = heightCm, weightKg = weightKg))

        // 結果はRepositoryに保存
        healthRepository.saveResult(result)

        // 状態をSuccessに更新
        _uiState.value = HealthUiState.Success(result)
    }

    // 依存を必要とするvmをインスタンス化するときに、必要とする依存を定義
    // companion: クラス固有の単一インスタンス
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            // ここでインスタンス作成レシピを登録
            initializer {
                // 実際に依存を渡す
                HealthViewModel(
                    calculateBmiUseCase = CalculateBmiUseCase(),
                    healthRepository = HealthRepositoryImpl()
                )
            }
        }
    }
}
