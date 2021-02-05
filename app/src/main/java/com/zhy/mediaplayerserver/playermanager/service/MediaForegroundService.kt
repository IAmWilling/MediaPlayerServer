package com.zhy.mediaplayerserver.playermanager.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.zhy.mediaplayerserver.playermanager.musicbroadcast.MusicBroadcast

class MediaForegroundService : Service() {
    companion object {
        @JvmStatic
        val EXTRA_NOTIFICATION_DATA = "EXTRA_NOTIFICATION_DATA"
        @JvmStatic
        val musicBroadcast = MusicBroadcast()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            getParcelableExtra<Notification>(EXTRA_NOTIFICATION_DATA)?.apply {
                println("yumi EXTRA_NOTIFICATION_DATA")
                startForeground(10086, this)
                registerReceiver(musicBroadcast, IntentFilter(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE))
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MBinder()
    }

    inner class MBinder : Binder() {

    }


    override fun onDestroy() {
        //移除通知栏
        stopForeground(true)
        unregisterReceiver(musicBroadcast)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(10086)
        super.onDestroy()
    }
}