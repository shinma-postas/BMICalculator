# 健康診断アプリ 仕様書（シンプル版）

## 1. アプリ概要

### 1.1 アプリ名

健康診断アプリ

### 1.2 アプリ概要

本アプリは、利用者が身長と体重を入力し、BMI（Body Mass Index）を算出して健康状態を簡易的に判定するAndroidアプリである。

Android開発学習用アプリとして作成し、以下の技術要素の習得を目的とする。

- Activity
- Fragment
- XMLレイアウト
- MVVMアーキテクチャ
- ViewModel
- UseCase
- Repository

### 1.3 利用イメージ

```text
アプリ起動
 ↓
身長・体重入力
 ↓
BMI計算ボタン押下
 ↓
BMI算出
 ↓
判定結果表示
```

例

```text
身長：170cm
体重：65kg

↓

BMI：22.5
判定：標準
```

---

## 2. 学習目的

- Activityの役割
- Fragmentの役割
- MVVMアーキテクチャ
- ViewModelの責務
- UseCaseの責務
- Repositoryの役割
- XMLレイアウト
- ViewBinding
- LiveDataまたはStateFlow

---

## 3. システム構成

```text
MainActivity
    │
    ▼
HealthFragment
    │
    ▼
HealthViewModel
    │
    ▼
CalculateBmiUseCase
    │
    ▼
HealthRepository

モデル
 ├─ HealthData
 └─ BmiResult
```

---

## 4. 画面レイアウトイメージ

### HealthFragment

```text
┌─────────────────────────────┐
│        健康診断アプリ         │ ← TextView
├─────────────────────────────┤
│ 身長(cm)                    │ ← TextView
│ [______________]            │ ← EditText
│                             │
│ 体重(kg)                    │ ← TextView
│ [______________]            │ ← EditText
│                             │
│ [   BMI計算   ]             │ ← Button
│                             │
├─────────────────────────────┤
│ BMI                         │ ← TextView
│ 22.5                        │ ← TextView
│                             │
│ 判定結果                    │ ← TextView
│ 標準                        │ ← TextView
└─────────────────────────────┘
```

### 画面項目一覧

| No | 項目名 | UI部品 | 説明 |
|----|--------|---------|------|
| 1 | タイトル | TextView | アプリ名「健康診断アプリ」を表示する |
| 2 | 身長ラベル | TextView | 「身長(cm)」を表示する |
| 3 | 身長入力欄 | EditText | ユーザーが身長(cm)を入力する |
| 4 | 体重ラベル | TextView | 「体重(kg)」を表示する |
| 5 | 体重入力欄 | EditText | ユーザーが体重(kg)を入力する |
| 6 | BMI計算ボタン | Button | 入力された身長と体重をもとにBMIを計算する |
| 7 | BMIラベル | TextView | BMI結果の項目名を表示する |
| 8 | BMI結果 | TextView | 計算されたBMI値を表示する |
| 9 | 判定結果ラベル | TextView | 判定結果の項目名を表示する |
| 10 | 判定結果 | TextView | BMIに応じた判定結果を表示する |

---

## 5. 機能仕様

### 入力項目

- 身長(cm)
- 体重(kg)

### ボタン

- BMI計算

### 表示項目

- BMI
- 判定結果

### 処理内容

1. 身長を入力する
2. 体重を入力する
3. BMI計算ボタンを押下する
4. BMIを算出する
5. 判定結果を表示する

---

## 6. BMI計算仕様

### 計算式

```text
BMI = 体重(kg) ÷ (身長(m) × 身長(m))
```

### 判定条件

|BMI|判定|
|---|---|
|18.5未満|低体重|
|18.5以上25未満|標準|
|25以上30未満|肥満|
|30以上|高度肥満|

---

## 7. アーキテクチャ

```text
Fragment
 ↓
ViewModel
 ↓
UseCase
 ↓
Repository
```

---

## 8. クラス一覧

|クラス名|種別|役割|
|---------|---------|---------|
|MainActivity|Activity|Fragmentのホスト画面|
|HealthFragment|Fragment|入力・結果表示UI|
|HealthViewModel|ViewModel|画面状態管理|
|CalculateBmiUseCase|UseCase|BMI計算・判定|
|HealthRepository|Repository|計算結果保持|
|HealthData|Model|入力データ保持|
|BmiResult|Model|BMI結果保持|

---

## 9. モデル仕様

### HealthData

```kotlin
data class HealthData(
    val heightCm: Double,
    val weightKg: Double
)
```

### BmiResult

```kotlin
data class BmiResult(
    val bmi: Double,
    val category: String
)
```

---

## 10. パッケージ構成

```text
com.example.healthcheck
│
├─ ui
│   ├─ MainActivity
│   └─ HealthFragment
│
├─ viewmodel
│   └─ HealthViewModel
│
├─ domain
│   ├─ usecase
│   │   └─ CalculateBmiUseCase
│   │
│   └─ model
│       ├─ HealthData
│       └─ BmiResult
│
├─ data
│   └─ HealthRepository
│
└─ util
```

---

## 11. 実装時の注意事項

### 必須

- Activityは1つとする
- Fragmentは1つとする
- XMLレイアウトを利用する
- MVVMを採用する
- UseCaseを作成する

### 意識してほしいこと

- Fragmentは画面表示のみ担当する
- ViewModelは画面状態を管理する
- BMI計算ロジックはUseCaseに実装する
- Repositoryはデータ保持のみ担当する

### NG例

```text
FragmentでBMIを計算する
ViewModelでBMI計算を行う
FragmentからRepositoryを直接呼び出す
```

### OK例

```text
Fragment
 ↓
ViewModel
 ↓
CalculateBmiUseCase
 ↓
Repository
```
