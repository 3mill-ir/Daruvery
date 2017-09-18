package com.startup.hezare.startup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
import co.ronash.pushe.PusheListenerService;

/**
 * Created by rf on 05/08/2017.
 */

public class MyPushListener extends PusheListenerService {

    String ImageURL;
    String RequestId;
    String Price;

    @Override
    public void onMessageReceived(JSONObject message, JSONObject content) {
        if (message.length() == 0)
            return; //json is empty
        android.util.Log.i("Pushe", "Custom json Message: " + message.toString()); //print json to logCat
        SessionManagment sessionManagment;
        /*sessionManagment =new SessionManagment(getApplicationContext());
        if(!sessionManagment.isLoggedIn()){
            return;
        }*/


        try {
            ImageURL = message.getString("Img");
            RequestId = message.getString("RequestId");
            Price = message.getString("Price");

            android.util.Log.i("Response from Pushe", "Json Message\n URL: " + ImageURL + "\n RequestId: " + RequestId + "\n Price:" + Price);
        } catch (JSONException e) {
            android.util.Log.e("Pushe", "Exception in parsing json", e);
        }


        Intent intent = new Intent(MyPushListener.this, ResponceActivity.class);
        intent.putExtra("URL", ImageURL);
        intent.putExtra("RequestId", RequestId);
        intent.putExtra("Price", Price);

        PendingIntent contentIntent = PendingIntent.getActivity(MyPushListener.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(MyPushListener.this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.daruvery_marker)
                .setContentTitle("تایید سفارش")
                .setContentText("برای تایید سفارش کلیک کنید!")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        Random rand = new Random();

        int n = rand.nextInt(50) + 1;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(n + 1, b.build());

    }
}