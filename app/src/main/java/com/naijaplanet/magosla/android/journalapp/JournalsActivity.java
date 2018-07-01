package com.naijaplanet.magosla.android.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.naijaplanet.magosla.android.journalapp.adapters.JournalsAdapter;
import com.naijaplanet.magosla.android.journalapp.models.JournalsItem;
import com.naijaplanet.magosla.android.journalapp.models.User;
import com.naijaplanet.magosla.android.journalapp.utilities.ActivityUtil;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalsViewModel;
import com.naijaplanet.magosla.android.journalapp.viewmodels.JournalsViewModelFactory;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

@SuppressWarnings("SpellCheckingInspection")
public class JournalsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, JournalsAdapter.ItemListeners {

    // the authenticated User
    private User mUser;

    private JournalsViewModel mViewModel;

    private ProgressBar mLoadingIndicator;

    // Recyclerview object
    private RecyclerView mRecyclerView;
    // JournalsAdapter object
    private JournalsAdapter mJournalsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.enableTransition(this);


        // get the user from sharedpreference
        mUser = Values.getUserFromPreference(this);

        if (mUser == null || mUser.getId().isEmpty()) {
            ActivityUtil.signOut(this);
            return;
        }

        initializeUiComponents();

        attachViewModel();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_journals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.journal_item_layout) {
            getMenuInflater().inflate(R.menu.activity_journals_item_context, menu);
            // TODO - provide statements to keep pass the journal key save in the View Tag to the menu items
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO - Implement for the context menu used by the journal list
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle logout
        if (id == R.id.drawer_logout) {
            signOut();
        }
        // start the Editor activity
        else if (id == R.id.drawer_create) launchEditor(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClickListener(View v) {
        switch (v.getId()) {
            // this identifies a click action from the journal list item
            // we want to view the details of the journal while this happens
            case R.id.journal_item_layout:
                launchJournal(v.getTag().toString());
                break;
        }

    }

    @Override
    public boolean onItemLongClickListener(View v) {
        switch (v.getId()) {
            // this identifies the long click action from the journals list item
            // as we want to Edit the journal while this happens
            case R.id.journal_item_layout:
                launchEditor(v.getTag().toString());
                return true;
        }
        return false;
    }


    /**
     * initializes the viewmodel and it adapter to fetch list of journal from the data source
     */
    private void attachViewModel() {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this, new JournalsViewModelFactory(mUser.getId()))
                    .get(JournalsViewModel.class);
        }
        mViewModel.getJournals().observe(this, new Observer<List<JournalsItem>>() {
            @Override
            public void onChanged(@Nullable List<JournalsItem> journalsItems) {
                showContentView();
                mJournalsAdapter.setJournals(journalsItems);
            }
        });
    }

    /**
     * Ensures the View for the journal list is visible while hiding the loading indicator
     */
    private void showContentView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
    }


    /**
     * Initialize the UI component for this Activity
     */
    private void initializeUiComponents() {
        setContentView(R.layout.activity_journals);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddFloatingButton();

        addDrawerLayout(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addRecyclerViewComponents();

    }

    /**
     * Set up the recylerview components
     */
    private void addRecyclerViewComponents() {
        mRecyclerView = findViewById(R.id.recyclerview_journals);
        mLoadingIndicator = findViewById(R.id.progress_loading);
        TextView pageTitle = findViewById(R.id.text_journal_title);
        pageTitle.setText(getString(R.string.journals_list_title, mUser.getName()));

        // Initially hide the recycler view.
        mRecyclerView.setVisibility(View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        // mRecyclerView.setHasFixedSize(true);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mJournalsAdapter = new JournalsAdapter(this, this);
        mRecyclerView.setAdapter(mJournalsAdapter);
    }

    /**
     * Adds a floating button to view
     */
    private void AddFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEditor(null);
            }
        });
    }

    /**
     * Adds a drawer layout to vies
     *
     * @param toolbar the activity toolbar
     */
    private void addDrawerLayout(final Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ((TextView) drawerView.findViewById(R.id.nav_header_title)).setText(mUser.getName());
                ((TextView) drawerView.findViewById(R.id.nav_header_subtitle)).setText(mUser.getEmail());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Lunches an Activity to Edit the journal if the journalKey is not null or
     * an Activity to create a new journal if journalKey is null
     *
     * @param journalKey this key that identifies  the journal.
     */
    private void launchEditor(@Nullable String journalKey) {
        ActivityUtil.launchEditor(this, journalKey);
    }

    /**
     * Starts an activity to view details the journal with the provided journalKey
     *
     * @param journalKey the key that identifies the journal
     */
    private void launchJournal(@NonNull String journalKey) {
        ActivityUtil.launchJournal(this, journalKey);
    }

    /**
     * Displays a dialog for the About of this app
     */
    private void showAbout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.about_app_title);
        dialog.setMessage(R.string.about_app_description);
        dialog.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Signout the user from the app
     */
    private void signOut() {
        ActivityUtil.signOut(this);
    }

}
