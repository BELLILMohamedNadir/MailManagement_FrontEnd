package com.example.gestiondecourrier.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {


    NotificationManager notificationManager;
    Notification notification;
    String title,aMessage;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        title=message.getNotification().getTitle();
        aMessage=message.getNotification().getBody();
        generateNotification(title,aMessage);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    public void generateNotification(String title,String aMessage){





    }
}
