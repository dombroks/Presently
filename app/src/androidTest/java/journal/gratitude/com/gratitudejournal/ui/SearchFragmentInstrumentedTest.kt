package journal.gratitude.com.gratitudejournal.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import journal.gratitude.com.gratitudejournal.R
import journal.gratitude.com.gratitudejournal.di.DaggerTestApplicationRule
import journal.gratitude.com.gratitudejournal.repository.EntryRepository
import journal.gratitude.com.gratitudejournal.testUtils.RecyclerViewItemCountAssertion.Companion.withItemCount
import journal.gratitude.com.gratitudejournal.testUtils.waitFor
import journal.gratitude.com.gratitudejournal.ui.search.SearchFragment
import journal.gratitude.com.gratitudejournal.ui.timeline.TimelineFragment
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFragmentInstrumentedTest {

    private lateinit var repository: EntryRepository

    @get:Rule
    val rule = DaggerTestApplicationRule()

    @Before
    fun setupDaggerComponent() {
        repository = rule.component.entryRepository
    }

    @Test
    fun search_pressBackButton_navigateUp() {
        launchFragmentInContainer<TimelineFragment>(
            themeResId = R.style.Base_AppTheme
        )

        onView(withId(R.id.search_icon)).perform(click())

        onView(withId(R.id.back_icon)).perform(click())

        onView(withId(R.id.container)).check(matches(isDisplayed()))
    }

    @Test
    fun search_showsResults() {
        launchFragmentInContainer<SearchFragment>(
            themeResId = R.style.Base_AppTheme
        )

        onView(withId(R.id.search_text)).perform(
            typeText("query!")
        )

        //wait for debounce to perform search
        onView(isRoot()).perform(waitFor(400))

        onView(withId(R.id.no_results_icon)).check(ViewAssertions.matches(not(isDisplayed())))
        onView(withId(R.id.search_results)).check(withItemCount(2))
    }

    @Test
    fun search_doesntSearchEmptyStrings() {
        launchFragmentInContainer<SearchFragment>(
            themeResId = R.style.Base_AppTheme
        )

        onView(withId(R.id.search_text)).perform(
            typeText("")
        )

        onView(withId(R.id.search_results)).check(withItemCount(0))
    }
}
