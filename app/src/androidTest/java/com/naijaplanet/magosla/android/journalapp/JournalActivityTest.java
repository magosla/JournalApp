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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class JournalActivityTest {
    @Rule
    public ActivityTestRule<JournalActivity> mActivityRule = new ActivityTestRule<>(
            JournalActivity.class);

    @Test
    public void testView(){
        onView(withId(R.id.text_datetime)).check(matches(isDisplayed()));
    }

    @Test
    public void journalHasBeenEdited(){
        onView(withId(R.id.text_edit_timestamp)).check(matches(isDisplayed()));
    }
}
