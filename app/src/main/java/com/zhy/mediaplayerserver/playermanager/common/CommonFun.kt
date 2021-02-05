package com.zhy.mediaplayerserver.playermanager.common

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition


fun getBitmap(
    context: Context,
    coverUrl: String,
    block: (bitmap: android.graphics.Bitmap) -> Unit
) {
    Glide.with(context)
        .asBitmap()
        .load(coverUrl)
        .into(object:SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                block(resource)
            }
        })
}