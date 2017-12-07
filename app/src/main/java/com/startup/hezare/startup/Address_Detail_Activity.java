package com.startup.hezare.startup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.UtilClasses.Utils;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Address_Detail_Activity extends FragmentActivity implements OnMapReadyCallback {

    String Detail_Address = "";
    double longitude = 0;
    double latitude = 0;
    Typeface BYekan;
    Typeface BHoma;
    @InjectView(R.id.btn_send_request)
    AppCompatButton btn_send_request;
    @InjectView(R.id.radio_address)
    RadioGroup radio_address;
    @InjectView(R.id.new_address)
    EditText new_address;
    @InjectView(R.id.current_location)
    RadioButton current_location;
    @InjectView(R.id.new_location)
    RadioButton new_location;
    @InjectView(R.id.guide)
    TextView guide;
    int height;
    int width;
    SessionManagment sessionManagment;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    AlertDialog Response_dialog;
    SendPostRequestImage sendPostRequestImage;
    private GoogleMap mMap;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        sessionManagment.set_splash(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Main", "OnResume:");


        LocationManager lm = (LocationManager) getSystemService(Address_Detail_Activity.LOCATION_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(Address_Detail_Activity.this);
            dialog.setMessage("تنظیمات GPS گوشی شما غیر فعال است\n آیا می خواهید فعال کنید؟");
            dialog.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(viewIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("خیر", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
        // Show rationale and request permission.

        final Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.i("Location Manager:", "Lat:" + latitude + "Long:" + longitude);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        ButterKnife.inject(this);
        init();
        sessionManagment = new SessionManagment(getApplicationContext());

        //check for google play services
        if (!isGooglePlayServicesAvailable(this)) {
            Toast.makeText(getApplicationContext(), "تنظیمات GooglePlay Services را بررسی کنید", Toast.LENGTH_LONG).show();
        }
        //Getting Device Display Metrics
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan.ttf");
        BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");


        btn_send_request.setOnClickListener(new View.OnClickListener() {
            //RadioButton checkedRadioButton = (RadioButton) radio_address.findViewById(checkedId);
            @Override
            public void onClick(View v) {
                if (latitude != 0 && longitude != 0) {

                    if (current_location.isChecked()) {
                        progressDialog = new ProgressDialog(Address_Detail_Activity.this, ProgressDialog.THEME_HOLO_DARK);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        //progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("در حال ارسال...");
                        progressDialog.show();

                        Detail_Address = sessionManagment.getUserDetailsname().get("address");
                        Log.i("Current Address:", "" + sessionManagment.getUserDetailsname().get("address"));
                        sendPostRequestImage = new SendPostRequestImage();
                        sendPostRequestImage.execute(Utils.Main_URL + "Request/SendRequestToServer");

                    } else if (new_location.isChecked()) {
                        if (!(new_address.getText().toString().isEmpty())) {
                            progressDialog = new ProgressDialog(Address_Detail_Activity.this, ProgressDialog.THEME_HOLO_DARK);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            //progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("در حال ارسال...");
                            progressDialog.show();

                            Detail_Address = new_address.getText().toString();
                            sendPostRequestImage = new SendPostRequestImage();
                            sendPostRequestImage.execute(Utils.Main_URL + "/Request/SendRequestToServer");
                        } else {
                            new_address.setError("فیلد آدرس خالی است");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "لطفا یکی از گزینه های آدرس را انتخاب کنید", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Address_Detail_Activity.this, "موقعیت خود را از روی نقشه انتخاب کنید", Toast.LENGTH_SHORT).show();
                }
            }

        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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


    }


    private void init() {
        new_address.setTypeface(App.BYekan);
        btn_send_request.setTypeface(App.BHoma);

        current_location.setTypeface(App.BYekan);
        new_location.setTypeface(App.BYekan);

        guide.setTypeface(App.BYekan);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }
        if (latitude != 0 && longitude != 0) {
            // show my location in background
            LatLng MyLocation = new LatLng(latitude, longitude);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(MyLocation, 13);
            mMap.moveCamera(update);
        } else {
            // Add a marker in Urmia and move the camera
            LatLng urmia = new LatLng(37.54023, 45.069767);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(urmia, 13);
            mMap.moveCamera(update);
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                LinearLayout layout = (LinearLayout) findViewById(R.id.map_layout);
                ViewGroup.LayoutParams params = layout.getLayoutParams();
                params.height = height;
                params.width = width;

                ScrollView layout2 = (ScrollView) findViewById(R.id.Address_layout);
                ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                params2.height = 0;
                params2.width = width;
                layout.setLayoutParams(params);
                layout2.setLayoutParams(params2);

                mMap.clear();
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                //        + latLng.latitude + "\nLong: " + latLng.longitude, Toast.LENGTH_LONG).show();

                LatLng location = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(location).title("موقعیت فعلی شما"));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 15);
                mMap.moveCamera(update);

                AlertDialog.Builder builder = new AlertDialog.Builder(Address_Detail_Activity.this);

                builder.setMessage("انتخاب موقعیت به عنوان آدرس جدید؟");
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScrollView layout = (ScrollView) findViewById(R.id.Address_layout);
                        ViewGroup.LayoutParams params = layout.getLayoutParams();
                        params.height = (height * 6) / 10;
                        params.width = width;

                        LinearLayout layout2 = (LinearLayout) findViewById(R.id.map_layout);
                        ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                        params2.height = (height * 4) / 10;
                        params2.width = width;

                        layout.setLayoutParams(params);
                        layout2.setLayoutParams(params2);

                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        Log.i("Custom Location", "Lat:" + String.valueOf(latitude) + "Long:" + longitude);

                        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                        //        + latLng.latitude + "\nLong: " + latLng.longitude, Toast.LENGTH_LONG).show();


                    }
                });
                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.map_layout);
                        ViewGroup.LayoutParams params = layout.getLayoutParams();
                        params.height = height;
                        params.width = width;

                        ScrollView layout2 = (ScrollView) findViewById(R.id.Address_layout);
                        ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                        params2.height = 0;
                        params2.width = width;
                        layout.setLayoutParams(params);
                        layout2.setLayoutParams(params2);
                    }
                });
                builder.setCancelable(false);

                dialog = builder.create();
                dialog.show();

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                dialog.show();
                return true;
            }
        });
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    public class SendPostRequestImage extends AsyncTask<String, Void, String> {

        String Result;
        protected void onPreExecute() {

        }
        protected String doInBackground(String... URL) {

            Result = "successful";

            return Result;
        }
        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                ANRequest.MultiPartBuilder a = new ANRequest.MultiPartBuilder(Utils.Main_URL + "Request/SendRequestToServer");

                App.getCookieImage(a);

                a.addMultipartParameter("Address", Detail_Address);
                a.addMultipartParameter("Lat", String.valueOf(latitude));
                a.addMultipartParameter("Long", String.valueOf(longitude));
                a.addMultipartFile("Img", new File(sessionManagment.getpath().get("path")));
                Log.e("Address", Detail_Address);
                Log.e("Lat", String.valueOf(latitude));
                Log.e("Long", String.valueOf(longitude));
                Log.e("Img", new File(sessionManagment.getpath().get("path")).getAbsolutePath());
                a.setPriority(Priority.MEDIUM);
                a.build().setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.e("Image", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.e("Image", " bytesSent : " + bytesSent);
                        Log.e("Image", " bytesReceived : " + bytesReceived);
                        Log.e("Image", " isFromCache : " + isFromCache);
                    }
                })

                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(okhttp3.Response response) {
                                AndroidNetworking.forceCancelAll();
                                AndroidNetworking.shutDown();
                                if (response != null) {
                                    if (response.isSuccessful()) {
                                        Log.e("Image", "response is successful");
                                        try {

                                            //Log.e("Image", "response : " + response.body().source().readUtf8());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Address_Detail_Activity.this);
                                            builder.setTitle("ارسال درخواست موفقیت آمیز");
                                            builder.setMessage("درخواست شما با موفقیت ثبت شد \n کد پیگیری شما در بخش لیست سفارشات قابل مشاهده است");
                                            builder.setCancelable(false);

                                            builder.setPositiveButton("تمام", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Response_dialog.cancel();
                                                    finish();
                                                }
                                            });
                                            Response_dialog = builder.create();
                                            Response_dialog.show();


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Log.e("Image", "response is not successful : " + response.code());
                                        Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("Image", "response is null");
                                }
                                new File(sessionManagment.getpath().get("path")).delete();
                                progressDialog.dismiss();

                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("Image", anError.getErrorDetail());
                                new File(sessionManagment.getpath().get("path")).delete();
                                progressDialog.dismiss();

                            }
                        });
            }
        }
    }
}
