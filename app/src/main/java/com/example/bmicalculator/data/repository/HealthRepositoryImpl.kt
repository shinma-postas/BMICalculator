package com.example.bmicalculator.data.repository

import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.repository.HealthRepository

// 計算結果を保持するリポジトリ
class HealthRepositoryImpl : HealthRepository {
    private var lastResult: BmiResult? = null

    // 結果をメンバ変数に代入
    override fun saveResult(result: BmiResult) {
        lastResult = result
    }
}
