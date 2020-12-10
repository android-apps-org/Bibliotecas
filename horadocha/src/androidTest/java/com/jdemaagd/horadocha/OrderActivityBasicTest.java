package com.jdemaagd.horadocha;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

// Testing Basic Views:
@RunWith(AndroidJUnit4.class)
public class OrderActivityBasicTest {

    // Note: replaces deprecated ActivityTestRule
    //       launches given activity before test starts and closes after test
    @Rule
    public ActivityScenarioRule<OrderActivity> mActivityTestRule =
            new ActivityScenarioRule<>(OrderActivity.class);

    @Test
    public void clickDecrementButton_ChangesQuantityAndCost() {

        onView((withId(R.id.quantity_text_view)))
                .check(matches(withText("0")));

        onView((withId(R.id.decrement_button)))
                .perform(click());

        onView(withId(R.id.quantity_text_view))
                .check(matches(withText("0")));

        onView(withId(R.id.cost_text_view))
                .check(matches(withText("$0.00")));
    }

    @Test
    public void clickIncrementButton_ChangesQuantityAndCost() {

        onView((withId(R.id.quantity_text_view)))
                .check(matches(withText("0")));

        onView((withId(R.id.increment_button)))
                .perform(click());

        onView(withId(R.id.quantity_text_view))
                .check(matches(withText("1")));

        onView(withId(R.id.cost_text_view))
                .check(matches(withText("$5.00")));
    }
}