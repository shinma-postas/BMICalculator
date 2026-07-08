package com.example.bmicalculator.data.repository

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bmicalculator.data.local.bmiDataStore
import com.example.bmicalculator.domain.model.BmiCategory
import com.example.bmicalculator.domain.model.BmiResult
import com.example.bmicalculator.domain.model.BodyData
import com.example.bmicalculator.domain.repository.BmiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 計算結果を保持するリポジトリ
class BmiRepositoryImpl(private val context: Context) : BmiRepository {
    // 型を指定したkeyを定義
    companion object {
        private val KEY_HEIGHT = doublePreferencesKey("height_cm")
        private val KEY_WEIGHT = doublePreferencesKey("weight_kg")
        private val KEY_BMI = doublePreferencesKey("bmi_value")
        private val KEY_CATEGORY = stringPreferencesKey("category")
    }

    // 読み込み処理
    // storeを監視。値が変わるたびに新しい値が流れてくる
    override val lastResult: Flow<BmiResult?> = context.bmiDataStore.data
        .map { prefs ->
            // storeのデータをBmiResult形式に変換して返す
            val height = prefs[KEY_HEIGHT]
            val weight = prefs[KEY_WEIGHT]
            val bmi = prefs[KEY_BMI]
            val categoryString = prefs[KEY_CATEGORY]

            if (height != null && weight != null && bmi != null && categoryString != null) {
                BmiResult(
                    bodyData = BodyData(heightCm = height, weightKg = weight),
                    bmi = bmi,
                    category = BmiCategory.valueOf(categoryString)
                )
            } else {
                null
            }
        }

    // データ保存処理
    override suspend fun saveResult(result: BmiResult) {
        // editでstoreを書き換え
        context.bmiDataStore.edit { prefs ->
            prefs[KEY_HEIGHT] = result.bodyData.heightCm
            prefs[KEY_WEIGHT] = result.bodyData.weightKg
            prefs[KEY_BMI] = result.bmi
            prefs[KEY_CATEGORY] = result.category.name
        }
    }
}