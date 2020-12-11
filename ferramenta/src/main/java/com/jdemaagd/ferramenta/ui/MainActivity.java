package com.jdemaagd.ferramenta.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdemaagd.ferramenta.R;
import com.jdemaagd.ferramenta.provider.PlantContract.PlantEntry;

import static com.jdemaagd.ferramenta.provider.PlantContract.BASE_CONTENT_URI;
import static com.jdemaagd.ferramenta.provider.PlantContract.PATH_PLANTS;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GARDEN_LOADER_ID = 100;
    private PlantListAdapter mAdapter;

    private RecyclerView mGardenRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGardenRecyclerView = findViewById(R.id.rv_plants_list);
        mGardenRecyclerView.setLayoutManager(
                new GridLayoutManager(this, 4)
        );

        mAdapter = new PlantListAdapter(this, null);
        mGardenRecyclerView.setAdapter(mAdapter);

        LoaderManager.getInstance(this).initLoader(GARDEN_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();

        return new CursorLoader(this, PLANT_URI, null,
                null, null, PlantEntry.COLUMN_CREATION_TIME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) { }

    public void onPlantClick(View view) {
        ImageView imgView = (ImageView) view.findViewById(R.id.iv_plant);
        long plantId = (long) imgView.getTag();

        Intent intent = new Intent(getBaseContext(), PlantDetailActivity.class);
        intent.putExtra(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
        startActivity(intent);
    }

    public void onAddFabClick(View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }
}