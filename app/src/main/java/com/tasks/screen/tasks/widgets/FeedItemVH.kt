package com.tasks.screen.tasks.widgets

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tasks.R
import com.tasks.image.ImageLoadOptions
import com.tasks.image.ImageLoader
import com.tasks.model.EventType
import com.tasks.model.FeedItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_item_view.*

/**
 * Simple ViewHolder class for individual RowItem
 * of the RecyclerView
 */
class FeedItemVH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private val imageLoadOptions: ImageLoadOptions

    init {
        val resources = containerView.resources
        val imgWidth = resources.getDimensionPixelSize(R.dimen.img_width)
        val imgHeight = resources.getDimensionPixelSize(R.dimen.img_height)
        imageLoadOptions = ImageLoadOptions.Builder().setErrorDrawable(R.drawable.ic_placeholder_error)
                .setPlaceHolder(R.drawable.ic_placeholder)
                .setResizeWidthHeight(imgWidth, imgHeight)
                .build()
    }

    fun bind(feedItem: FeedItem) {
        tvTitle.text = feedItem.text
        ImageLoader.loadImage(feedItem.profile.profileUrl, ivProfile, imageLoadOptions)
        itvDate.text = getString(R.string.icon_calendar).plus("  ").plus(feedItem.createdAt)
        itvEvent.text = when (feedItem.event) {
            EventType.Assigned -> getEventString(R.string.icon_assigned, EventType.Assigned.name)
            EventType.Completed -> getEventString(R.string.icon_completed, EventType.Completed.name)
            EventType.Comment -> getEventString(R.string.icon_comment, EventType.Comment.name)
            else -> getEventString(R.string.icon_post, EventType.Post.name)
        }
    }

    private fun getString(stringId: Int): String {
        return containerView.resources.getString(stringId)
    }

    private fun getEventString(stringId: Int, eventName: String): String {
        return getString(stringId).plus("  ").plus(eventName)
    }
}