package com.jdemaagd.horadocha;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jdemaagd.horadocha.idlingresource.SimpleIdlingResource;
import com.jdemaagd.horadocha.model.Tea;

import java.util.ArrayList;

class ImageDownloader {

    private static final int DELAY_MILLIS = 3000;

    final static ArrayList<Tea> mTeas = new ArrayList<>();

    interface DelayerCallback{
        void onDone(ArrayList<Tea> teas);
    }

    static void downloadImage(Context context, final DelayerCallback callback,
                              @Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        String text = context.getString(R.string.loading_msg);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mTeas.add(new Tea(context.getString(R.string.black_tea_name), R.drawable.black_tea));
        mTeas.add(new Tea(context.getString(R.string.green_tea_name), R.drawable.green_tea));
        mTeas.add(new Tea(context.getString(R.string.white_tea_name), R.drawable.white_tea));
        mTeas.add(new Tea(context.getString(R.string.oolong_tea_name), R.drawable.oolong_tea));
        mTeas.add(new Tea(context.getString(R.string.honey_lemon_tea_name), R.drawable.honey_lemon_tea));
        mTeas.add(new Tea(context.getString(R.string.chamomile_tea_name), R.drawable.chamomile_tea));

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (callback != null) {
                callback.onDone(mTeas);
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        }, DELAY_MILLIS);
    }
}