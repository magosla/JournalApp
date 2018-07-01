package com.naijaplanet.magosla.android.journalapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditorActivityTest {
    @Rule
    public ActivityTestRule<EditorActivity> mActivityRule = new ActivityTestRule<>(
            EditorActivity.class);


    @Rule
    public void testInputFormDisplayed(){
        onView(allOf(withId(R.id.edit_title)
            ,withId(R.id.edit_content)
                ,withId(R.id.radio_group_type),
                isDisplayed()
        ));
    }
}
