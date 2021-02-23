package com.zhy.mediaplayerserver

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

//        val className = MediaForegroundService.javaClass.name
//        if(MediaForegroundService.isServiceWork(this,className.substring(0,className.lastIndexOf("$")))) {
//            println("正在运行")
//        }else {
//            println("没有运行")
//
//        }
        MediaPlayerService.init(
            this,
            DefaultNotification.Builder(this)
                .setIntroText("详情信息")
                .setTitleText("FM收音机广播")
                .setSmallIcon(R.mipmap.icon)
                .setPlayImage(R.mipmap.play)
                .setPauseImage(R.mipmap.pause)
                .setNextImage(R.mipmap.exe_next)
                .setLastImage(R.mipmap.exe_last)
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