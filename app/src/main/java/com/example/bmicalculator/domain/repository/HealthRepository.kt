package com.example.bmicalculator.domain.repository

import com.example.bmicalculator.domain.model.BmiResult

// repositoryメソッド要件を定義
interface HealthRepository {
    fun saveResult(result: BmiResult)
    fun getLastResult(): BmiResult?
}
