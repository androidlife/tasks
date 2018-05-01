package com.tasks.screen.tasks.widgets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tasks.R
import com.tasks.model.FeedItem

class FeedsAdapter(val feeds: List<FeedItem>) : RecyclerView.Adapter<FeedItemVH>() {
    override fun getItemCount(): Int {
        return feeds.size
    }

    override fun onBindViewHolder(holder: FeedItemVH?, position: Int) {
        holder?.bind(feeds[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedItemVH {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.feed_item_view, parent, false)
        return FeedItemVH(view)
    }

}