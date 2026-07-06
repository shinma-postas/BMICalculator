package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // idを指定してツールバーを渡す。
        setSupportActionBar(findViewById(R.id.toolbar))

        // TextViewでタイトルを指定しているため、デフォルトのタイトルを非表示
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Rについて: プロジェクト内の画面パーツ、画像、文字、色などのリソース("R"esource)を呼び出すためのもの
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
