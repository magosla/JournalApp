package com.naijaplanet.magosla.android.journalapp.data;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseLiveData<T> extends LiveData<T> {
    private final static String TAG = "FirebaseLiveData";

    private final ValueEventListener mValueEventListener;
    private final ChildEventListener mChildEventListener;

    private final Query mQuery;

    private boolean mPendingListenerDetach = false;
    private final Handler mListenerDetachHandler = new Handler();

    private final Runnable initListenerDetach = new Runnable() {
        @Override
        public void run() {
            detachListener();
            mPendingListenerDetach = false;
        }
    };

    public FirebaseLiveData(DatabaseReference ref, ValueEventListener eventListener){
        mValueEventListener = eventListener;
        mChildEventListener = null;
        mQuery = ref;
    }

    @SuppressWarnings("unused")
    public FirebaseLiveData(Query query, ValueEventListener eventListener){
        mValueEventListener = eventListener;
        mChildEventListener = null;
        mQuery = query;
    }
    @SuppressWarnings("unused")
    public FirebaseLiveData(DatabaseReference ref, ChildEventListener eventListener){
        mChildEventListener = eventListener;
        mValueEventListener = null;
        mQuery = ref;
    }
    @SuppressWarnings("unused")
    public FirebaseLiveData(Query query, ChildEventListener eventListener){
        mChildEventListener = eventListener;
        mValueEventListener = null;
        mQuery = query;
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }

    @Override
    protected void onActive() {
        if (mPendingListenerDetach) {
            mListenerDetachHandler.removeCallbacks(initListenerDetach);
        }
        else {
            attachListener();
        }
        mPendingListenerDetach = false;
    }

    @Override
    protected void onInactive() {
        // Schedule removal of listener after two seconde
        mListenerDetachHandler.postDelayed(initListenerDetach, 2000);
        mPendingListenerDetach = true;
    }

    private void attachListener() {
        Log.d(TAG, "Observer active");

        // Attach listener based on the Listener type being used
        if(mValueEventListener != null){
            mQuery.addValueEventListener(mValueEventListener);
        }
        else if(mChildEventListener != null){
            mQuery.addChildEventListener(mChildEventListener);
        }
    }

    private void detachListener() {
        Log.d(TAG, "Observer in-active");

        // Detach listener based on the Listener type being used
        if(mValueEventListener != null){
            mQuery.removeEventListener(mValueEventListener);
        }
        else if(mChildEventListener != null){
            mQuery.removeEventListener(mChildEventListener);
        }
    }
}
