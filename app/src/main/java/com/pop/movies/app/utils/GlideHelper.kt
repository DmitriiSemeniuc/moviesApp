package com.pop.movies.app.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pop.movies.app.base.App

object GlideHelper {

    fun urlToImage(
        imageView: ImageView,
        imageUrl: String?,
        placeHolder: Int,
        onLoadFailed: () -> Unit = {},
        onResourceReady: () -> Unit = {}
    ) {
        Glide.with(App.getAppContext())
            .load(imageUrl)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onResourceReady()
                    return false
                }

            })
            .apply(getGlideRequestOptions().error(placeHolder))
            .into(imageView)
    }

    fun urlToImage(
        imageView: ImageView,
        path: String?
    ) {
        Glide.with(App.getAppContext())
            .load(path)
            .apply(
                getGlideRequestOptions()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    private fun getGlideRequestOptions() : RequestOptions {
        return RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.IMMEDIATE)
    }
}