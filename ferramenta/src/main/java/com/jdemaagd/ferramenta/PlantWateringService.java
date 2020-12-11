package com.jdemaagd.ferramenta;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.jdemaagd.ferramenta.provider.PlantContract;
import com.jdemaagd.ferramenta.provider.PlantContract.PlantEntry;
import com.jdemaagd.ferramenta.utils.PlantUtils;

import static com.jdemaagd.ferramenta.provider.PlantContract.BASE_CONTENT_URI;
import static com.jdemaagd.ferramenta.provider.PlantContract.INVALID_PLANT_ID;
import static com.jdemaagd.ferramenta.provider.PlantContract.PATH_PLANTS;

public class PlantWateringService extends IntentService {

    public static final String ACTION_WATER_PLANT =
            "com.jdemaagd.ferramenta.action.water_plants";
    public static final String ACTION_UPDATE_PLANT_WIDGETS =
            "com.jdemaagd.ferramenta.action.update_plant_widgets";
    public static final String EXTRA_PLANT_ID =
            "com.jdemaagd.ferramenta.extra.PLANT_ID";

    public PlantWateringService() {
        super("PlantWateringService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANT.equals(action)) {
                final long plantId = intent.getLongExtra(EXTRA_PLANT_ID,
                        PlantContract.INVALID_PLANT_ID);
                handleActionWaterPlant(plantId);
            } else if (ACTION_UPDATE_PLANT_WIDGETS.equals(action)) {
                handleActionUpdatePlantWidgets();
            }
        }
    }

    public static void startActionWaterPlant(Context context, long plantId) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_WATER_PLANT);
        intent.putExtra(EXTRA_PLANT_ID, plantId);
        context.startService(intent);
    }

    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        context.startService(intent);
    }

    // Query ContentProvider via ContentResolver
    private void handleActionWaterPlant(long plantId) {
        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build(), plantId);

        ContentValues contentValues = new ContentValues();
        long timeNow = System.currentTimeMillis();
        contentValues.put(PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);

        getContentResolver().update(
                SINGLE_PLANT_URI,
                contentValues,
                PlantEntry.COLUMN_LAST_WATERED_TIME + ">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});

        startActionUpdatePlantWidgets(this);
    }

    // Query ContentProvider via ContentResolver/Cursor
    private void handleActionUpdatePlantWidgets() {
        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
        Cursor cursor = getContentResolver().query(
                PLANT_URI,
                null,
                null,
                null,
                PlantEntry.COLUMN_LAST_WATERED_TIME
        );

        int imgRes = R.drawable.grass;
        boolean canWater = false;
        long plantId = INVALID_PLANT_ID;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int idIndex = cursor.getColumnIndex(PlantEntry._ID);
            int createTimeIndex = cursor.getColumnIndex(PlantEntry.COLUMN_CREATION_TIME);
            int waterTimeIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERED_TIME);
            int plantTypeIndex = cursor.getColumnIndex(PlantEntry.COLUMN_PLANT_TYPE);

            plantId = cursor.getLong(idIndex);

            long timeNow = System.currentTimeMillis();
            long wateredAt = cursor.getLong(waterTimeIndex);
            long createdAt = cursor.getLong(createTimeIndex);
            int plantType = cursor.getInt(plantTypeIndex);

            cursor.close();

            canWater = (timeNow - wateredAt) > PlantUtils.MIN_AGE_BETWEEN_WATER &&
                    (timeNow - wateredAt) < PlantUtils.MAX_AGE_WITHOUT_WATER;
            imgRes = PlantUtils.getPlantImageRes(this, timeNow - createdAt, timeNow - wateredAt, plantType);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PlantWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget);

        PlantWidgetProvider.updatePlantWidgets(this, appWidgetManager, imgRes, plantId, canWater, appWidgetIds);
    }
}