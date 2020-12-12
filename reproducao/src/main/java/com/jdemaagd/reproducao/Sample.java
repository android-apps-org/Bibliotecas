package com.jdemaagd.reproducao;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.JsonReader;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Sample {

    private int mSampleID;
    private String mComposer;
    private String mTitle;
    private String mUri;
    private String mAlbumArtID;

    private Sample(int sampleID, String composer, String title, String uri, String albumArtID) {
        mSampleID = sampleID;
        mComposer = composer;
        mTitle = title;
        mUri = uri;
        mAlbumArtID = albumArtID;
    }

    /**
     * Get portrait of composer for sample by sample ID
     *
     * @param context The application context
     * @param sampleID The sample ID
     * @return The portrait Bitmap
     */
    static Bitmap getComposerArtBySampleID(Context context, int sampleID) {
        Sample sample = Sample.getSampleByID(context, sampleID);
        int albumArtID = context.getResources().getIdentifier(
                sample != null ? sample.getAlbumArtID() : null, "drawable",
                context.getPackageName());

        return BitmapFactory.decodeResource(context.getResources(), albumArtID);
    }

    /**
     * Get single sample by its ID
     *
     * @param context application context
     * @param sampleID sample ID
     * @return sample object
     */
    static Sample getSampleByID(Context context, int sampleID) {
        JsonReader reader;
        try {
            reader = readJSONFile(context);
            reader.beginArray();
            while (reader.hasNext()) {
                Sample currentSample = readEntry(reader);
                if (currentSample.getSampleID() == sampleID) {
                    reader.close();
                    return currentSample;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets ArrayList of IDs for all Samples from JSON file
     *
     * @param context The application context
     * @return The ArrayList of all sample IDs
     */
    static ArrayList<Integer> getAllSampleIDs(Context context) {
        JsonReader reader;
        ArrayList<Integer> sampleIDs = new ArrayList<>();

        try {
            reader = readJSONFile(context);
            reader.beginArray();
            while (reader.hasNext()) {
                sampleIDs.add(readEntry(reader).getSampleID());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sampleIDs;
    }

    /**
     * Single sample from JSON file
     *
     * @param reader JSON reader object pointing to single sample JSON object
     * @return Sample JsonReader is pointing to
     */
    private static Sample readEntry(JsonReader reader) {
        Integer id = -1;
        String composer = null;
        String title = null;
        String uri = null;
        String albumArtID = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        title = reader.nextString();
                        break;
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "composer":
                        composer = reader.nextString();
                        break;
                    case "uri":
                        uri = reader.nextString();
                        break;
                    case "albumArtID":
                        albumArtID = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Sample(id, composer, title, uri, albumArtID);
    }

    /**
     * Create JsonReader object that points to JSON array of samples
     *
     * @param context application context
     * @return JsonReader object pointing to the JSON array of samples
     * @throws IOException thrown if sample file cannot be found
     */
    private static JsonReader readJSONFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        String uri = null;

        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith(".exolist.json")) {
                    uri = "asset:///" + asset;
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, R.string.sample_list_load_error, Toast.LENGTH_LONG)
                    .show();
        }

        String userAgent = Util.getUserAgent(context, "ClassicalMusicQuiz");
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } finally {
            Util.closeQuietly(dataSource);
        }

        return reader;
    }


    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    String getUri() {
        return mUri;
    }

    void setUri(String uri) {
        mUri = uri;
    }

    int getSampleID() {
        return mSampleID;
    }

    void setSampleID(int sampleID) {
        mSampleID = sampleID;
    }

    String getComposer() {
        return mComposer;
    }

    void setComposer(String composer) {
        mComposer = composer;
    }

    String getAlbumArtID() {
        return mAlbumArtID;
    }

    void setAlbumArtID(String albumArtID) {
        mAlbumArtID = albumArtID;
    }
}