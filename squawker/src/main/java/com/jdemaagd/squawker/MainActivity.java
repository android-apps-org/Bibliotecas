package com.jdemaagd.squawker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jdemaagd.squawker.following.FollowingPreferenceActivity;
import com.jdemaagd.squawker.provider.SquawkContract;
import com.jdemaagd.squawker.provider.SquawkProvider;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_MESSAGES = 0;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SquawkAdapter mAdapter;

    static final String[] MESSAGES_PROJECTION = {
            SquawkContract.COLUMN_AUTHOR,
            SquawkContract.COLUMN_MESSAGE,
            SquawkContract.COLUMN_DATE,
            SquawkContract.COLUMN_AUTHOR_KEY
    };

    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;
    static final int COL_NUM_AUTHOR_KEY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.squawks_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new SquawkAdapter();
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager.getInstance(this).initLoader(LOADER_ID_MESSAGES, null, this);

        logFCMToken();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("test")) {
            Log.d(LOG_TAG, "Contains: " + extras.getString("test"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_following_preferences) {
            Intent startFollowingActivity = new Intent(this, FollowingPreferenceActivity.class);
            startActivity(startFollowingActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = SquawkContract.createSelectionForCurrentFollowers(
                PreferenceManager.getDefaultSharedPreferences(this));

        Log.d(LOG_TAG, "Selection is " + selection);

        return new CursorLoader(this, SquawkProvider.SquawkMessages.CONTENT_URI,
                MESSAGES_PROJECTION, selection, null, SquawkContract.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void logFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(LOG_TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    Log.d(LOG_TAG, "FCM Token: " + token);
                });
    }
}