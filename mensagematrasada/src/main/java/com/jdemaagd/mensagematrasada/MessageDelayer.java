package com.jdemaagd.mensagematrasada;

import android.os.Handler;
import androidx.annotation.Nullable;

import com.jdemaagd.mensagematrasada.idle.SimpleIdlingResource;

class MessageDelayer {

    private static final int DELAY_MILLIS = 3000;

    interface DelayerCallback {
        void onDone(String text);
    }

    static void processMessage(final String message, final DelayerCallback callback,
                               @Nullable final SimpleIdlingResource idlingResource) {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (callback != null) {
                callback.onDone(message);
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        }, DELAY_MILLIS);
    }
}