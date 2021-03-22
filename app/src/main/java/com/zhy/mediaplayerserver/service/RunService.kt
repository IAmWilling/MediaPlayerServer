package com.zhy.mediaplayerserver.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class RunService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(6, Notification())
        return Service.START_STICKY;
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}