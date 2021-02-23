package com.zhy.mediaplayerserver.util

import java.util.*


// 转换成字符串的时间
val mFormatBuilder = StringBuffer()
val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

/**
 * 把毫秒转换成：20:30这里形式
 *
 * @param timeMs
 * @return
 */
fun stringForTime2(timeMs: Long): String? {
    val totalSeconds = timeMs / 1000
    val seconds = totalSeconds % 60
    var minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    if (hours > 0) {
        minutes += hours * 60
    }
    mFormatBuilder.setLength(0)
    return mFormatter.format("%02d:%02d", minutes, seconds).toString()
}