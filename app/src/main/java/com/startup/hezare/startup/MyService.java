package com.startup.hezare.startup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by rf on 23/08/2017.
 */

public class MyService extends Service {


SessionManagment sessionManagment;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("onTaskRemoved called");
        super.onTaskRemoved(rootIntent);
        //sessionManagment=new SessionManagment(getApplicationContext());
        //do something you want
        //stop service
        //sessionManagment.set_splash(false);
        //Log.i("sessionManagment", "onPause: "+String.valueOf(sessionManagment.show_splash()));
        this.stopSelf();
    }
}
