package com.zhy.mediaplayer_exo.playermanager.manager

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.zhy.mediaplayer_exo.playermanager.*
import com.zhy.mediaplayer_exo.playermanager.musicbroadcast.MusicBroadcast


object MediaManager : Player.EventListener {
    private lateinit var mContext: Context
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var playlistItemList = mutableListOf<PlaylistItem>()
    private var playlistItemBitmap = hashMapOf<String, Bitmap>()
    private val infProgressList = mutableListOf<MediaProgressListener>()
    private val infErrorListenerList = mutableListOf<MediaErrorListener>()
    private val infMediaSwitchTrackChangeListenerList = mutableListOf<MediaSwitchTrackChange>()
    private val infMediaPlayerStateListenerList = mutableListOf<MediaPlayStateListaner>()
    var isInit = false
    fun init(context: Context) {
        this.mContext = context
        //设置固定码率 比特率 某些音频格式文件，在seekTo的时候无法找到对应的节点导致无法跳播，此代码可以解决这个问题
        val extractorsFactory =
            DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()
        simpleExoPlayer =
            SimpleExoPlayer.Builder(mContext, extractorsFactory)
                .setAudioAttributes(audioAttributes, true)
                .setLoadControl(DefaultMyControl())
                .build()
        simpleExoPlayer.addListener(this)
        isInit = true
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
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        infMediaSwitchTrackChangeListenerList.map {
            it.onTracksChange(playlistItemList[0])
        }
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    fun invokeProgressListenerList(current: Long) {
        infProgressList.map {
            it.onProgressChange(current, simpleExoPlayer.duration)
        }
    }

    fun addProgressListener(mediaProgressListener: MediaProgressListener) {
        infProgressList.add(mediaProgressListener)
    }

    fun addMediaPlayerStateListener(mediaPlayStateListaner: MediaPlayStateListaner) {
        infMediaPlayerStateListenerList.add(mediaPlayStateListaner)
    }

    fun addMediaErrorListener(mediaErrorListener: MediaErrorListener) {
        infErrorListenerList.add(mediaErrorListener)
    }

    fun removeProgressListener(mediaProgressListener: MediaProgressListener) {
        infProgressList.remove(mediaProgressListener)
    }

    fun addMediaSwitchChange(mediaSwitchTrackChange: MediaSwitchTrackChange) {
        infMediaSwitchTrackChangeListenerList.add(mediaSwitchTrackChange)
    }


    fun playNext(): Boolean {
        var f = false
        f = simpleExoPlayer.hasNext()
        if (f) {
            simpleExoPlayer.next()
        }
        return f
    }

    fun playLast() {
        if (simpleExoPlayer.hasPrevious()) {
            simpleExoPlayer.previous()
        }
    }

    fun playOrPause() {
        if (simpleExoPlayer.isPlaying) {
            simpleExoPlayer.pause()
        } else {
            simpleExoPlayer.play()
        }
    }

    fun isPlaying() = simpleExoPlayer.isPlaying

    fun getCacheBitmap(): Bitmap? = playlistItemBitmap[currentId()]

    fun setCacheBitmap(bitmap: Bitmap) {
        currentId()?.let {
            playlistItemBitmap[it] = bitmap
        }
    }

    fun currentId() = simpleExoPlayer.currentMediaItem?.mediaId
    fun getSimpleExoPlayer(): SimpleExoPlayer = simpleExoPlayer
    fun getCurrentDuration() = simpleExoPlayer.duration
    fun getCurrentMediaTitle(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].title ?: ""
    }

    fun getCurrentMediaDesc(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].intro ?: ""
    }

    fun getCurrentMediaCover(): String {
        return playlistItemList[simpleExoPlayer.currentWindowIndex].coverUrl ?: ""
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK || reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            //已自动切换到下一个
            val list = playlistItemList.filter {
                it.id.toString() == mediaItem?.mediaId
            }
            if (!list.isNullOrEmpty()) {
                infMediaSwitchTrackChangeListenerList.map {
                    it.onTracksChange(list[0])
                }
            }
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                )
            })

        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        infMediaPlayerStateListenerList.map {
            if (isPlaying) {
                it.onMediaPlayState(Player.STATE_READY)
            } else {
                it.onMediaPlayState(Player.STATE_ENDED)
            }
        }
        if (isPlaying) {
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                )
            })
        } else {
            mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_NO_READY_PLAY_CLICK
                )
            })
        }
    }

    override fun onPlaybackStateChanged(state: Int) {
        when (state) {
            Player.STATE_READY -> {
                mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                    putExtra(
                        MusicBroadcast.EXTRA_ACTION,
                        MusicBroadcast.PENDINGINTENT_READY_PLAY_CLICK
                    )
                })
            }

            else -> mContext.sendBroadcast(Intent(MusicBroadcast.ACTION_MUSIC_BROADCASET_UPDATE).apply {
                putExtra(
                    MusicBroadcast.EXTRA_ACTION,
                    MusicBroadcast.PENDINGINTENT_NO_READY_PLAY_CLICK
                )
            })
        }
        infMediaPlayerStateListenerList.map {
            it.onMediaPlayState(state)
        }
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {
        super.onTracksChanged(trackGroups, trackSelections)
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        error.printStackTrace()

        if (playNext()) {
            simpleExoPlayer.prepare()
            simpleExoPlayer.play()
        }

        infErrorListenerList.map {
            it.onMediaError()
        }
    }

    override fun onPositionDiscontinuity(reason: Int) {
        super.onPositionDiscontinuity(reason)

    }


}