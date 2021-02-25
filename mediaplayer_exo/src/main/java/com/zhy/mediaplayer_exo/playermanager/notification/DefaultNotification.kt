package com.zhy.mediaplayer_exo.playermanager.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.zhy.mediaplayer_exo.R
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerService
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.musicbroadcast.MusicBroadcast

/**
 * Created by zhy
 * Date 2021/2/25
 *
 * 默认通知栏
 * 默认渠道ID：playerMedia 渠道名称：音频服务
 * 播放服务控制视图
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

        @JvmStatic
        private var contentPrimaryId = 5

    }

    lateinit var mContext: Context

    //默认通知栏布局
    var notificationLayout = R.layout.default_notification_layout
        set(value) {
            field = value
        }

    //通知栏显示的小图标
    var smallIcon = R.mipmap.ic_launcher
        set(value) {
            field = value
        }

    //通知栏显示的大图标
    var largeIcon: Bitmap? = null
        set(value) {
            field = value
        }

    //播放状态
    var playState: Boolean = false
        set(value) {
            field = value
        }

    //通知栏自定义标题
    var notificTitle: String? = null
        set(value) {
            field = value
        }

    //通知栏自定义简介
    var notificIntro: String? = null
        set(value) {
            field = value
        }

    //通知栏自定义封面
    var notificCover: Bitmap? = null
        set(value) {
            field = value
        }

    //播放image
    var playImage: Int = -1
        set(value) {
            field = value
        }

    //暂停image
    var pauseImage: Int = -1
        set(value) {
            field = value
        }

    //上一个image
    var nextImage: Int = -1
        set(value) {
            field = value
        }

    //下一个image
    var lastImage: Int = -1
        set(value) {
            field = value
        }

    //适配小通知栏
    var smallContentRemoteViews: Int = R.layout.default_small_media_exopalyer_notification
        set(value) {
            field = value
        }

    //设置传入参数参数
    var contentPrams: () -> Bundle = { Bundle() }

    //需要启动的activity
    var startIntentArray = arrayOf<Class<*>>()

    //传入的bundle key
    var extraBundleName = ""

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

    /**
     * 创建渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    protected fun createChannel(channelId: String, channelName: String) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN)
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
        val smallRemoteViews = RemoteViews(mContext.packageName, smallContentRemoteViews)
        playPrimaryId++
        lastPrimaryId++
        nextPrimaryId++
        closePrimaryId++
        contentPrimaryId++
        val contentClickPendingIntent =
            if (startIntentArray.isEmpty()) null else {
                val intentArray = mutableListOf<Intent>()
                println("contentClickPendingIntent ${startIntentArray.size}")
                startIntentArray.forEachIndexed { index, clazz ->
                    if (index != startIntentArray.size - 1) {
                        intentArray.add(
                            Intent(
                                mContext,
                                clazz
                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )

                    } else {
                        println("contentClickPendingIntent ${clazz.name}")
                        intentArray.add(
                            Intent(mContext, clazz).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra(
                                    extraBundleName,
                                    if (MediaManager.isInit) contentPrams() else Bundle()
                                )
                        )
                    }

                }
                PendingIntent.getActivities(
                    mContext,
                    contentPrimaryId,
                    intentArray.toTypedArray(),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

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
            if (playState) if (pauseImage == -1) R.mipmap.pause_notification else pauseImage else if (playImage == -1) R.mipmap.play_notification else playImage
        )
        remoteViews.setImageViewResource(
            R.id.default_next,
            if (nextImage == -1) R.mipmap.next_notification_icon else nextImage
        )

        remoteViews.setImageViewResource(
            R.id.default_last,
            if (lastImage == -1) R.mipmap.last_notification_icon else lastImage
        )
        remoteViews.setImageViewBitmap(R.id.default_song_cover_img, notificCover)
        remoteViews.setTextViewText(R.id.default_song_intro, notificIntro)
        remoteViews.setTextViewText(R.id.default_song_name, notificTitle)

        smallRemoteViews.apply {
            setOnClickPendingIntent(R.id.default_play, playerClickPendingIntent)
            setOnClickPendingIntent(R.id.default_last, lastClickPendingIntengt)
            setOnClickPendingIntent(R.id.default_next, nextClickPendingIntengt)
            setOnClickPendingIntent(R.id.default_notific_close, closeClickPendingIntent)
            setImageViewResource(
                R.id.default_play,
                if (playState) if (pauseImage == -1) R.mipmap.pause_notification else pauseImage else if (playImage == -1) R.mipmap.play_notification else playImage
            )
            setImageViewResource(
                R.id.default_next,
                if (nextImage == -1) R.mipmap.next_notification_icon else nextImage
            )

            setImageViewResource(
                R.id.default_last,
                if (lastImage == -1) R.mipmap.last_notification_icon else lastImage
            )
            setImageViewBitmap(R.id.default_song_cover_img, notificCover)
            setTextViewText(R.id.default_song_intro, notificIntro)
            setTextViewText(R.id.default_song_name, notificTitle)
        }

        val notification = NotificationCompat.Builder(mContext, channelId)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setLargeIcon(largeIcon)
            .setSmallIcon(smallIcon)
            .setContent(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setCustomContentView(smallRemoteViews)
            .setContentIntent(contentClickPendingIntent)
            .setOnlyAlertOnce(true)
            .setVibrate(null)
            .setSound(null)
            .setLights(0, 0, 0)
            .build()
        notification.flags = Notification.FLAG_FOREGROUND_SERVICE

        return notification
    }

    /**
     * 更新播放状态
     */
    fun update(playState: Boolean, title: String, intro: String) {
        println("update------ 更新播放状态 ${title}")
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            MediaPlayerService.notificationId, DefaultNotification.Builder(mContext)
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
        println("update------ 更新bitmap ${title}")
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            MediaPlayerService.notificationId, DefaultNotification.Builder(mContext)
                .setPlayState(playState)
                .setTitleText(title)
                .setIntroText(intro)
                .setMediaCover(cover)
                .build()
        )
    }

    /**
     * 服务构建
     */
    class Builder(val context: Context) {
        var defaultNotification: DefaultNotification = DefaultNotification.getInstance()

        /**
         * 设置通知栏应用图标
         */
        fun setSmallIcon(resid: Int): Builder {
            defaultNotification.smallIcon = resid
            return this
        }

        /**
         * 设置通知栏自定义布局
         * 不设置 则 默认是sdk自带布局
         */
        fun setRemoteViews(layoutId: Int): Builder {
            defaultNotification.notificationLayout = layoutId
            return this
        }

        /**
         * 设置图标
         */
        fun setLargeIcon(icon: Bitmap?): Builder {
            defaultNotification.largeIcon = icon
            return this
        }

        /**
         * 设置播放状态
         * 非必要初始化不需要设置
         */
        fun setPlayState(state: Boolean): Builder {
            defaultNotification.playState = state
            return this
        }

        /**
         * 设置通知栏音频标题
         * 初始化可设置初始标题
         */
        fun setTitleText(title: String): Builder {
            defaultNotification.notificTitle = title
            return this
        }

        /**
         * 设置通知栏布局详情
         * 初始化可设置初始详情
         */
        fun setIntroText(intro: String): Builder {
            defaultNotification.notificIntro = intro
            return this
        }

        /**
         * 设置封面
         */
        fun setMediaCover(bitmap: Bitmap): Builder {
            defaultNotification.notificCover = bitmap
            return this
        }

        /**
         * 设置播放状态时的图片
         */
        fun setPlayImage(resid: Int): Builder {
            defaultNotification.playImage = resid
            return this
        }

        /**
         * 设置暂停状态时的图片
         */
        fun setPauseImage(resid: Int): Builder {
            defaultNotification.pauseImage = resid
            return this
        }

        /**
         * 设置上一曲图片
         */
        fun setLastImage(resid: Int): Builder {
            defaultNotification.lastImage = resid
            return this
        }
        /**
         * 设置下一曲图片
         */
        fun setNextImage(resid: Int): Builder {
            defaultNotification.nextImage = resid
            return this
        }

        /**
         * 设置通知栏适配小型布局
         * 有些手机需要设置比如小米，总是初始为小布局
         * 锁屏界面需要的话 可以设置
         */
        fun setSmallRemoteViews(layoutId: Int): Builder {
            defaultNotification.smallContentRemoteViews = layoutId
            return this
        }

        /**
         * 设置通知栏整体点击之后需要跳转的activity组
         * 一般最后一个activity是你需要最终跳转到activity
         * 第一个一般都是MainActivity 看需求
         */
        fun setStartActivityClassArray(array: Array<Class<*>>): Builder {
            defaultNotification.startIntentArray = array
            return this
        }

        /**
         * 跳转之后需要携带的一些数据可以自己设置
         * 但这些数据最好是全局都可以拿到的，比如在MediaManager可以拿到的任何数据等
         */
        fun setStartActivityBundle(extraName: String, block: () -> Bundle): Builder {
            defaultNotification.extraBundleName = extraName
            defaultNotification.contentPrams = block
            return this
        }

        /**
         * 打包
         */
        fun build(): Notification {
            return defaultNotification.startNotification(context)
        }

    }


}