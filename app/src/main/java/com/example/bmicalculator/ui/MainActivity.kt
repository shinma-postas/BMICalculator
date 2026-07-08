package com.example.bmicalculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bmicalculator.R
import com.example.bmicalculator.ui.bmi.BmiFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 親クラスのonCreateを先に実行
        super.onCreate(savedInstanceState)

        // xml viewファイルをactivityに関連付け
        // Rについて: プロジェクト内の画面パーツ、画像、文字、色などのリソース("R"esource)を呼び出すためのもの
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // activityにfragmentを読み込み
            supportFragmentManager
                .beginTransaction()
                // fragment_containerを新しく生成したBmiFragmentインスタンスに置き換え(削除→追加)
                .replace(R.id.fragment_container, BmiFragment())
                .commit()
        }
    }
}
