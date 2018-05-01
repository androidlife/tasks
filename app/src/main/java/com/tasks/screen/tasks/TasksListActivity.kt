package com.tasks.screen.tasks

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.tasks.R
import com.tasks.model.FeedItem
import com.tasks.provider.Injection
import com.tasks.screen.tasks.widgets.FeedsAdapter
import kotlinx.android.synthetic.main.activity_tasklist.*

class TasksListActivity : AppCompatActivity(), ListContract.View {


    private var viewState = ListContract.View.ViewState.Empty
    private var feeds: List<FeedItem> = ArrayList(0)

    private lateinit var presenter: ListContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasklist)

        presenter = ListPresenter(this, Injection.getListModel())
        initViews()
    }

    override fun onPause() {
        super.onPause()
        presenter.start(false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start(true)
    }

    private fun initViews() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvTasks.layoutManager = linearLayoutManager

        val divider = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider))
        rvTasks.addItemDecoration(divider)

        refreshLayout.isEnabled = false

        tvInfo.setOnClickListener({ _ -> presenter.retry() })
    }

    //ListContract.View implementation
    override fun getViewState(): ListContract.View.ViewState {
        return viewState
    }

    override fun setViewState(viewState: ListContract.View.ViewState) {
        this.viewState = viewState
    }

    override fun showError(show: Boolean) {
        when (show) {
            true -> tvInfo.visibility = android.view.View.VISIBLE
            else -> tvInfo.visibility = android.view.View.GONE
        }
    }

    override fun showProgress(show: Boolean) {
        refreshLayout.isRefreshing = show

    }

    override fun showInfo(infoMsg: String) {
        Toast.makeText(this, infoMsg, Toast.LENGTH_SHORT).show()
    }

    override fun isConnectedToNetwork(): Boolean {
        return com.tasks.utils.isConnectedToNetwork(this)
    }

    override fun setData(listing: List<FeedItem>) {
        this.feeds = listing
        rvTasks.adapter = FeedsAdapter(this.feeds)
    }

    override fun getFetchedData(): List<FeedItem> {
        return feeds
    }


}
