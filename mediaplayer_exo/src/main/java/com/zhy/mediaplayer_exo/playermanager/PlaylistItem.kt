package com.zhy.mediaplayer_exo.playermanager

import android.os.Handler
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DrmSessionEventListener
import com.google.android.exoplayer2.source.MediaPeriod
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.upstream.Allocator
import com.google.android.exoplayer2.upstream.TransferListener

/**
 * 播放item接口
 */
interface PlaylistItem {
    val id: Long
    val title: String?
    val intro: String?
    val coverUrl: String?
    val mediaUrl: String?
    val isdownload: Boolean
    val downloadFileUri: String?

}