package com.zhy.mediaplayerserver.test

data class PlayItem(
    val id: Long,
    val url: String,
    val title: String,
    val desc: String,
    val cover: String,
    val isdownload: Boolean = false,
    val downloadFileUri: String = ""
) {
}