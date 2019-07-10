package com.bangalore.bosankus.finrec.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bangalore.bosankus.finrec.Activities.CompletedTaskActivity;
import com.bangalore.bosankus.finrec.Activities.HomeActivity;
import com.bangalore.bosankus.finrec.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class FinrecFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "Messaging Service";

    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        } else {
            Log.d(TAG, "Message Notification Body: " + Objects.requireNonNull(remoteMessage.getNotification()).getBody());
        }

        String title = remoteMessage.getData().get("title");

        String message = remoteMessage.getData().get("message");

        String imageUri = remoteMessage.getData().get("image");

        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");

        bitmap = getBitmapFromUrl(imageUri);

        sendNotification(title, message, bitmap, TrueOrFlase);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void sendNotification(String messageTitle, String messageBody, Bitmap image, String TrueOrFalse) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri soundUri = RingtoneManager.getDefaultUri(R.raw.notification);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(getNotificaitonIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(image))
                .addAction(R.drawable.ic_eye, "Watch Now", pendingIntent)
                .setAutoCancel(true)
                .setSound(soundUri);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel Human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificaitonIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        return useWhiteIcon ? R.drawable.ic_logo_noti : R.drawable.ic_logo_noti;
    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
