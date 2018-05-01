package com.tasks.image

import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


/**
 */

object ImageLoader {
    var requestOptions: RequestOptions? = null

    fun loadImage(url: String, imageView: ImageView,
                  imageLoadOptions: ImageLoadOptions) {
        applyRequestOptions(imageLoadOptions)
        Glide.with(imageView.context).load(url).apply(requestOptions!!).into(imageView)
    }

    // since there is only image items in recycler view or only one type of image item
    // to be loaded, we are creating single requestOptions
    // this can later be customized to handle multiple type of image load
    private fun applyRequestOptions(imageLoadOptions: ImageLoadOptions) {
        if (requestOptions == null) {
            requestOptions = RequestOptions().error(imageLoadOptions.errorDrawableId)
                    .placeholder(imageLoadOptions.placeHolderDrawableId)
                    .override(imageLoadOptions.resizeWidth, imageLoadOptions.resizeHeight)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
        }
    }
}
