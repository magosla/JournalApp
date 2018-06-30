package com.naijaplanet.magosla.android.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.naijaplanet.magosla.android.journalapp.models.Journal;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.models.User;
import com.naijaplanet.magosla.android.journalapp.utilities.ActivityUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.FirebaseUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModel;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalViewModelFactory;

public class EditorActivity extends AppCompatActivity {
    public final static String TAG = "EditorActivity";

    private EditText mTitleEditText, mContentEditText;
    private RadioGroup mJournalTypeRadioGroup;
    private RadioButton mFeelingsRadioButton;
    private RadioButton mThoughtsRadioButton;
    private MenuItem mSaveMenuItem;

    // the key that identifies the journal if provided
    private String mJournalKey;

    // the user object
    private User mUser;

    // the journal object
    private Journal mJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // try to enable activity change transitions if supported
        ActivityUtil.enableTransition(this);

        mUser = getIntent().getParcelableExtra(Values.USER_INTENT_DATA_KEY);
        String journalId = getIntent().getStringExtra(Values.INTENT_JOURNAL_ID_KEY);
        mJournalKey = journalId.equals(Values.JOURNAL_ID_NONE) ? "" : journalId;

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeUiComponents();
        updateInputFields(mJournalKey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor, menu);
        mSaveMenuItem = menu.findItem(R.id.action_journal_save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_journal_save:
                saveJournal();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialize the view components for this activity
     */
    private void initializeUiComponents() {
        setContentView(R.layout.activity_editor);
        setTitle(getString(mJournalKey.isEmpty() ? R.string.add_journal : R.string.edit_journal));

        mTitleEditText = findViewById(R.id.et_title);
        mContentEditText = findViewById(R.id.et_content);
        mJournalTypeRadioGroup = findViewById(R.id.rg_type);
        mThoughtsRadioButton = findViewById(R.id.rb_thought);
        mFeelingsRadioButton = findViewById(R.id.rb_feelings);

        // key track of text inputted to this field to determine if the save
        // button can be enabled
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // key track of text inputted to this field to determine if the save
        // button can be enabled
        mContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Update the Input fields after trying to fetch the journal entry
     * if a journalKey was provided
     *
     * @param journalKey the key that identifies the journal
     */
    private void updateInputFields(@Nullable final String journalKey) {
        if (journalKey == null || journalKey.isEmpty()) return;

        final JournalViewModel journalViewModel = ViewModelProviders.
                of(this, new JournalViewModelFactory(mUser.getId(), journalKey))
                .get(JournalViewModel.class);

        journalViewModel.getJournal().observe(this, new Observer<Journal>() {
            @Override
            public void onChanged(@Nullable Journal journal) {
                // Remove this lifecycle owner
                journalViewModel.getJournal().removeObserver(this);

                mJournal = journal;
                updateFields();
            }
        });
    }

    /**
     * Update the input fields from the journal entry retrieved from the database
     */
    private void updateFields() {
        if(mJournal == null) return;

        mTitleEditText.setText(mJournal.getTitle());
        mContentEditText.setText(mJournal.getContent());
        // make the journal type radio item selected
        checkSelectedType(mJournal.getType());
        updateSaveButtonState();
    }

    /**
     * Toggles the Enabled state of the save button
     */
    private void updateSaveButtonState() {
        if (mSaveMenuItem != null) {
            mSaveMenuItem.setEnabled(
                    mTitleEditText.getText().toString().trim().length() > 0 && mContentEditText.getText()
                            .toString().trim().length() > 0 && isJournalTypeSelected()

            );
        }
    }

    /**
     * Click hander for the journal type radio button
     *
     * @param view the radio item
     */
    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
        updateSaveButtonState();
    }

    /**
     * Save the journal to database
     */
    public void saveJournal() {

        final DatabaseReference userJournalDatabaseRef =
                FirebaseUtil.getFirebaseDatabase().getReference()
                .child(String.format("%s/%s", Values.JOURNAL_DATABASE_PATH, mUser.getId()));

        DatabaseReference ref;
        if (mJournal == null) {
            mJournal = new Journal();
            // get the reference of the new key
            ref = userJournalDatabaseRef.push();
            mJournal.setKey(ref.getKey());
            mJournal.setTimestamp(Values.getCurrentTime());
        } else {
            // use the reference for the existing key
            ref = userJournalDatabaseRef.child(mJournalKey);
            mJournal.setEditTimestamp(Values.getCurrentTime());
        }
        mJournal.setTitle(mTitleEditText.getText().toString().trim());
        mJournal.setContent(mContentEditText.getText().toString().trim());
        mJournal.setType(getSelectedJournalType());

        ref.setValue(mJournal);


        Toast.makeText(this, String.format("%s %s", mJournal.getTitle(),
                !mJournalKey.isEmpty() ? getString(R.string.updated) :
                        getString(R.string.added)), Toast.LENGTH_SHORT).show();
        finish();

    }

    /**
     * Checks if a radio type has been checked
     *
     * @return true if an item is selected
     */
    private boolean isJournalTypeSelected() {
        return mJournalTypeRadioGroup.getCheckedRadioButtonId() != -1;
    }

    /**
     *  Checks the radio button from the Journal type radio group
     * @param selectedType the journal type
     */
    private void checkSelectedType(String selectedType) {

        Log.d(TAG, selectedType);
        switch (selectedType) {
            case "FEELINGS":
                Log.d(TAG, "FEELINGS");
                //mJournalTypeRadioGroup.check(R.id.rb_feelings);
                mFeelingsRadioButton.setChecked(true);
                break;
            case "THOUGHTS":
                Log.d(TAG, "THOUGHTS");
                //mJournalTypeRadioGroup.check(R.id.rb_thought);
                mThoughtsRadioButton.setChecked(true);
        }
    }

    /**
     * Get the journal type the user selected
     *
     * @return the name of the journal type selected
     */
    private String getSelectedJournalType() {
        switch (mJournalTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_feelings:
                return JournalsItem.Type.FEELINGS.toString();
            case R.id.rb_thought:
                return JournalsItem.Type.THOUGHTS.toString();
            default:
                return JournalsItem.Type.FEELINGS.toString();
        }
    }
}
