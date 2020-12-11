package com.jdemaagd.ferramenta.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import com.jdemaagd.ferramenta.provider.PlantContract.PlantEntry;

public class PlantContentProvider extends ContentProvider {

    public static final int PLANTS = 100;
    public static final int PLANT_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = PlantContentProvider.class.getName();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PlantContract.AUTHORITY, PlantContract.PATH_PLANTS, PLANTS);
        uriMatcher.addURI(PlantContract.AUTHORITY, PlantContract.PATH_PLANTS + "/#", PLANT_WITH_ID);

        return uriMatcher;
    }

    private PlantDbHelper mPlantDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPlantDbHelper = new PlantDbHelper(context);

        return true;
    }

    /***
     * Handles requests to insert a single new row of data
     *
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PLANTS:
                long id = db.insert(PlantEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PlantContract.PlantEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    /***
     * Handles requests for data by URI
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mPlantDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case PLANTS:
                retCursor = db.query(PlantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLANT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(PlantEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    /***
     * Deletes a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int plantsDeleted;

        switch (match) {
            case PLANT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                plantsDeleted = db.delete(PlantEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (plantsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return plantsDeleted;
    }

    /***
     * Updates a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int plantsUpdated;

        switch (match) {
            case PLANTS:
                plantsUpdated = db.update(PlantEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PLANT_WITH_ID:
                if (selection == null) selection = PlantEntry._ID + "=?";
                else selection += " AND " + PlantEntry._ID + "=?";
                String id = uri.getPathSegments().get(1);
                if (selectionArgs == null) selectionArgs = new String[]{id};
                else {
                    ArrayList<String> selectionArgsList = new ArrayList<String>();
                    selectionArgsList.addAll(Arrays.asList(selectionArgs));
                    selectionArgsList.add(id);
                    selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);
                }
                plantsUpdated = db.update(PlantEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (plantsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return plantsUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}