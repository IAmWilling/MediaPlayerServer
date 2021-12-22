package com.zhy.mediaplayerserver.test

import com.zhy.mediaplayer_exo.playermanager.PlaylistItem

class MusicItem(val item: PlayItem) : PlaylistItem {

    override val id: Long
        get() = item.id

    override
    val title: String?
        get() = item.title

    override
    val intro: String?
        get() = item.desc
    override val coverUrl: String?
        get() = item.cover

    override
    var mediaUrl: String? = null
        get() = item.url

    override
    val isdownload: Boolean
        get() = item.isdownload

    override
    val downloadFileUri: String?
        get() = item.downloadFileUri
}
