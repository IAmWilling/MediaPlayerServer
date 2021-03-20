package com.zhy.mediaplayerserver

import android.app.Activity
import android.app.Application
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerService
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.notification.DefaultNotification
import com.zhy.mediaplayer_exo.playermanager.service.MediaForegroundService

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MediaPlayerService.init(
            this,
            10,
            DefaultNotification.Builder(this)
                .setIntroText("详情信息")
                .setTitleText("FM收音机广播")
                .setSmallIcon(R.mipmap.icon)
                .setStartActivityClassArray(
                    arrayOf(
                        MainActivity::class.java,
                        DetailActivity::class.java
                    )
                )
                .setStartActivityBundle("qq1455841095") {
                    Bundle().apply {
                        putString("media_title", MediaManager.getCurrentMediaTitle())
                    }
                }
                .setMediaCover(BitmapFactory.decodeResource(resources, R.mipmap.icon))
                .build()
        )

    }

}