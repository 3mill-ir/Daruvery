package com.startup.hezare.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.startup.hezare.startup.UtilClasses.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ResponceActivity extends Activity implements AsyncResponse{

    SendPostRequest sendPostRequest;
    ProgressDialog progressDialog;

    Button btn_accept;
    Button btn_reject;
    TextView price;
    ImageView image;
    Login login1;
    int View_clicked=0;
    SessionManagment sessionManagment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responce);
        init();
        sessionManagment=new SessionManagment(getApplicationContext());

        login1 = new Login();
        login1.execute(Utils.Main_URL + "api/AndroidAccount/Login?Tell=" + sessionManagment.getUserDetails().get("phone") + "&Password=" + sessionManagment.getUserDetails().get("password") + "&AndroidId=" + App.getAndroidId());

        sendPostRequest=new SendPostRequest(getApplicationContext());
        sendPostRequest.delegate=ResponceActivity.this;

        price.setText(getIntent().getStringExtra("Price") + "  تومان");
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("URL")).placeholder(R.drawable.fullimage80)
                .fit()
                //.transform(new CropCircleTransformation())
                .transform(new RoundedCornersTransformation(100,0))
                .into(image);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                sendPostRequest.execute(Utils.Main_URL + "Admin3mill/Request/ClientRejectAcceptRequest?RequestId=" + getIntent().getStringExtra("RequestId") + "&Type=Accept");
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                sendPostRequest.execute(Utils.Main_URL + "Admin3mill/Request/ClientRejectAcceptRequest?RequestId=" + getIntent().getStringExtra("RequestId") + "&Type=Reject");

            }
        });
    }

    @Override
    public void processFinish() {
        Log.i("MyTAG", ""+ sendPostRequest.getResult());
        Log.i("MyTAG", ""+ sendPostRequest.getMessage());

        String state = "";
        state += sendPostRequest.getMyStatus();

        if (state.equals("OK")) {
            Log.i("PUSH", "OK");
            Toast.makeText(getApplicationContext(), "درخواست شما با موفقیت ارسال گردید.", Toast.LENGTH_LONG).show();

        } else if(state.equals("NOK")){
            Log.i("PUSH", "false");
            Toast.makeText(getApplicationContext(), "مشکل در ارسال درخواست", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_LONG).show();

        }
        progressDialog.dismiss();
        finish();
    }

    private void init() {
        price = (TextView) findViewById(R.id.price);
        image = (ImageView) findViewById(R.id.thumbnail);
        btn_accept=(Button)findViewById(R.id.btn_accept);
        btn_reject=(Button)findViewById(R.id.btn_reject);

        progressDialog = new ProgressDialog(ResponceActivity.this, ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(R.style.AppTheme);
        progressDialog.setCancelable(false);
        //progressDialog.setIndeterminate(true);
        progressDialog.setMessage("در حال ارسال درخواست...");

        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan.ttf");
        Typeface BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");

        btn_accept.setTypeface(BHoma);
        btn_reject.setTypeface(BHoma);

        price.setTypeface(BYekan);

    }



    public class Login extends AsyncTask<String, Void, String> {

        String Message;
        String myStatus;
        String Result;

        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            Result = result;
        }

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

                java.net.URL url = new URL(URL[0]); // here is your URL path

          /* JSONObject postDataParams = new JSONObject();
           postDataParams.put("Tell", "09215482877");
           postDataParams.put("Password", "123456789");
          postDataParams.put("email", "abc@gmail.com");
          Log.e("params", postDataParams.toString());*/


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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


                    StringBuffer sb = new StringBuffer("");
                    String line = "";


                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    Log.i("MY tag:", sb.toString());
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
            // tmp hash map for single Msg
            HashMap<String, String> Msg = new HashMap<>();
            try {
                Result = result;
                String MSG = result;
                //replacing backslash with null and removing first and end quotation marks
                MSG = MSG.replace("\\", "");
                MSG = MSG.replaceAll("^\"|\"$", "");
                Log.i("Message from Server", MSG);
                JSONObject jsonObj = new JSONObject(MSG);

                // Getting JSON Array node
                JSONArray array = jsonObj.getJSONArray("Root");

                // looping through All Contacts
                for (int i = 0; i < array.length(); i++) {
                    JSONObject c = array.getJSONObject(i);
                    String Key = c.getString("Key");
                    String Text = c.getString("Text");

                    // adding each child node to HashMap key => value
                    Msg.put("key", Key);
                    Msg.put("text", Text);

                    myStatus = Msg.get("key");
                    Message = Msg.get("text");

                    Log.i("myStatus:", Msg.get("key"));

                }
            } catch (final JSONException e) {
                Log.e("JJ", "Json parsing error: " + e.getMessage());

            }
            String state = "";
            state += myStatus;

            if (state.equals("OK")) {
                Log.i("sign in", "OK");

            } else {
                Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
            }


        }

    }

}
