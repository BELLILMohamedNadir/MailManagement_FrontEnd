package com.example.gestiondecourrier.ui.service;

import static com.example.gestiondecourrier.ui.ui.MainActivity.NOTIFICATION_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.gestiondecourrier.R;
import com.example.gestiondecourrier.ui.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG="firebase";
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "firebase token generation onNewToken: ");
       // managementViewModel.updateToken(userId,token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "firebase From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());

        }
        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d(TAG, "firebase notification payload onMessageReceived: "+message.getNotification().getBody());
            showNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    void showNotification(String title,String body){

        NotificationManager notificationManager=null;
        //to create a channel if the sdk>android oreo
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(MainActivity.CHANNEL_ID,"my channel", NotificationManager
                    .IMPORTANCE_DEFAULT);
            notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //to go from notification to the fragment
        PendingIntent pi =new NavDeepLinkBuilder(this)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.receivedFragment)
                .createPendingIntent();

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,MainActivity.CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_doc)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManagerCompat nmc=NotificationManagerCompat.from(getApplicationContext());
        nmc.notify(NOTIFICATION_ID,builder.build());


    }
}
