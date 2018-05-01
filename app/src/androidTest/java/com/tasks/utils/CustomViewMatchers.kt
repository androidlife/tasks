
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object CustomViewMatcher {

    fun isSwipeRefreshing(): Matcher<View> = IsSwipeRefreshingMatcher()
    private class IsSwipeRefreshingMatcher : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Checking whether the swipe refresh layout is refreshing")
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item is SwipeRefreshLayout) {
                return item.visibility == View.VISIBLE
            }
            return false
        }

    }
}