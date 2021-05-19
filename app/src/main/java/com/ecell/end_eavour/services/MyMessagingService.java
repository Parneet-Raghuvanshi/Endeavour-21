package com.ecell.end_eavour.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.SplashScreen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> dataMap = remoteMessage.getData();
        String title = dataMap.get("title");
        String body = dataMap.get("body");
        String date = dataMap.get("date");
        String id = dataMap.get("id");

        String key = "userUserID";
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(),Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)){
            if (!sharedPreferences.getString(key,null).isEmpty()){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(sharedPreferences.getString(key,null)).child(id);
                databaseReference.keepSynced(true);
                databaseReference.child("title").setValue(title);
                databaseReference.child("body").setValue(body);
                databaseReference.child("date").setValue(date);
                databaseReference.child("id").setValue(id);

                DatabaseReference databaseReferencenotup = FirebaseDatabase.getInstance().getReference().child("notificationdots").child(sharedPreferences.getString(key,null));
                databaseReferencenotup.child("dotStatus").setValue("yes");
                databaseReferencenotup.keepSynced(true);

                sendNotification(title,body);
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void sendNotification(String title,String messageBody) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Notification";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.endvr_appicon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
