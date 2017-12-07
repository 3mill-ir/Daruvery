package com.startup.hezare.startup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    Animation               animation_login_icon;
    ImageView               img_view;
    TextView                txt_view_Register;
    Animation animation_zoom_in;
    SessionManagment sessionManagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        sessionManagment=new SessionManagment(getApplicationContext());
        Typeface BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");

        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/ANegaarBold.ttf");
        img_view = (ImageView) findViewById(R.id.imgLogo);
        txt_view_Register = (TextView) findViewById(R.id.splash_text);
        txt_view_Register.setTypeface(font);
        txt_view_Register.setVisibility(View.GONE);
        animation_login_icon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_from_bottom);
        animation_zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        //Animation animation_zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        if(sessionManagment.show_splash()) {
            //Log.i("sessionManagment", "onCreate: "+String.valueOf(sessionManagment.show_splash()));
            Timer t = new Timer(false);
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String next = "<font color='#EE0000'>وری</font>";
                            txt_view_Register.setText(Html.fromHtml("با دارو" + next + " به آسانی دارو بخرید"));
                            txt_view_Register.setVisibility(View.VISIBLE);
                            img_view.setVisibility(View.VISIBLE);
                            img_view.startAnimation(animation_login_icon);
                            txt_view_Register.startAnimation(animation_zoom_in);


                            //txt_view_Register.setGravity(View.VISIBLE);
                        }
                    });
                }
            }, 500);//500

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Log.i("Shared Pref", sessionManagment.getUserDetails().get("phone") + "  " + sessionManagment.getUserDetails().get("password") + "restored");
                    Log.i("Shared Pref", String.valueOf(sessionManagment.isLoggedIn()));
                    if (sessionManagment.isLoggedIn()) {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        // close this activity

                    } else {
                        Intent i = new Intent(SplashActivity.this, Sign_in_Activity.class);
                        startActivity(i);
                        // close this activity
                    }

                    finish();
                }
            }, 3000);//3000
        }
        else
        {
            if (sessionManagment.isLoggedIn()) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                // close this activity

            } else {
                Intent i = new Intent(SplashActivity.this, Sign_in_Activity.class);
                startActivity(i);
                // close this activity
            }
            finish();
        }


    }

    //reciver for downloading apk update
    private class DownloadReceiver extends ResultReceiver {

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                //Log.i("percent",String.valueOf(progress));
                //mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    //mProgressDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/daruvery.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        }
    }

}
