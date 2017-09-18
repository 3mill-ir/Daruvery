package com.startup.hezare.startup;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rf on 30/07/2017.
 */


public class SendPostRequest extends AsyncTask<String, Void, String> {
    Context mContext;
    public SendPostRequest(Context mContext)
    {

        this.mContext=mContext;
    }
    public AsyncResponse delegate = null;

    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    String Message;
    String myStatus;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    String Result;

    public String getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(String myStatus) {
        this.myStatus = myStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    protected void onPreExecute() {
    }

    protected String doInBackground(String... URL) {

        try {

            URL url = new URL(URL[0]); // here is your URL path

          /* JSONObject postDataParams = new JSONObject();
           postDataParams.put("Tell", "09215482877");
           postDataParams.put("Password", "123456789");
          postDataParams.put("email", "abc@gmail.com");
          Log.e("params", postDataParams.toString());*/


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000 /* milliseconds */);
            conn.setConnectTimeout(60000 /* milliseconds */);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

                /*OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());

                writer.flush();
                writer.close();
                os.close();*/

            int responseCode = conn.getResponseCode();
            Log.i("MY TAG", " " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.i("MY TAG", "Connected!");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));

                App.setCookie(conn);


                StringBuffer sb=new StringBuffer("");
                String line ="";


                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                //Log.i("MY tag:", sb.toString());
                in.close();



                return sb.toString();

            } else {
                   return new String("false : " + responseCode);

            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {


            try {
                Result=result;
                String MSG=result;
                //replacing backslash with null and removing first and end quotation marks
                MSG=MSG.replace("\\","");
                MSG=MSG.replaceAll("^\"|\"$", "");
                //Log.i("Message from Server", MSG);
                JSONObject jsonObj=new JSONObject(MSG);

                // Getting JSON Array node
                JSONArray array = jsonObj.getJSONArray("Root");

                // looping through All Contacts
                for (int i = 0; i < array.length(); i++) {
                    JSONObject c = array.getJSONObject(i);
                    String Key = c.getString("Key");
                    String Text = c.getString("Text");

                    // tmp hash map for single Msg
                    HashMap<String, String> Msg = new HashMap<>();

                    // adding each child node to HashMap key => value
                    Msg.put("key", Key);
                    Msg.put("text", Text);

                    myStatus=Msg.get("key");
                    Message=Msg.get("text");

                    Log.i("myStatus:" ,Msg.get("key"));



                }
            } catch (final JSONException e) {
                Log.e("JJ", "Json parsing error: " + e.getMessage());
                // runOnUiThread(new Runnable() {
                // @Override
                //  public void run() {
                //Toast.makeText(getApplicationContext(),
                //        "Json parsing error: " + e.getMessage(),
                //      Toast.LENGTH_LONG).show();
            }
            // });
        delegate.processFinish();
    }
}