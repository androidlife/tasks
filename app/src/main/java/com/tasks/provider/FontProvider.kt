package com.tasks.provider

import android.content.res.AssetManager
import android.graphics.Typeface

class FontProvider(asset: AssetManager) {
    companion object {
        const val FONT_AWESOME: String = "fonts/awesome_solid.ttf"
    }

    val fontAwesome: Typeface = Typeface.createFromAsset(asset, FONT_AWESOME)
}
