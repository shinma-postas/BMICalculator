package com.example.bmicalculator.domain.usecase

import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.repository.BmiRepository
import kotlinx.coroutines.flow.Flow

// 前回のBMI計算結果を取得するUseCase
class GetLastBmiResultUseCase(private val bmiRepository: BmiRepository) {
    operator fun invoke(): Flow<BmiResult?> = bmiRepository.lastResult
}
