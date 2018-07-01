package com.naijaplanet.magosla.android.journalapp.utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.transition.Explode;
import android.view.Window;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.naijaplanet.magosla.android.journalapp.EditorActivity;
import com.naijaplanet.magosla.android.journalapp.JournalActivity;
import com.naijaplanet.magosla.android.journalapp.MainActivity;
import com.naijaplanet.magosla.android.journalapp.R;

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
     *
     * @param activity   the current activity
     *                   //@param user the user object
     * @param journalKey the journal key
     */
    public static void launchEditor(Activity activity,/* User user,*/ @Nullable String journalKey) {
        Intent editorIntent = new Intent(activity, EditorActivity.class);
        // put the User object
        //editorIntent.putExtra(Values.USER_INTENT_DATA_KEY, user);
        // put the journal key if available
        editorIntent.putExtra(Values.EXTRA_JOURNAL_KEY, journalKey != null ? journalKey : Values.JOURNAL_ID_NONE);
        activity.startActivity(editorIntent);
    }


    /**
     * Starts an activity to open the journal detail
     *
     * @param activity   the current activity
     *                   //@param user the user object
     * @param journalKey the journal key
     */
    public static void launchJournal(Activity activity,/* User user,*/ @Nullable String journalKey) {
        Intent journalIntent = new Intent(activity, JournalActivity.class);
        // send the user object
        // journalIntent.putExtra(Values.USER_INTENT_DATA_KEY, user);

        // send the journal key
        journalIntent.putExtra(Values.EXTRA_JOURNAL_KEY, journalKey);
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
    private static Bundle getTransitionsBundle(Activity activity) {
        Bundle bundle = null;
        // if enableTransition has been called and the device SDK support transition
        if (mCalledTransitionEnable && mTransitionEnabled) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
        }
        return bundle;
    }

    /**
     * Enables activity transition if supported by the SDK version
     *
     * @param activity the current activity
     */
    public static void enableTransition(Activity activity) {
        if (!mCalledTransitionEnable) {
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
                mTransitionEnabled = false;
            }
        }

    }


    /**
     * Sign out the user from App
     */
    public static void signOut(final Activity activity) {
        AuthUI.getInstance().signOut(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Values.clearUserInPreference(activity);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    /**
     * @param context    the application context
     * @param userId     the app user id
     * @param journalKey the journal key
     * @param callback   callback called when action is complete
     */
    public static void deleteJournal(Context context, @NonNull final String userId
            , @NonNull final String journalKey, final FirebaseUtil.OnCompletionListener callback) {

        new AlertDialog.Builder(context)
                .setTitle(R.string.title_delete_confirm)
                .setMessage(R.string.msg_delete_confirm)
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUtil.deleteItem(userId, journalKey, callback);
                    }
                }).show();
    }
}
