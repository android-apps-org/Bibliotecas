package com.jdemaagd.ferramenta.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdemaagd.ferramenta.R;
import com.jdemaagd.ferramenta.provider.PlantContract.PlantEntry;

public class AddPlantActivity extends AppCompatActivity {

    private RecyclerView mTypesRecyclerView;
    private PlantTypesAdapter mTypesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        mTypesAdapter = new PlantTypesAdapter(this);
        mTypesRecyclerView = findViewById(R.id.rv_plant_types);
        mTypesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        mTypesRecyclerView.setAdapter(mTypesAdapter);
    }

    /**
     * Event handler to handle clicking on a plant type
     *
     * @param view
     */
    public void onPlantTypeClick(View view) {
        ImageView imgView = view.findViewById(R.id.iv_plant_type);
        int plantType = (int) imgView.getTag();

        long timeNow = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PlantEntry.COLUMN_PLANT_TYPE, plantType);
        contentValues.put(PlantEntry.COLUMN_CREATION_TIME, timeNow);
        contentValues.put(PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        getContentResolver().insert(PlantEntry.CONTENT_URI, contentValues);

        finish();
    }

    public void onBackButtonClick(View view) {
        finish();
    }
}