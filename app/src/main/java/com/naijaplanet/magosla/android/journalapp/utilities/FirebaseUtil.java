package com.naijaplanet.magosla.android.journalapp.utilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class provides helper methods for Firebase
 */
@SuppressWarnings("SpellCheckingInspection")
public class FirebaseUtil {
    private static FirebaseDatabase mDatabase;

    /**
     * Initializes the FirebaseDatabase instance
     *
     * @return the FirebaseDatabase instance
     */
    @SuppressWarnings("WeakerAccess")
    public static FirebaseDatabase getFirebaseDatabase() {
        Log.d("FirebaseUtil", "Requesting FirebaseDatabase instance");

        // don't initialize if previously initialized
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            Log.d("FirebaseUtil", "FirebaseDatabase instance initialization");
        }
        return mDatabase;
    }

    public static DatabaseReference getJournalsRef(@NonNull String userId) {
        return FirebaseUtil.getFirebaseDatabase().getReference()
                .child(String.format("%s/%s",
                        Values.JOURNAL_DATABASE_PATH, userId));
    }

    public static DatabaseReference getJournalRef(@NonNull String userId, @NonNull String journalKey) {
        return FirebaseUtil.getFirebaseDatabase().getReference()
                .child(String.format("%s/%s/%s",
                        Values.JOURNAL_DATABASE_PATH, userId, journalKey));
    }

    public static void deleteItem(@NonNull String userId, @NonNull String journalKey, @Nullable final OnCompletionListener callback) {
        getJournalRef(userId, journalKey).getRef().removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (callback != null) {
                    boolean hasError = databaseError != null && !databaseError.getMessage().isEmpty();
                    callback.onComplete(hasError);
                }
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnCompletionListener {
        // just basic, no checking if it was a success or not

        /**
         * Called when the process has completed
         *
         * @param encounteredError if there was an error
         */
        void onComplete(boolean encounteredError);
    }
}
