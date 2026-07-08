package com.example.bmicalculator.ui.bmi

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bmicalculator.data.repository.BmiRepositoryImpl
import com.example.bmicalculator.domain.usecase.CalculateBmiUseCase
import com.example.bmicalculator.domain.usecase.GetLastBmiResultUseCase

// BmiViewModelの生成レシピを定義する専用Factory
// ViewModelProvider.Factory: 引数ありコンストラクタを持つViewModelを生成するための仕組み
class BmiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = BmiRepositoryImpl(application.applicationContext)

        return BmiViewModel(
            calculateBmiUseCase = CalculateBmiUseCase(bmiRepository = repository),
            getLastBmiResultUseCase = GetLastBmiResultUseCase(bmiRepository = repository)
        ) as T
    }
}
