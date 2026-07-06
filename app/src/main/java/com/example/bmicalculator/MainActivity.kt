package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 親クラスのonCreateを先に実行
        super.onCreate(savedInstanceState)

        // ステータスバー・ナビゲーションバーの裏側までアプリを描画する
        enableEdgeToEdge()

        // xml viewファイルをactivityに関連付け
        // Rについて: プロジェクト内の画面パーツ、画像、文字、色などのリソース("R"esource)を呼び出すためのもの
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // activityにfragmentを読み込み
            supportFragmentManager
                .beginTransaction()
                // fragment_containerを新しく生成したHealthFragmentインスタンスに置き換え(削除→追加)
                .replace(R.id.fragment_container, HealthFragment())
                .commit()
        }

        // enableEdgeToEdge()で全画面表示にした結果、コンテンツがシステムバーの裏に潜り込んだ分だけルートViewにpaddingを付けて元の見た目に戻す
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
