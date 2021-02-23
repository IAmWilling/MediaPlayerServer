package com.zhy.mediaplayer_exo.playermanager

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.service.MediaForegroundService

/**
 * 音频播放服务
 *
 */
object MediaPlayerService : Application.ActivityLifecycleCallbacks {
    private lateinit var mNotification: Notification
    private lateinit var mContext: Context
    private var activityCreated = 0
    fun init(context: Application, notification: Notification) {
        this.mNotification = notification
        this.mContext = context
        context.registerActivityLifecycleCallbacks(this)
    }

    /**
     * 停止播放服务
     */
    fun stop(context: Context) {
        MediaManager.getSimpleExoPlayer().stop()
        MediaManager.getSimpleExoPlayer().release()
        context.stopService(Intent(context, MediaForegroundService::class.java))
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityCreated++
        if (activityCreated == 1) {
            MediaManager.init(activity)
            val serviceIntent = Intent(mContext, MediaForegroundService::class.java).apply {
                action = "${mContext.packageName}.mediaplayer.service.action"
                putExtra(MediaForegroundService.EXTRA_NOTIFICATION_DATA, mNotification)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(serviceIntent)
            }else {
                mContext.startService(serviceIntent)
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }




}