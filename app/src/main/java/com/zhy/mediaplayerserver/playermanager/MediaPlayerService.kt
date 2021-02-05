package com.zhy.mediaplayerserver.playermanager

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import com.zhy.mediaplayerserver.playermanager.manager.MusicManager
import com.zhy.mediaplayerserver.playermanager.service.MediaForegroundService

/**
 * 音频播放服务
 *
 */
object MediaPlayerService {
    fun init(context: Context, notification: Notification) {
        MusicManager.init(context)
        val serviceIntent = Intent(context, MediaForegroundService::class.java).apply {
            action = "${context.packageName}.mediaplayer.service.action"
            putExtra(MediaForegroundService.EXTRA_NOTIFICATION_DATA, notification)
        }
        context.startService(serviceIntent)
    }

    /**
     * 停止播放服务
     */
    fun stop(context: Context) {

        MusicManager.getSimpleExoPlayer().stop()
        MusicManager.getSimpleExoPlayer().release()

        context.stopService(Intent(context, MediaForegroundService::class.java))



    }

}