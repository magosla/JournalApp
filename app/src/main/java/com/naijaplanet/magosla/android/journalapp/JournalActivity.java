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
import android.widget.Toast;

import com.naijaplanet.magosla.android.journalapp.databinding.ActivityJournalBinding;
import com.naijaplanet.magosla.android.journalapp.models.Journal;
import com.naijaplanet.magosla.android.journalapp.models.User;
import com.naijaplanet.magosla.android.journalapp.utilities.ActivityUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.FirebaseUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModel;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModelFactory;

public class JournalActivity extends AppCompatActivity {
    private String mJournalKey;
    private ActivityJournalBinding mJournalBinding;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try and enable transitions between activities
        ActivityUtil.enableTransition(this);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get if provided the key that identifies the journal
        String journalKey = getIntent().getStringExtra(Values.EXTRA_JOURNAL_KEY);
        mJournalKey = journalKey.equals(Values.JOURNAL_ID_NONE) ? "" : journalKey;

        initializeUiComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_journal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                delete();
                return true;
            case R.id.menu_edit:
                ActivityUtil.launchEditor(JournalActivity.this, mJournalKey);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * deletes the journal from database
     */
    private void delete() {

        if(mUser != null && !mJournalKey.isEmpty()) {
            ActivityUtil.deleteJournal(this, mUser.getId(), mJournalKey, new FirebaseUtil.OnCompletionListener() {
                @Override
                public void onComplete(boolean encounteredError) {
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.msg_journal_deleted, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Initialize the UI components for this activity
     */
    private void initializeUiComponents() {
        mJournalBinding = DataBindingUtil.setContentView(this, R.layout.activity_journal);

        // get the user object
        //User user = getIntent().getParcelableExtra(Values.EXTRA_USER);
        // get the user object from preference
        mUser = Values.getUserFromPreference(this);
        if(mUser == null){
            ActivityUtil.signOut(this);
            return;
        }

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
     *
     * @param journal the Journal object
     */
    private void updateUi(@Nullable Journal journal) {
        if (journal == null) return;

        mJournalBinding.textTitle.setText(journal.getTitle());
        mJournalBinding.textContent.setText(journal.getContent());
        mJournalBinding.textDatetime.setText(
                getString(R.string.created_time_format
                        , Values.getFormattedDate(journal.getTimestamp(), Values.DATE_TIME_FORMAT))
        );
        // only include the date this item was edited if it was actually edited
        if (journal.getEditTimestamp() > 0) {
            mJournalBinding.textEditTimestamp.setVisibility(View.VISIBLE);
            mJournalBinding.textEditTimestamp.setText(
                    getString(R.string.edited_time_format
                            , Values.getFormattedDate(journal.getEditTimestamp(), Values.DATE_TIME_FORMAT))
            );
        } else {
            mJournalBinding.textEditTimestamp.setVisibility(View.GONE);
        }
        String jType = journal.getType().equals("FEELINGS") ?
                getString(R.string.my_feelings) : getString(R.string.my_thoughts);
        mJournalBinding.textJournalType.setText(jType);
    }
}
