package com.zhy.mediaplayerserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent.getBundleExtra("qq1455841095")?.let {
            val title = it.getString("media_title")!!
            title_medianame.text = title
        }
    }
}