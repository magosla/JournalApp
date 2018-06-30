package com.naijaplanet.magosla.android.journalapp.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;


/**
 *  This class makes it possible to pass values to JournalViewModel
 */
public class JournalsViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private String mUserId;
    public JournalsViewModelFactory(String userId){
        mUserId = userId;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new JournalsViewModel(mUserId);
    }
}
