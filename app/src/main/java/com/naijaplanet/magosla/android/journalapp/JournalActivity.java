package com.naijaplanet.magosla.android.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.naijaplanet.magosla.android.journalapp.databinding.ActivityJournalBinding;
import com.naijaplanet.magosla.android.journalapp.models.Journal;
import com.naijaplanet.magosla.android.journalapp.models.User;
import com.naijaplanet.magosla.android.journalapp.utilities.ActivityUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModel;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModelFactory;

public class JournalActivity extends AppCompatActivity {
    private User mUser;
    private String mJournalKey;
    private ActivityJournalBinding mJournalBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try and enable transitions between activities
        ActivityUtil.enableTransition(this);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the user object
        mUser = getIntent().getParcelableExtra(Values.USER_INTENT_DATA_KEY);
        // get if provided the key that identifies the journal
        String journalKey = getIntent().getStringExtra(Values.INTENT_JOURNAL_ID_KEY);
        mJournalKey = journalKey.equals(Values.JOURNAL_ID_NONE) ? "" : journalKey;

        initializeUiComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_journal_edit:
                ActivityUtil.launchEditor(JournalActivity.this, mUser, mJournalKey);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialize the UI components for this activity
     */
    private void initializeUiComponents() {
        mJournalBinding = DataBindingUtil.setContentView(this, R.layout.activity_journal);

        final JournalViewModel journalViewModel = ViewModelProviders.
                of(this, new JournalViewModelFactory(mUser.getId(), mJournalKey))
                .get(JournalViewModel.class);


        journalViewModel.getJournal().observe(this, new Observer<Journal>() {
            @Override
            public void onChanged(@Nullable Journal journal) {
                // Remove this lifecycle owner
                //journalViewModel.getJournal().removeObserver(this);
                updateUi(journal);
            }
        });
    }

    /**
     * Update the Views with the data provided from the ViewModel
     * @param journal the Journal object
     */
    private void updateUi(@Nullable Journal journal) {
        if(journal == null)return;

        mJournalBinding.tvTitle.setText(journal.getTitle());
        mJournalBinding.tvContent.setText(journal.getContent());
        mJournalBinding.tvDatetime.setText(
                getString(R.string.created_time_format,Values.getFormattedDate(journal.getTimestamp()))
        );
        // only include the date this item was edited if it was actually edited
        if(journal.getEditTimestamp() > 0) {
            mJournalBinding.tvEditTimestamp.setVisibility(View.VISIBLE);
            mJournalBinding.tvEditTimestamp.setText(
                    getString(R.string.edited_time_format, Values.getFormattedDate(journal.getEditTimestamp()))
            );
        }else{
            mJournalBinding.tvEditTimestamp.setVisibility(View.GONE);
        }
        String jType = journal.getType().equals("FEELINGS") ?
                getString(R.string.my_feelings):getString(R.string.my_thoughts);
        mJournalBinding.tvJournalType.setText(jType);
    }
}
