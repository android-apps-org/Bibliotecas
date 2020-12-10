package com.jdemaagd.horadocha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;

import com.jdemaagd.horadocha.idlingresource.SimpleIdlingResource;
import com.jdemaagd.horadocha.model.Tea;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements ImageDownloader.DelayerCallback  {

    Intent mTeaIntent;

    public final static String EXTRA_TEA_NAME = "com.jdemaagd.horadocha.EXTRA_TEA_NAME";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setTitle(getString(R.string.menu_title));

        final ArrayList<Tea> teas = new ArrayList<>();
        teas.add(new Tea(getString(R.string.black_tea_name), R.drawable.black_tea));
        teas.add(new Tea(getString(R.string.green_tea_name), R.drawable.green_tea));
        teas.add(new Tea(getString(R.string.white_tea_name), R.drawable.white_tea));
        teas.add(new Tea(getString(R.string.oolong_tea_name), R.drawable.oolong_tea));
        teas.add(new Tea(getString(R.string.honey_lemon_tea_name), R.drawable.honey_lemon_tea));
        teas.add(new Tea(getString(R.string.chamomile_tea_name), R.drawable.chamomile_tea));

        GridView gridview = findViewById(R.id.tea_grid_view);
        TeaMenuAdapter adapter = new TeaMenuAdapter(this, R.layout.grid_item_layout, teas);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener((adapterView, view, position, id) -> {
            Tea item = (Tea) adapterView.getItemAtPosition(position);

            mTeaIntent = new Intent(MenuActivity.this, OrderActivity.class);
            String teaName = item.getTeaName();
            mTeaIntent.putExtra(EXTRA_TEA_NAME, teaName);

            startActivity(mTeaIntent);
        });

        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageDownloader.downloadImage(this, MenuActivity.this, mIdlingResource);
    }

    @Override
    public void onDone(ArrayList<Tea> teas) {
        GridView gridview = findViewById(R.id.tea_grid_view);
        TeaMenuAdapter adapter = new TeaMenuAdapter(this, R.layout.grid_item_layout, teas);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener((adapterView, view, position, id) -> {

            Tea item = (Tea) adapterView.getItemAtPosition(position);

            mTeaIntent = new Intent(MenuActivity.this, OrderActivity.class);
            String teaName = item.getTeaName();
            mTeaIntent.putExtra(EXTRA_TEA_NAME, teaName);

            startActivity(mTeaIntent);
        });
    }
}