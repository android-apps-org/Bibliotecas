package com.jdemaagd.discador;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DialerActivityTest {

    private static final String VALID_PHONE_NUMBER = "123-345-6789";

    private static final Uri INTENT_DATA_PHONE_NUMBER = Uri.parse("tel:" + VALID_PHONE_NUMBER);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant("android.permission.CALL_PHONE");

    /**
     * A JUnit {@link Rule @Rule} to init and release Espresso Intents before and after each
     * test run.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * This rule is based on {@link IntentsTestRule} and will create and launch the activity
     * for you and also expose the activity under test.
     */
    @Rule
    public IntentsTestRule<DialerActivity> mActivityRule = new IntentsTestRule<>(
            DialerActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void typeNumber_ValidInput_InitiatesCall() {
        onView(withId(R.id.edit_text_caller_number))
                .perform(typeText(VALID_PHONE_NUMBER), closeSoftKeyboard());
        onView(withId(R.id.button_call_number))
                .perform(click());

        // Verify intent was sent with correct action, data, package
        intended(allOf(
                hasAction(Intent.ACTION_CALL),
                hasData(INTENT_DATA_PHONE_NUMBER)));
    }

    @Test
    public void typeNumber_ValidInput_InitiatesCall_truth() {
        onView(withId(R.id.edit_text_caller_number))
                .perform(typeText(VALID_PHONE_NUMBER), closeSoftKeyboard());
        onView(withId(R.id.button_call_number))
                .perform(click());

        // Verify intent was sent with correct action, data, package
        Intent receivedIntent = Iterables.getOnlyElement(Intents.getIntents());
        //assertThat(receivedIntent).hasAction(Intent.ACTION_CALL);
        //assertThat(receivedIntent).hasData(INTENT_DATA_PHONE_NUMBER);
    }

    @Test
    public void pickContactButton_click_SelectsPhoneNumber() {
        // Note: Activity is never launched and result is stubbed
        intending(hasComponent(hasShortClassName(".ContactsActivity")))
                .respondWith(new ActivityResult(Activity.RESULT_OK,
                        ContactsActivity.createResultData(VALID_PHONE_NUMBER)));

        onView(withId(R.id.button_pick_contact))
                .perform(click());

        onView(withId(R.id.edit_text_caller_number))
                .check(matches(withText(VALID_PHONE_NUMBER)));
    }
}