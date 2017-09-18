package com.startup.hezare.startup;


import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;


public class App extends Application {
    private static Context c;
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager;
    static String android_id ;
    @Override
    public void onCreate() {
        super.onCreate();
        c = getApplicationContext();
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();
        msCookieManager = new java.net.CookieManager();
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SessionManagment sessionManagment=new SessionManagment(c);
        sessionManagment.set_splash(true);
    }

    public static Context getContext() {
        return c;
    }

    public static String getAndroidId() {
        return android_id;
    }


    public static void setCookie(HttpURLConnection conn) {
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    public static void getCookieImage(ANRequest.MultiPartBuilder a) {
        if (msCookieManager.getCookieStore().getCookies().size() > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'

            a.addHeaders("Cookie", TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));

        }
    }

    public static void getCookieHttp(HttpURLConnection conn) {
        if (msCookieManager.getCookieStore().getCookies().size() > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'

            conn.setRequestProperty("Cookie",
                    TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));

        }
    }
}
