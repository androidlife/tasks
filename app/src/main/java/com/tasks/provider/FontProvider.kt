package com.tasks.provider

import android.content.res.AssetManager
import android.graphics.Typeface

/**
 * Use a singleton of this class
 * as we can't make multiple instances of Typeface in an application
 * The reason for it is that, multiple instances of Typeface will consume
 * more memory. If this class is singleton, it's typeface instance like
 * fontAwesome will be single instance
 */
class FontProvider(asset: AssetManager) {
    companion object {
        const val FONT_AWESOME: String = "fonts/awesome_solid.ttf"
    }

    val fontAwesome: Typeface = Typeface.createFromAsset(asset, FONT_AWESOME)
}
