package com.zhy.mediaplayer_exo.playermanager

interface MediaProgressListener {
    //当前媒体进度
    fun onProgressChange(position: Long,duration:Long)
}