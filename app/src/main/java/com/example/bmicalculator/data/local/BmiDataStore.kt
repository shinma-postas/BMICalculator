package com.example.bmicalculator.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// このファイルはアプリ全体で共有するbmi_preferencesという名前の保存箱への入口を定義しているイメージ

// const val: コンパイル時に値が確定した定数であることを保証(トップレベルかobject内でしか使えない)
private const val BMI_PREFERENCES_NAME = "bmi_preferences"

// Context: アプリケーション環境に関するグローバル情報へのインターフェース
// ContextクラスにbmiDataStoreプロパティを後付け
// preferencesDataStore(): 指定した名前のファイルを作る/開くためのDataStore生成の仕組み
val Context.bmiDataStore: DataStore<Preferences> by preferencesDataStore(name = BMI_PREFERENCES_NAME)
