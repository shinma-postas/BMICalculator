package com.example.bmicalculator.ui.bmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

// viewへのアクセスを提供するクラス
import com.example.bmicalculator.databinding.FragmentBmiBinding
import kotlinx.coroutines.launch

class BmiFragment : Fragment() {
    // nullで初期化
    private var _binding: FragmentBmiBinding? = null

    // !!で_bindingをnon-null化 処理の中で使うbindingはnullでない保証をする
    private val binding get() = _binding!!

    // Factory経由でViewModelを生成
    // ViewModelProvider.Factoryは引数なしコンストラクタしか呼べないので、カスタムFactoryで依存を注入する
    private val viewModel: BmiViewModel by viewModels {
        BmiViewModelFactory(requireActivity().application)
    }

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

        // コルーチンを起動
        viewLifecycleOwner.lifecycleScope.launch {
            // ライフサイクル連動(STARTED未満で自動キャンセル、STARTEDに戻ると再実行)
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Flowの値監視・処理
                viewModel.uiState.collect { state ->
                    when (state) {
                        // アイドル時は何もしない
                        is BmiUiState.Idle -> Unit

                        // 起動、復元時
                        is BmiUiState.Loaded -> {
                            // TextEditに入力値を復元
                            binding.etHeight.setText(state.result.bodyData.heightCm.toString())
                            binding.etWeight.setText(state.result.bodyData.weightKg.toString())

                            // bmi結果テキストを復元
                            binding.tvBmiValue.text = state.result.bmi.toString()
                            binding.tvCategoryValue.text = state.result.category.label
                        }

                        // 計算完了時
                        is BmiUiState.Calculated -> {
                            // bmi結果テキストを更新
                            binding.tvBmiValue.text = state.result.bmi.toString()
                            binding.tvCategoryValue.text = state.result.category.label
                        }

                        // 失敗時
                        is BmiUiState.Error -> {
                            // TextEditのerrorに値を挿入
                            binding.etHeight.error = state.heightError?.let { getString(it) }
                            binding.etWeight.error = state.weightError?.let { getString(it) }
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
