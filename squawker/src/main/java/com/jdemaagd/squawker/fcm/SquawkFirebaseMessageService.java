package com.jdemaagd.squawker.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jdemaagd.squawker.MainActivity;
import com.jdemaagd.squawker.R;
import com.jdemaagd.squawker.provider.SquawkContract;
import com.jdemaagd.squawker.provider.SquawkProvider;

import java.util.Map;

/**
 * Listens for squawk FCM messages both in background and foreground
 * responds appropriately depending on type of message
 */
public class SquawkFirebaseMessageService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR = SquawkContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SquawkContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SquawkContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SquawkContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static String LOG_TAG = SquawkFirebaseMessageService.class.getSimpleName();

    /**
     * Called when message is received
     *
     * @param remoteMessage Object representing message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Types of messages: data messages and notification messages
        // Data messages: are handled in onMessageReceived
        //                whether app is in foreground or background
        //                type traditionally used with FCM
        // Notification messages: only received in onMessageReceived when app is in foreground
        //      When app is in background an automatically generated notification is displayed
        //
        // When user taps on notification they are returned to the app
        // Messages containing both notification and data payloads are treated as notification messages
        // Firebase console always sends notification messages:
        //      https://firebase.google.com/docs/cloud-messaging/concept-options\

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            sendNotification(data);
            insertSquawk(data);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(LOG_TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    /**
     * Insert single squawk into database
     *
     * @param data Map which has message data in it
     */
    private void insertSquawk(final Map<String, String> data) {
        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(SquawkContract.COLUMN_AUTHOR, data.get(JSON_KEY_AUTHOR));
                newMessage.put(SquawkContract.COLUMN_MESSAGE, data.get(JSON_KEY_MESSAGE).trim());
                newMessage.put(SquawkContract.COLUMN_DATE, data.get(JSON_KEY_DATE));
                newMessage.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(JSON_KEY_AUTHOR_KEY));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);
                return null;
            }
        };

        insertSquawkTask.execute();
    }

    /**
     * Create and show a simple notification containing received FCM message
     *
     * @param data Map which has message data in it
     */
    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder);
    }

    /**
     * Persist token to third-party servers
     * <p>
     * Modify this method to associate user FCM InstanceID token
     * with any server-side account maintained by your application
     *
     * @param token new token
     */
    private void sendRegistrationToServer(String token) {
        // if you were to build a server that stores users token information
        // this is where you would send the token to the server
    }
}