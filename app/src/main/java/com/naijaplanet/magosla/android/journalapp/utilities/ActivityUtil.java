package com.naijaplanet.magosla.android.journalapp.utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.view.Window;

import com.naijaplanet.magosla.android.journalapp.EditorActivity;
import com.naijaplanet.magosla.android.journalapp.JournalActivity;
import com.naijaplanet.magosla.android.journalapp.models.User;

/**
 * This class provides helper methods for activities
 */
public class ActivityUtil {
    // if activity change transition has been enabled
    private static boolean mTransitionEnabled;
    // it a call the enable activity transition has been made
    private static boolean mCalledTransitionEnable;
    /**
     * Starts an activity to edit or create journal
     * @param activity the current activity
     * @param user the user object
     * @param journalKey the journal key
     */
    public static void launchEditor(Activity activity, User user, @Nullable String journalKey) {
        Intent editorIntent = new Intent(activity, EditorActivity.class);
        editorIntent.putExtra(Values.USER_INTENT_DATA_KEY, user);
        editorIntent.putExtra(Values.INTENT_JOURNAL_ID_KEY, journalKey != null ? journalKey : Values.JOURNAL_ID_NONE);
        activity.startActivity(editorIntent);
    }


    /**
     * Starts an activity to open the journal detail
     * @param activity the current activity
     * @param user the user object
     * @param journalKey the journal key
     */
    public static void launchJournal(Activity activity, User user, @Nullable String journalKey){
        Intent journalIntent = new Intent(activity, JournalActivity.class);
        journalIntent.putExtra(Values.USER_INTENT_DATA_KEY, user);
        journalIntent.putExtra(Values.INTENT_JOURNAL_ID_KEY, journalKey);
        activity.startActivity(journalIntent, getTransitionsBundle(activity));
    }

    /**
     * Tries to get the transition bundle that would be used to animate
     * Activity change
     *
     * @param activity the current activity
     * @return the bundle if available
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bundle getTransitionsBundle(Activity activity){
        Bundle bundle = null;
        if(mCalledTransitionEnable && mTransitionEnabled) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
        }
        return bundle;
    }

    /**
     * Enables activity transition if supported by the SDK version
     * @param activity the current activity
     */
    public static  void enableTransition(Activity activity){
        if(!mCalledTransitionEnable) {
            mCalledTransitionEnable = true;
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
                // inside your activity (if you did not enable transitions in your theme)
                activity.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                // set an exit transition
                activity.getWindow().setExitTransition(new Explode());
                mTransitionEnabled = true;

            } else {
                // Swap without transition
                mTransitionEnabled =false;
            }
        }

    }
}
