package com.example.bmicalculator.ui.health

import androidx.lifecycle.ViewModel
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.usecase.CalculateAndSaveBmiUseCase
import com.example.bmicalculator.domain.usecase.BmiCalculationResult
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

class HealthViewModel(private val calculateAndSaveBmiUseCase: CalculateAndSaveBmiUseCase) : ViewModel() {
    // 画面状態(デフォルトIdle)
    private val _uiState = MutableStateFlow<HealthUiState>(HealthUiState.Idle)

    // 外部から参照され、読み取り専用なstate
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    // 入力値バリデーションと計算呼び出し
    fun onCalculateClicked(heightText: String, weightText: String) {
        when (val calcResult = calculateAndSaveBmiUseCase(heightText, weightText)) {
            is BmiCalculationResult.Success -> {
                _uiState.value = HealthUiState.Success(calcResult.result)
            }
            is BmiCalculationResult.ValidationError -> {
                _uiState.value = HealthUiState.Error(
                    heightError = calcResult.heightError,
                    weightError = calcResult.weightError
                )
            }
        }
    }

    // 依存を必要とするvmをインスタンス化するときに、必要とする依存を定義
    // companion: クラス固有の単一インスタンス
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            // ここでインスタンス作成レシピを登録
            initializer {
                // 実際に依存を渡す
                HealthViewModel(
                    calculateAndSaveBmiUseCase = CalculateAndSaveBmiUseCase(
                        healthRepository = HealthRepositoryImpl()
                    )
                )
            }
        }
    }
}
