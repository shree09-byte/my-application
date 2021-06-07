package com.example.realestate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.realestate.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMesseging extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID";
    private FirebaseAuth firebaseAuth;
    private String firebaseUser;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = Prevalent.currentonlineUser.getPhone();

        String notificationType = remoteMessage.getData().get("notificationType");
        if (notificationType.equals("NewOrder")){
            String buyerUid = remoteMessage.getData().get("buyerUid");
            String sellerUid = remoteMessage.getData().get("sellerUid");
            String orderUid = remoteMessage.getData().get("orderUid");
            String notificationTitle = remoteMessage.getData().get("notificationTitle");
            String notificationDescription = remoteMessage.getData().get("notificationDescription");

            if (firebaseUser !=null && firebaseAuth.getUid().equals(sellerUid)){

                showNotification(orderUid, sellerUid, buyerUid, notificationTitle, notificationDescription, notificationType);

            }
        }
        if (notificationType.equals("NewOrder")){
            String buyerUid = remoteMessage.getData().get("buyerUid");
            String sellerUid = remoteMessage.getData().get("sellerUid");
            String orderUid = remoteMessage.getData().get("orderUid");
            String notificationTitle = remoteMessage.getData().get("notificationTitle");
            String notificationDescription = remoteMessage.getData().get("notificationDescription");

            if (firebaseUser !=null && firebaseAuth.getUid().equals(buyerUid)){
                showNotification(orderUid, sellerUid, buyerUid, notificationTitle, notificationDescription, notificationType);
            }
        }

    }

    private void showNotification(String orderUid, String sellerUid, String buyerrUid, String notificationTitle, String notificationDescription,String notificationType){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setupNotificationChannel(notificationManager);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.common_google_signin_btn_icon_dark);

        Uri notificationSoundUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSound(notificationSoundUrl)
                .setAutoCancel(true);

                notificationManager.notify(notificationID, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannel(NotificationManager notificationManager) {
            CharSequence channelName = "Some Sample Text";
            String channelDescription = "Channel Descriptioni here";

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        if (notificationManager !=null){
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
