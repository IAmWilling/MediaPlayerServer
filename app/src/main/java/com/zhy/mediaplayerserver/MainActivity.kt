package com.zhy.mediaplayerserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhy.mediaplayerserver.playermanager.MediaPlayerService
import com.zhy.mediaplayerserver.playermanager.manager.MusicManager
import com.zhy.mediaplayerserver.playermanager.notification.DefaultNotification
import com.zhy.mediaplayerserver.test.MusicItem
import com.zhy.mediaplayerserver.test.PlayItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MediaPlayerService.init(
            this,
            DefaultNotification.Builder(this)
                .build()
        )
        MusicManager.playlist(
            mutableListOf(
                MusicItem(
                    PlayItem(
                        1,
                        "https://lcache.qingting.fm/cache/20210204/386/386_20210204_003000_010000_24_0.aac",
                        "午夜凶铃",
                        "dufsud",
                        "http://imagev2.xmcdn.com/group83/M01/D3/F3/wKg5HV856H3SX_90AAcwXS7cY9M508.jpg!strip=1&quality=7&magick=webp&op_type=5&upload_type=album&name=mobile_large&device_type=ios"
                    )
                )
                ,
                MusicItem(
                    PlayItem(
                        2,
                        "https://lcache.qingting.fm/cache/20210204/386/386_20210204_003000_010000_24_0.aac",
                        "qq故事巴拉",
                        "热热热太秃头",
                        "http://imagev2.xmcdn.com/group23/M02/5D/93/wKgJL1g0SKzTRwU9AAG1LGWoXWg235.jpg!strip=1&quality=7&magick=webp&op_type=5&upload_type=album&name=mobile_large&device_type=ios"
                    )
                )
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}