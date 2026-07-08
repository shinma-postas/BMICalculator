package com.example.bmicalculator.domain.repository

import com.example.bmicalculator.domain.model.BmiResult
import kotlinx.coroutines.flow.Flow

// repositoryメソッド要件を定義
interface BmiRepository {
    val lastResult: Flow<BmiResult?>

    suspend fun saveResult(result: BmiResult)
}
