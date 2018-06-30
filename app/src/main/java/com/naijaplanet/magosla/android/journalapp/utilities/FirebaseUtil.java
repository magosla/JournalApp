package com.naijaplanet.magosla.android.journalapp.utilities;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * This class provides helper methods for Firebase
 */
public class FirebaseUtil {
    private static FirebaseDatabase mDatabase;

    /**
     * Initializes the FirebaseDatabase instance
     * @return the FirebaseDatabase instance
     */
    public static FirebaseDatabase getFirebaseDatabase(){
        Log.d("FirebaseUtil", "Requesting FirebaseDatabase instance");

        // don't initialize if previously intialized
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            Log.d("FirebaseUtil", "FirebaseDatabase instance initialization");
        }
        return mDatabase;
    }
}
