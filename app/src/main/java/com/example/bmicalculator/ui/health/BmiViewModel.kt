package com.example.bmicalculator.ui.bmi

import androidx.lifecycle.ViewModel
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.usecase.CalculateBmiUseCase
import com.example.bmicalculator.domain.usecase.BmiCalculationResult
import com.example.bmicalculator.data.repository.BmiRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

// 画面が取りうる状態を型で管理
// sealed: 各状態ごとにデータ構造を型として表現でき、それぞれ固有のデータが持てる
sealed class BmiUiState {
    data object Idle : BmiUiState()
    data class Success(val result: BmiResult) : BmiUiState()
    data class Error(val heightError: String?, val weightError: String?) : BmiUiState()
}

class BmiViewModel(private val calculateBmiUseCase: CalculateBmiUseCase) : ViewModel() {
    // 画面状態(デフォルトIdle)
    private val _uiState = MutableStateFlow<BmiUiState>(BmiUiState.Idle)

    // 外部から参照され、読み取り専用なstate
    val uiState: StateFlow<BmiUiState> = _uiState.asStateFlow()

    // 入力値バリデーションと計算呼び出し
    fun onCalculateClicked(heightText: String, weightText: String) {
        when (val calcResult = calculateBmiUseCase(heightText, weightText)) {
            is BmiCalculationResult.Success -> {
                _uiState.value = BmiUiState.Success(calcResult.result)
            }
            is BmiCalculationResult.ValidationError -> {
                _uiState.value = BmiUiState.Error(
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
                BmiViewModel(
                    calculateBmiUseCase = CalculateBmiUseCase(
                        bmiRepository = BmiRepositoryImpl()
                    )
                )
            }
        }
    }
}
