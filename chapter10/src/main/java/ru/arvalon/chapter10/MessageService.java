package ru.arvalon.chapter10;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static ru.arvalon.chapter10.StartActivity.*;

/** Created by arvalon on 07.02.2018 */

public class MessageService extends FirebaseMessagingService {

    private static final int SIMPLE_NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "my_notification_channel_name";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG,"Farebase Cloud Message");

        if (remoteMessage.getData().size()>0){
            Log.d(TAG,"Date: " + remoteMessage.getData());
            if (remoteMessage.getFrom().equals(TOPIC_NAME)){

                Log.d(TAG,"Message: " + remoteMessage.getData().get(TEXT_KEY));

                Intent intent = new Intent(MSG_DELIVERY);

                intent.putExtra(USERNAME_KEY,remoteMessage.getData().get(USERNAME_KEY));
                intent.putExtra(TEXT_KEY,remoteMessage.getData().get(TEXT_KEY));

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }

        if (remoteMessage.getNotification()!=null){

            Log.d(TAG,"Notification title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG,"Notification text: " + remoteMessage.getNotification().getBody());

            Log.d(TAG,"Map<String,String>");
            for (Map.Entry<String, String> stringStringEntry : remoteMessage.getData().entrySet()) {
                Log.d(TAG,"Key = "+stringStringEntry.getKey()+" Value = "+stringStringEntry.getValue());
            }

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel =
                        new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                NOTIFICATION_CHANNEL_NAME,
                                NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                nm.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.arrow_down_float)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());

            Notification notification = builder.build();

            nm.notify(SIMPLE_NOTIFICATION_ID,notification);
        }
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d(TAG,getClass().getSimpleName()+ " onMessageSent " + s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.d(TAG,getClass().getSimpleName()+ " onSendError " + s+" Error "+e);
    }
}