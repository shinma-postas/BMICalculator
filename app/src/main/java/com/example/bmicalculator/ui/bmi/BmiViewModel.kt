package com.example.bmicalculator.ui.bmi

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.usecase.BmiCalculationResult
import com.example.bmicalculator.domain.usecase.CalculateBmiUseCase
import com.example.bmicalculator.domain.usecase.GetLastBmiResultUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// 画面が取りうる状態を型で管理
// sealed: 各状態ごとにデータ構造を型として表現でき、それぞれ固有のデータが持てる
sealed class BmiUiState {
    data object Idle : BmiUiState()
    data class Loaded(val result: BmiResult) : BmiUiState() // 起動時・前回値の復元
    data class Calculated(val result: BmiResult) : BmiUiState() // ボタン押下による計算結果
    data class Error(
        @StringRes val heightError: Int?,
        @StringRes val weightError: Int?
    ) : BmiUiState()
}

class BmiViewModel(
    private val calculateBmiUseCase: CalculateBmiUseCase,
    private val getLastBmiResultUseCase: GetLastBmiResultUseCase
) : ViewModel() {
    // 画面状態(デフォルトIdle)
    private val _uiState = MutableStateFlow<BmiUiState>(BmiUiState.Idle)

    // fragmentで参照されるuiステータス
    val uiState: StateFlow<BmiUiState> = _uiState.asStateFlow()

    // init: インスタンス生成時に一度だけ実行
    init {
        // vmのコルーチンを起動
        viewModelScope.launch {
            // flowから値を1つ取得し、監視を終了。結果がnullでなければstateを更新。
            getLastBmiResultUseCase().firstOrNull()?.let { result ->
                _uiState.value = BmiUiState.Loaded(result)
            }
        }
    }

    // 入力値バリデーションと計算呼び出し
    fun onCalculateClicked(heightText: String, weightText: String) {
        viewModelScope.launch {
            when (val calcResult = calculateBmiUseCase(heightText, weightText)) {
                is BmiCalculationResult.Success -> {
                    _uiState.value = BmiUiState.Calculated(calcResult.result)
                }

                is BmiCalculationResult.ValidationError -> {
                    _uiState.value = BmiUiState.Error(
                        heightError = calcResult.heightError,
                        weightError = calcResult.weightError
                    )
                }
            }
        }
    }
}
