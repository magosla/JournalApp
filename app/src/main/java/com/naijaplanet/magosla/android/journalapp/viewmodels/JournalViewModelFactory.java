package com.naijaplanet.magosla.android.journalapp.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/**
 *  This class makes it possible to pass values to JournalViewModel
 */
public class JournalViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private String mUserId;
    private String mJournalKey;

    public JournalViewModelFactory(String userId, String journalKey){
        mUserId = userId;
        mJournalKey = journalKey;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new JournalViewModel(mUserId, mJournalKey);
    }
}
