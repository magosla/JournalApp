package com.naijaplanet.magosla.android.journalapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.naijaplanet.magosla.android.journalapp.data.FirebaseLiveData;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.utilities.FirebaseUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the List of journal Entries
 */
public class JournalsViewModel extends ViewModel {

    private final DatabaseReference mDatabaseRef;
    private ValueEventListener mValueEventListener;

    @SuppressWarnings("SpellCheckingInspection")
    private FirebaseLiveData<List<JournalsItem>> mLiveData;

    @SuppressWarnings("WeakerAccess")
    public JournalsViewModel(String userId){
        mDatabaseRef = FirebaseUtil.getJournalsRef(userId);

        configureListeners();
    }

    /**
     * Configure how the view model listens to data from the data source
     */
    private void configureListeners() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // TODO: - Use Executor instead of Thread
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    List<JournalsItem> journals  = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        journals.add(snapshot.getValue(JournalsItem.class));
                                    }
                                    mLiveData.postValue(journals);
                                }
                            }
                    ).start();
                }else{
                    mLiveData.postValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
    }

    /**
     *  Get the list of Journal Entries
     * @return journals LiveData
     */
    public LiveData<List<JournalsItem>> getJournals(){
        if(mLiveData == null){
            mLiveData = new FirebaseLiveData<>(mDatabaseRef, mValueEventListener);
        }
        return mLiveData;
    }

}
