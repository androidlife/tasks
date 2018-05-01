package com.tasks.image


import com.tasks.R

/**
 */

class ImageLoadOptions private constructor() {
    var errorDrawableId: Int = 0
    var placeHolderDrawableId: Int = 0
    var resizeWidth: Int = 0
    var resizeHeight: Int = 0

    class Builder {
        private var errorDrawableId = R.drawable.ic_placeholder_error
        private var placeHolderDrawableId = R.drawable.ic_placeholder
        private var resizeWidth = 150
        private var resizeHeight = 150

        fun setErrorDrawable(errorDrawableId: Int): Builder {
            this.errorDrawableId = errorDrawableId
            return this
        }

        fun setPlaceHolder(placeHolderDrawableId: Int): Builder {
            this.placeHolderDrawableId = placeHolderDrawableId
            return this
        }

        fun setResizeWidthHeight(resizeWidth: Int, resizeHeight: Int): Builder {
            this.resizeHeight = resizeHeight
            this.resizeWidth = resizeWidth
            return this
        }

        fun build(): ImageLoadOptions {
            val imageLoadOptions = ImageLoadOptions()
            imageLoadOptions.errorDrawableId = errorDrawableId
            imageLoadOptions.placeHolderDrawableId = placeHolderDrawableId
            imageLoadOptions.resizeWidth = resizeWidth
            imageLoadOptions.resizeHeight = resizeHeight
            return imageLoadOptions
        }
    }
}
