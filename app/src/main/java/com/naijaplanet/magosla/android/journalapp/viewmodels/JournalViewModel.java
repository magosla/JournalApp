package com.naijaplanet.magosla.android.journalapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.naijaplanet.magosla.android.journalapp.data.FirebaseLiveData;
import com.naijaplanet.magosla.android.journalapp.models.Journal;
import com.naijaplanet.magosla.android.journalapp.utilities.FirebaseUtil;

/**
 * The Journal ViewModel
 */
public class JournalViewModel extends ViewModel {

    private final DatabaseReference mDatabaseRef;
    private ValueEventListener mValueEventListener;

    @SuppressWarnings("SpellCheckingInspection")
    private FirebaseLiveData<Journal> mLiveData;

    @SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
    public JournalViewModel(String userId, String journalKey){
        // the path to the data in firebase, which is determined by the
        // App User's ID and the journalKey
        mDatabaseRef = FirebaseUtil.getJournalRef(userId, journalKey);

        configureListener();
    }


    /**
     * Configure how the view model listens to data from the data source
     */
    private void configureListener(){
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Journal journal = dataSnapshot.getValue(Journal.class);
                    mLiveData.postValue(journal);
                }else{
                    mLiveData.postValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    /**
     * Get the journal LiveData
     * @return Journal LiveData
     */
    public LiveData<Journal> getJournal() {
        if(mLiveData == null){
            mLiveData = new FirebaseLiveData<>(mDatabaseRef, mValueEventListener);
        }
        return mLiveData;
    }
}
