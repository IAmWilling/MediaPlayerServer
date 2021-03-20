package com.zhy.mediaplayerserver

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.exoplayer2.Player
import com.zhy.mediaplayer_exo.playermanager.*
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayerserver.test.MusicItem
import com.zhy.mediaplayerserver.test.PlayItem
import com.zhy.mediaplayerserver.util.stringForTime2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MediaProgressListener, MediaPlayStateListaner,
    MediaSwitchTrackChange {
    private var isTouch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var a = android.os.Process.myPid()
        setContentView(R.layout.activity_main)
        MediaManager.addProgressListener(this)
        MediaManager.addMediaPlayerStateListener(this)
        MediaManager.addMediaSwitchChange(this)
        if (MediaManager.getSimpleExoPlayer().currentMediaItem == null) {
            MediaManager.playlist(
                mutableListOf(
                    MusicItem(
                        PlayItem(
                            1,
                            "https://lcache.qingting.fm/cache/20210223/388/388_20210223_060000_070000_24_0.aac",
                            "dfgsdfghfsghdfjdgj",
                            "特尔特容易",
                            "http://imagev2.xmcdn.com/group83/M01/D3/F3/wKg5HV856H3SX_90AAcwXS7cY9M508.jpg!strip=1&quality=7&magick=webp&op_type=5&upload_type=album&name=mobile_large&device_type=ios"
                        )
                    )
                    ,
                    MusicItem(
                        PlayItem(
                            2,
                            "https://lcache.qingting.fm/cache/20210204/386/386_20210204_003000_010000_24_0.aac",
                            "qq故事巴拉",
                            "热热热太秃头",
                            "http://imagev2.xmcdn.com/group23/M02/5D/93/wKgJL1g0SKzTRwU9AAG1LGWoXWg235.jpg!strip=1&quality=7&magick=webp&op_type=5&upload_type=album&name=mobile_large&device_type=ios"
                        )
                    )
                )
            )
        }


        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val f = seek_bar.progress.toFloat() / 100.0f
                println("onStopTrackingTouch ${MediaManager.getCurrentDuration() * f}")
                MediaManager.getSimpleExoPlayer()
                    .seekTo((MediaManager.getCurrentDuration() * f).toLong())
                isTouch = false
            }
        })
        media_play.setOnClickListener { MediaManager.playOrPause() }
        media_last.setOnClickListener { MediaManager.playLast() }
        media_next.setOnClickListener { MediaManager.playNext() }
    }

    override fun onResume() {
        super.onResume()
        media_name.text = MediaManager.getCurrentMediaTitle()
        Glide.with(this).load(MediaManager.getCurrentMediaCover()).into(media_cover)
        if (MediaManager.isPlaying()) media_play.text = "暂停" else media_play.text = "播放"
        current_time.text = stringForTime2(MediaManager.getSimpleExoPlayer().currentPosition)
        total_time.text =
            stringForTime2(if (MediaManager.getSimpleExoPlayer().duration > 0) MediaManager.getSimpleExoPlayer().duration else 0)
    }

    override fun onStart() {
        super.onStart()
        println("activity_li onStart")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }


    override fun onProgressChange(position: Long, duration: Long) {
        current_time.text = stringForTime2(position)
        println("onProgressChange ${((position.toFloat() / duration.toFloat()) * 100)} pos = ${position} dur = $duration")
        if (!isTouch) {
            seek_bar.progress = ((position.toFloat() / duration.toFloat()) * 100).toInt()
        }
        total_time.text = stringForTime2(duration)
    }

    override fun onMediaPlayState(state: Int) {
        when (state) {
            Player.STATE_READY -> {
                media_play.text = "暂停"
            }
            else -> {
                media_play.text = "播放"
            }
        }
    }

    override fun onTracksChange(playlistItem: PlaylistItem) {
        media_name.text = playlistItem.title
        //取得时间
        val updateTime = System.currentTimeMillis().toString()
        if (!isDestroy((this as Activity?)!!)) {
            Glide.with(this).load(playlistItem.coverUrl)
                .signature(ObjectKey(updateTime))
                .transition(DrawableTransitionOptions.withCrossFade(300)).into(media_cover)
        }

    }

    /**
     * 判断Activity是否Destroy
     * @param activity
     * @return
     */
    fun isDestroy(mActivity: Activity): Boolean {
        return mActivity == null || mActivity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(!hasFocus) {
            println("通知栏下拉 通知栏下拉")
        }
    }



}