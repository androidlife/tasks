package com.tasks

import CustomViewActions.getTotalItemsFromRecyclerAdapter
import CustomViewActions.recyclerScrollTo
import CustomViewMatcher.isSwipeRefreshing
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingPolicies
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.tasks.screen.tasks.TasksListActivity
import com.tasks.utils.CustomIdlingResource
import com.tasks.utils.isConnectedToNetwork
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class TaskListActivityTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<TasksListActivity>(TasksListActivity::class.java)

    private val TIMEOUT_ESPRESSO = 60L
    private val TIMEOUT_FETCH = 4L
    private val TIMEOUT_UNIT = TimeUnit.SECONDS

    private var idlingResource: CustomIdlingResource? = null


    @Before
    fun setUp() {
        IdlingPolicies.setMasterPolicyTimeout(TIMEOUT_ESPRESSO, TIMEOUT_UNIT)
    }

    @After
    fun onTestComplete() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    @Test
    fun fetchTest() {
        if (isConnectedToNetwork(activityTestRule.activity)) {
            val totalItems = getTotalFetchedItems()
            if (totalItems > 0) {
                Espresso.onView(ViewMatchers.withId(R.id.rvTasks)).perform(recyclerScrollTo(totalItems - 1))
            }
        }
    }

    private fun getTotalFetchedItems(): Int {
        if (isConnectedToNetwork(activityTestRule.activity)) {
            Espresso.onView(ViewMatchers.withId(R.id.refreshLayout)).check(ViewAssertions.matches(isSwipeRefreshing()))
            idlingResource = CustomIdlingResource()
            IdlingRegistry.getInstance().register(idlingResource)
            idlingResource?.startCountdown(TIMEOUT_FETCH, TIMEOUT_UNIT)
            return getTotalItemsFromRecyclerAdapter(R.id.rvTasks)
        }
        return -1

    }


}
