package com.zhy.mediaplayerserver.playermanager.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zhy.mediaplayerserver.R
import com.zhy.mediaplayerserver.playermanager.musicbroadcast.MusicBroadcast
import kotlin.math.abs

/**
 * 通知栏服务
 */
open class DefaultNotification {
    companion object {
        @JvmStatic
        private var INSTANCE: DefaultNotification? = null

        @JvmStatic
        fun getInstance(): DefaultNotification {
            if (INSTANCE == null) {
                synchronized(DefaultNotification::class.java) {
                    INSTANCE = DefaultNotification()
                }
            }
            return INSTANCE!!
        }

        @JvmStatic
        private var playPrimaryId: Int = 1

        @JvmStatic
        private var lastPrimaryId: Int = 2

        @JvmStatic
        private var nextPrimaryId: Int = 3

        @JvmStatic
        private var closePrimaryId = 4

    }

    lateinit var mContext: Context

    //默认通知栏布局
    var notificationLayout = R.layout.default_notification_layout
        set(value) {
            field = value
        }
    var smallIcon = R.mipmap.ic_launcher
        set(value) {
            field = value
        }
    var largeIcon: Bitmap? = null
        set(value) {
            field = value
        }
    var playState: Boolean = false
        set(value) {
            field = value
        }
    var notificTitle: String? = null
        set(value) {
            field = value
        }
    var notificIntro: String? = null
        set(value) {
            field = value
        }
    var notificCover: Bitmap? = null
        set(value) {
            field = value
        }

    /**
     * 启动通知栏
     */
    protected fun startNotification(context: Context): Notification {
        this.mContext = context

        //启动通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel("playerMedia", "音频播放")
            return createNotification("playerMedia")
        } else {
            return createNotification()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    protected fun createChannel(channelId: String, channelName: String) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.setSound(null, null)
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 创建通知栏
     */
    protected fun createNotification(channelId: String = "playerMedia"): Notification {
        val remoteViews = RemoteViews(mContext.packageName, notificationLayout)
        playPrimaryId++
        lastPrimaryId++
        nextPrimaryId++
        closePrimaryId++
        // 播放/暂停按钮点击
        val playerClickPendingIntent = PendingIntent.getBroadcast(
            mContext,
            playPrimaryId,
            Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(MusicBroadcast.EXTRA_ACTION, MusicBroadcast.PENDINGINTENT_PLAY_CLICK)
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //上一首
        val lastClickPendingIntengt = PendingIntent.getBroadcast(
            mContext,
            lastPrimaryId, Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(MusicBroadcast.EXTRA_ACTION, MusicBroadcast.PENDINGINTENT_LAST_MUSIC_CLICK)
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //下一首
        val nextClickPendingIntengt = PendingIntent.getBroadcast(
            mContext,
            nextPrimaryId, Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_LAST_NEXT_MUSIC_CLICK
                )
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val closeClickPendingIntent = PendingIntent.getBroadcast(
            mContext,
            closePrimaryId, Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_CLOSE_MUSIC_SERVICE
                )
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setOnClickPendingIntent(R.id.default_play, playerClickPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.default_last, lastClickPendingIntengt)
        remoteViews.setOnClickPendingIntent(R.id.default_next, nextClickPendingIntengt)
        remoteViews.setOnClickPendingIntent(R.id.default_notific_close, closeClickPendingIntent)
        remoteViews.setImageViewResource(
            R.id.default_play,
            if (playState) R.mipmap.pause_notification else R.mipmap.play_notification
        )
        remoteViews.setImageViewBitmap(R.id.default_song_cover_img, notificCover)
        remoteViews.setTextViewText(R.id.default_song_intro, notificIntro)
        remoteViews.setTextViewText(R.id.default_song_name, notificTitle)
        val notification = NotificationCompat.Builder(mContext, channelId)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setLargeIcon(largeIcon)
            .setSmallIcon(smallIcon)
            .setContent(remoteViews)
            .setOnlyAlertOnce(true)
            .setVibrate(null)
            .setSound(null)
            .setLights(0, 0, 0)
            .build()
        notification.flags =
            Notification.FLAG_NO_CLEAR
        return notification
    }

    /**
     * 更新播放状态
     */
    fun update(playState: Boolean, title: String, intro: String) {
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            10086, DefaultNotification.Builder(mContext)
                .setPlayState(playState)
                .setTitleText(title)
                .setIntroText(intro)
                .build()
        )
    }

    /**
     * 更新播放状态
     */
    fun update(playState: Boolean, title: String, intro: String, cover: Bitmap) {
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            10086, DefaultNotification.Builder(mContext)
                .setPlayState(playState)
                .setTitleText(title)
                .setIntroText(intro)
                .setMediaCover(cover)
                .build()
        )
    }


    class Builder(val context: Context) {
        var defaultNotification: DefaultNotification = DefaultNotification.getInstance()

        fun setSmallIcon(resid: Int): Builder {
            defaultNotification.smallIcon = resid
            return this
        }

        fun setRemoteViews(layoutId: Int): Builder {
            defaultNotification.notificationLayout = layoutId
            return this
        }

        fun setLargeIcon(icon: Bitmap?): Builder {
            defaultNotification.largeIcon = icon
            return this
        }

        fun setPlayState(state: Boolean): Builder {
            defaultNotification.playState = state
            return this
        }

        fun setTitleText(title: String): Builder {
            defaultNotification.notificTitle = title
            return this
        }

        fun setIntroText(intro: String): Builder {
            defaultNotification.notificIntro = intro
            return this
        }

        fun setMediaCover(bitmap: Bitmap): Builder {
            defaultNotification.notificCover = bitmap
            return this
        }

        fun build(): Notification {
            return defaultNotification.startNotification(context)
        }


    }


}