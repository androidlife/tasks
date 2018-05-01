package com.tasks.screen.tasks.widgets

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.tasks.provider.Injection

/**
 *  A custom text view which contains
 *  a custom typeface here AwesomeFont,so that
 *  we can use necessary font codes to generate icons
 */
class IconicTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        typeface = Injection.getFontProvider(context)?.fontAwesome
    }

}
