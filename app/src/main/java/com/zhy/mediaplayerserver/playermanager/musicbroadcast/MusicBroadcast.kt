package com.zhy.mediaplayerserver.playermanager.musicbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zhy.mediaplayerserver.playermanager.MediaPlayerService
import com.zhy.mediaplayerserver.playermanager.common.getBitmap
import com.zhy.mediaplayerserver.playermanager.manager.MusicManager
import com.zhy.mediaplayerserver.playermanager.notification.DefaultNotification

class MusicBroadcast : BroadcastReceiver() {
    companion object {
        @JvmStatic
        val PENDINGINTENT_NO_READY_PLAY_CLICK =
            "com.simplemusic.musicbroadcast.action.noready.play.click"

        @JvmStatic
        val ACTION_MUSIC_BROADCASET_UPDATE = "com.simplemusic.musicbroadcast.update.action_._"

        @JvmStatic
        val EXTRA_ACTION = "com.simplemusic.musicbroadcast.data.action"

        @JvmStatic
        val PENDINGINTENT_PLAY_CLICK = "com.simplemusic.musicbroadcast.action.play.click"

        @JvmStatic
        val PENDINGINTENT_LAST_MUSIC_CLICK = "com.simplemusic.musicbroadcast.action.lastmusic.click"

        @JvmStatic
        val PENDINGINTENT_LAST_NEXT_MUSIC_CLICK =
            "com.simplemusic.musicbroadcast.action.nextmusic.click"

        @JvmStatic
        val PENDINGINTENT_READY_PLAY_CLICK =
            "com.simplemusic.musicbroadcast.action.ready.play.click"

        @JvmStatic
        val PENDINGINTENT_CLOSE_MUSIC_SERVICE =
            "com.simplemusic.musicbroadcast.action.close.play..service.click"
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_MUSIC_BROADCASET_UPDATE) {
                val data = it.getStringExtra(EXTRA_ACTION)
                when (data) {
                    PENDINGINTENT_PLAY_CLICK -> {
                        if (MusicManager.getSimpleExoPlayer().isPlaying)
                            MusicManager.getSimpleExoPlayer().pause()
                        else
                            MusicManager.getSimpleExoPlayer().play()
                        DefaultNotification.getInstance()
                            .update(
                                MusicManager.getSimpleExoPlayer().isPlaying,
                                MusicManager.getCurrentMediaTitle(),
                                MusicManager.getCurrentMediaDesc()
                            )
                    }
                    PENDINGINTENT_LAST_MUSIC_CLICK -> MusicManager.playLast()
                    PENDINGINTENT_LAST_NEXT_MUSIC_CLICK -> MusicManager.playNext()
                    PENDINGINTENT_READY_PLAY_CLICK -> {
                        getBitmap(context!!, MusicManager.getCurrentMediaCover()) { bitmap ->
                            MusicManager.getSimpleExoPlayer().play()
                            DefaultNotification.getInstance()
                                .update(
                                    true,
                                    MusicManager.getCurrentMediaTitle(),
                                    MusicManager.getCurrentMediaDesc(),
                                    bitmap
                                )
                        }

                    }
                    PENDINGINTENT_NO_READY_PLAY_CLICK -> DefaultNotification.getInstance()
                        .update(
                            false,
                            MusicManager.getCurrentMediaTitle(),
                            MusicManager.getCurrentMediaDesc()
                        )
                    PENDINGINTENT_CLOSE_MUSIC_SERVICE -> MediaPlayerService.stop(context!!)
                }
            }
        }
    }
}