package com.startup.hezare.startup.UtilClasses;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.startup.hezare.startup.DownloadService;
import com.startup.hezare.startup.SessionManagment;

import java.io.File;


public class Utils {
    public static String Main_URL = "http://daruvery.ir/";
    private Activity activity;

    public Utils(Activity _activity) {
        this.activity = _activity;
    }

    public void check_Version() throws PackageManager.NameNotFoundException {
        PackageManager manager = activity.getPackageManager();
        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
        final String version = info.versionName;

        final SessionManagment sessionManagment = new SessionManagment(activity);


        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "http://3mill.ir/download/appcheck?path=mobile/android/daruvery&secretKey=3mill186";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String availableVersion = response.replaceAll("^\"|\"$", "");
                        Log.i("Response is ", availableVersion);
                        Log.i("compare result", String.valueOf(compareVersionNames(version, availableVersion)));
                        if (compareVersionNames(version, availableVersion) == -1) {
                            Log.i("Version of Available", String.valueOf(availableVersion));
                            Log.i("Version of current App ", String.valueOf(version));

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("نسخه جدید اپلیکیشن موجود است \n تمایل به بروزرسانی دارید؟")
                                    .setCancelable(false);
                            builder.setPositiveButton("آره", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(activity, DownloadService.class);
                                    intent.putExtra("url", "http://3mill.ir/download/AppSend?path=mobile/android/daruvery&secretkey=3mill186");
                                    intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                    activity.startService(intent);
                                    sessionManagment.set_showdialog(false);
                                }
                            });
                            builder.setNegativeButton("نه", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    sessionManagment.set_showdialog(false);
                                    Log.i("showDialog", String.valueOf(sessionManagment.showDialog()));
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "That didn't work!");
            }
        });
        stringRequest.setShouldCache(false);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
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
                    activity.startActivity(intent);

                }
            }
        }
    }


}
