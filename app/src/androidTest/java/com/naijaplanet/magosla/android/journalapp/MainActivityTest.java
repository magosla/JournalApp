package com.naijaplanet.magosla.android.journalapp;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * when there is no internet connection
     */
    @Test
    public void noInternetConnection(){
        onView(withId(R.id.text_connection)).check(matches(isDisplayed()));
    }

    /**
     * When the firebase login is displayed
     */
    @Test
    public void testFirebaseLogin(){
        allOf(withId(R.id.email_button), withId(R.id.btn_holder)).matches(matches(isDisplayed()));
    }
}
