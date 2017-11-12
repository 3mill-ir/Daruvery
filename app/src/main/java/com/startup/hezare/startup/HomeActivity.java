package com.startup.hezare.startup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.UtilClasses.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import co.ronash.pushe.Pushe;

//import com.roughike.bottombar.BottomBar;

public class HomeActivity extends AppCompatActivity implements AsyncResponse {
    private static final String TAG = "Home";
    private static long back_pressed = 0L;
    int TAKE_PHOTO_CODE = 0;
    int SELECT_PICTURE = 3;
    File newfile;
    SendPostRequest sendPostRequest;
    Login login;
    SessionManagment sessionManagment;
    String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Pushe.initialize(App.getContext(), false);
        Pushe.setNotificationOn(this);



        sessionManagment = new SessionManagment(getApplicationContext());

        login = new Login(getApplicationContext());
        login.execute(Utils.Main_URL + "api/AndroidAccount/Login?Tell=" + sessionManagment.getUserDetails().get("phone") + "&Password=" + sessionManagment.getUserDetails().get("password") + "&AndroidId=" + App.getAndroidId());


        AppCompatButton btn_opencamera = (AppCompatButton) findViewById(R.id.btn_capture);
        AppCompatButton btn_gallery = (AppCompatButton) findViewById(R.id.btn_choose_from_gallery);
        TextView txt_guide = (TextView) findViewById(R.id.txt_guide);
        btn_gallery.setTypeface(App.BYekan);
        btn_opencamera.setTypeface(App.BYekan);
        txt_guide.setTypeface(App.iransans);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/daruvery/";
        File newdir = new File(dir);
        newdir.mkdirs();

        /*if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        btn_opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.


                Date d = new Date();
                CharSequence ImageDir = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                file_path = dir + ImageDir + ".jpg";
                sessionManagment.createpathSession(file_path);
                Log.d("file", file_path);

                newfile = new File(file_path);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_PICTURE);
            }
        });

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu m = bottomNavigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //the method we have create in activity
            CustomTypefaceSpan.applyFontToMenuItem(mi,getApplicationContext());
        }
        bottomNavigationView.setSelectedItemId(R.id.tab_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    case R.id.tab_map:
                        intent = new Intent(getBaseContext(), MapsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_list:
                        intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_share:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                        intent1.putExtra("sms_body", "می توانید این برنامه را از بازار دریافت کنید \n لینک دریافت برنامه در کافه بازار:");
                        startActivity(intent1);
                        break;
                    case R.id.tab_about:
                        intent = new Intent(getBaseContext(), More2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });



        /*bottomBar = (BottomBar) findViewById(R.id.bottomBarhome);
        //setting the Default Tab Selected
        bottomBar.setDefaultTab(R.id.tab_home);
        bottomBar.setItems(R.xml.bottombar_tabs);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Intent intent;
                switch (tabId) {
                    case R.id.tab_map:
                        intent = new Intent(getBaseContext(), MapsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_list:
                        intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_share:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                        intent1.putExtra("sms_body", "می توانید این برنامه را از بازار دریافت کنید \n لینک دریافت برنامه در کافه بازار:");
                        startActivity(intent1);
                        break;
                    case R.id.tab_about:
                        intent = new Intent(getBaseContext(), More2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
            }
        });*/
    }

    private HashMap<String, String> Parsing() {
        HashMap<String, String> info = new HashMap<>();

        try {
            String MSG = sendPostRequest.Result;
            //replacing backslash with null and removing first and end quotation marks
            MSG = MSG.replace("\\", "");
            MSG = MSG.replaceAll("^\"|\"$", "");
            //Log.i("Recived Message", MSG);
            JSONObject jsonObj = new JSONObject(MSG);

            // Getting JSON Array node
            JSONObject array = jsonObj.getJSONObject("Root");

            //JSONObject c = array.getJSONObject(i);
            String Name = array.getString("FirstName");
            String Family = array.getString("LastName");
            String Address = array.getString("Address");

            // adding each child node to HashMap key => value
            info.put("name", Name);
            info.put("family", Family);
            info.put("address", Address);

        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        } finally {

            return info;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {

            Uri selectedImage = data.getData();
            Log.d("CameraDemo", selectedImage.toString());
            File f = new File(selectedImage.toString());
            Log.d("CameraDemo", f.getPath());


            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Log.d("imagePath", imagePath.toString());


            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            //selectedImage.setImageBitmap(bitmap);
            try {
                FileOutputStream out = new FileOutputStream(selectedImage);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            String new_path=null;
            try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            InputStream in = new ByteArrayInputStream(bos.toByteArray());

            //you can create a new file name "test.jpg" in sdcard folder.
            new_path=Environment.getExternalStorageDirectory()
                        + File.separator + "test.jpg";
            File f1 = new File(new_path);

                f1.createNewFile();
                //write the bytes in file
                FileOutputStream fo = new FileOutputStream(f1);
                fo.write(bos.toByteArray());
                // remember close de FileOutput
                fo.close();
            } catch (Exception e) {
                Log.e("tag", e.toString());
            }
            sessionManagment.createpathSession(new_path);
            Intent intent = new Intent(getBaseContext(), Address_Detail_Activity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            String new_path=null;
            try {
            Bitmap bmp = BitmapFactory.decodeFile(file_path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            //InputStream in = new ByteArrayInputStream(bos.toByteArray());

            //you can create a new file name "test.jpg" in sdcard folder.
            new_path=Environment.getExternalStorageDirectory()
                        + File.separator + "test.jpg";
            File f1 = new File(new_path);

                f1.createNewFile();
                //write the bytes in file
                FileOutputStream fo = new FileOutputStream(f1);
                fo.write(bos.toByteArray());
                // remember close de FileOutput
                fo.close();
            } catch (Exception e) {
                Log.e("tag", e.toString());
            }
            sessionManagment.createpathSession(new_path);

            Log.d("CameraDemo", "Pic saved" + sessionManagment.getpath().get("path"));
            Intent intent = new Intent(getBaseContext(), Address_Detail_Activity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_CANCELED) {
            if (newfile.exists()) {
                if (newfile.delete()) {
                    Log.d("CameraDemo", "file deleted");
                } else {
                    Log.d("CameraDemo", "file not deleted");
                }
            }
        }
    }

    @Override
    public void processFinish() {
        //Log.i("MyTAG", ""+ sendPostRequest.getResult());
        Log.i("MyTAG", "" + Parsing().get("name"));
        //Log.i("MyTAG",sendPostRequest.getMessage() + " "+ sendPostRequest.getMyStatus());
        String name = "";
        name += Parsing().get("name");

        if (!name.equals(null)) {
            Log.i("Get data", "OK");
            sessionManagment.createUserDetails(Parsing().get("name"), Parsing().get("family"), Parsing().get("address"));
            Log.i("Shared Pref", sessionManagment.getUserDetailsname().get("name") + "  " + sessionManagment.getUserDetailsname().get("family") + "  " + sessionManagment.getUserDetailsname().get("address") + "saved");

        } else {
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "برای خروج از برنامه دوباره فشار دهید", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    public class Login extends AsyncTask<String, Void, String> {
        public AsyncResponse delegate = null;
        Context mContext;
        String Message;
        String myStatus;
        String Result;

        public Login(Context mContext) {

            this.mContext = mContext;
        }

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
                //Log.i("MY TAG", " " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    //Log.i("MY TAG", "Connected!");
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
                    //Log.i("MY tag:", sb.toString());
                    in.close();


                    return sb.toString();

                } else {
                    return ("false : " + responseCode);

                }
            } catch (Exception e) {
                return ("Exception: " + e.getMessage());
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

                    //Log.i("myStatus:", Msg.get("key"));

                }
            } catch (final JSONException e) {
                Log.e("JJ", "Json parsing error: " + e.getMessage());

            }
            String state = "";
            state += myStatus;

            if (state.equals("OK")) {
                sendPostRequest = new SendPostRequest(getApplicationContext());
                sendPostRequest.delegate = HomeActivity.this;
                sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/GetSpecifics");
                Log.i("sign in", "OK");

            } else {
                Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
            }


        }

    }



}
