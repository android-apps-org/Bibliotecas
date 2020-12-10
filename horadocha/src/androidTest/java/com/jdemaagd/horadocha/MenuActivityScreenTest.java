package com.jdemaagd.horadocha;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/** Testing AdapterViews:
    onView() can handle most Views in our UI
    Espresso requires to use onData when dealing with AdapterView widgets
    AdapterViews such as ListView and GridView load data dynamically from an Adapter,
    only a subset of content may be loaded in current view hierarchy at a time
    So onView() may not be able to find necessary view

    Using onData() loads adapter item of interest onto screen before operating on it
 */
@RunWith(AndroidJUnit4.class)
public class MenuActivityScreenTest {

    public static final String TEA_NAME = "Green Tea";

    @Rule
    public ActivityScenarioRule<MenuActivity> mActivityTestRule =
            new ActivityScenarioRule<>(MenuActivity.class);

    @Test
    public void clickGridViewItem_OpensOrderActivity() {
        onData(anything())
                .inAdapterView(withId(R.id.tea_grid_view))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.tea_name_text_view))
                .check(matches(withText(TEA_NAME)));
    }
}