package com.example.bmicalculator.ui.bmi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.launch

// viewへのアクセスを提供するクラス
import com.example.bmicalculator.databinding.FragmentBmiBinding

class BmiFragment : Fragment() {
    // nullで初期化
    private var _binding: FragmentBmiBinding? = null

    // !!で_bindingをnon-null化 処理の中で使うbindingはnullでない保証をする
    private val binding get() = _binding!!

    // Factory経由でViewModelを生成
    // ViewModelProvider.Factoryは引数なしコンストラクタしか呼べないので、カスタムFactoryで依存を注入する
    private val viewModel: BmiViewModel by viewModels { BmiViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // fragment_bmi.xml をinflateし、FragmentBmiBindingのインスタンスを生成
        _binding = FragmentBmiBinding.inflate(inflater, container, false)

        // viewのroot(xmlの一番外側のタグ)を返す
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // まず親クラスのonViewCreatedを呼ぶ
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculate.setOnClickListener {
            // EditTextの入力値を取得しStringに変換
            val heightStr: String = binding.etHeight.text.toString()
            val weightStr: String = binding.etWeight.text.toString()

            // バリデーション・BMI計算はViewModelに委譲する（Fragmentは表示のみ担当）
            viewModel.onCalculateClicked(heightStr, weightStr)
        }

        // コルーチン定義
        //     viewLifecycleOwner: fragmentのviewライフサイクルを持つライフサイクルオーナー
        //     lifecycleScope: ライフサイクルに紐づいたCoroutineScope
        //     launch: コルーチンを起動
        viewLifecycleOwner.lifecycleScope.launch {
            // STARTED以上(STARTED~RESUMED)の状態の間だけ実行する
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // viewModel.uiStateをstateに受け取り、新しい値がemitされるたび、ラムダを実行
                viewModel.uiState.collect { state ->
                    when (state) {
                        // アイドル時は何もしない
                        is BmiUiState.Idle -> Unit

                        // 成功時、errorをnull、TextViewに値を挿入
                        is BmiUiState.Success -> {
                            binding.tvBmiValue.text = state.result.bmi.toString()
                            binding.tvCategoryValue.text = state.result.category.label
                        }

                        // 失敗時、errorに値を挿入
                        is BmiUiState.Error -> {
                            binding.etHeight.error = state.heightError
                            binding.etWeight.error = state.weightError
                        }
                    }
                }
            }
        }
    }

    // フラグメント破棄時
    override fun onDestroyView() {
        // まず親クラスのonDestroyViewを呼ぶ
        super.onDestroyView()

        // メモリリーク対策として、View破棄と同時に参照を解放
        _binding = null
    }
}
