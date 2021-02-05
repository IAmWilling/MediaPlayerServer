package com.zhy.mediaplayerserver.playermanager.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener

import com.google.android.exoplayer2.source.MediaSource
import com.zhy.mediaplayerserver.playermanager.PlaylistItem
import com.zhy.mediaplayerserver.playermanager.musicbroadcast.MusicBroadcast

object MusicManager : Player.EventListener {
    private lateinit var mContext: Context
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var playlistItemList = mutableListOf<PlaylistItem>()
    fun init(context: Context) {
        this.mContext = context
        simpleExoPlayer = SimpleExoPlayer.Builder(mContext).build()
        simpleExoPlayer.addListener(this)
    }

    //播放
    fun playlist(mutableList: MutableList<PlaylistItem>) {
        playlistItemList = mutableList
        val mediaItemList = mutableListOf<MediaItem>()
        mutableList.map {
            mediaItemList.add(
                MediaItem.Builder()
                    .setUri(Uri.parse(it.mediaUrl))
                    .setMediaId(it.id.toString())
                    .build()
            )
        }
        simpleExoPlayer.setMediaItems(mediaItemList)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    fun playNext() {
        if (simpleExoPlayer.hasNext()) {
            simpleExoPlayer.next()
        }
    }
    fun playLast(){
        if (simpleExoPlayer.hasPrevious()) {
            simpleExoPlayer.previous()
        }
    }

    fun getSimpleExoPlayer(): SimpleExoPlayer = simpleExoPlayer

    fun getCurrentMediaTitle(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].title ?: ""
    }

    fun getCurrentMediaDesc(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].intro ?: ""
    }
    fun getCurrentMediaCover(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].coverUrl ?: ""
    }


    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
    }



    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_READY -> mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(MusicBroadcast.EXTRA_ACTION, MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK)
            })
            else -> mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(MusicBroadcast.EXTRA_ACTION, MusicBroadcast.PENDINGINTENT_NO_READY_PLAY_CLICK)
            })
        }
    }


    override fun onPlayerError(error: ExoPlaybackException) {
    }


}