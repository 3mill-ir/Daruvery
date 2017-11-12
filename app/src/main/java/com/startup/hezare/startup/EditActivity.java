package com.startup.hezare.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.UtilClasses.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditActivity extends Activity implements AsyncResponse{

    @InjectView(R.id.input_name) EditText input_name;
    @InjectView(R.id.input_family) EditText input_family;
    @InjectView(R.id.input_address) EditText input_address;
    @InjectView(R.id.btn_update) Button btn_update;
    @InjectView(R.id.name_layout) TextInputLayout name_layout;
    @InjectView(R.id.family_layout) TextInputLayout family_layout;
    @InjectView(R.id.address_layout) TextInputLayout address_layout;


    SendPostRequest sendPostRequest;
    ProgressDialog progressDialog;
    SessionManagment sessionManagment;
    String Name;
    String Family;
    String Address;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);
        Init();
        sessionManagment=new SessionManagment(getApplicationContext());
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Input_Validate()) {
                    try {
                        String _name = URLEncoder.encode(Name, "UTF-8");
                        String _family = URLEncoder.encode(Family, "UTF-8");
                        String _address = URLEncoder.encode(Address, "UTF-8");

                        progressDialog = new ProgressDialog(EditActivity.this, ProgressDialog.THEME_HOLO_DARK);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        //progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("در حال بررسی...");
                        progressDialog.show();

                        sendPostRequest = new SendPostRequest(getApplicationContext());
                        //this to set delegate/listener drug_header to this class
                        sendPostRequest.delegate = EditActivity.this;

                        sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/AndroidEditClient?Address=" + _address + "&FirstName=" + _name + "&LastName=" + _family);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else
                {}
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
        bottomNavigationView.setSelectedItemId(R.id.tab_about);
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
                    case R.id.tab_home:
                        intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_share:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                        intent1.putExtra("sms_body", "می توانید این برنامه را از بازار دریافت کنید \n لینک دریافت برنامه در کافه بازار:");
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

    }



    public boolean Input_Validate() {
        boolean valid = true;

        Name = input_name.getText().toString();
        Family=input_family.getText().toString();
        Address=input_address.getText().toString();

        if (Name.isEmpty()) {
            name_layout.setError("نام خود را وارد کنید");
            valid = false;
        } else {

        }
        if (Family.isEmpty()) {
            family_layout.setError("نام خانوادگی خود را وارد کنید");
            valid = false;
        } else {

        }
        if (Address.isEmpty()) {
            address_layout.setError("اطلاعات آدرس خود را به صورت دقیق وارد کنید");
            valid = false;
        } else {

        }
        return valid;
    }
    //Initiation layout
    private void Init()
    {
        name_layout.setTypeface(App.BYekan);
        family_layout.setTypeface(App.BYekan);
        address_layout.setTypeface(App.BYekan);

        input_name.setTypeface(App.BYekan);
        input_family.setTypeface(App.BYekan);
        input_address.setTypeface(App.BYekan);

        btn_update.setTypeface(App.BHoma);
    }

    @Override
    public void processFinish() {
        Log.i("MyTAG",sendPostRequest.getMessage() + " "+ sendPostRequest.getMyStatus());
        String state = "";
        state += sendPostRequest.getMyStatus();

        if (state.equals("OK")) {
            Log.i("Edit", "OK");
            sessionManagment.createUserDetails(Name,Family,Address);
            Toast.makeText(getApplicationContext(), "مشخصات حساب کاربری با موفقیت ویرایش شد", Toast.LENGTH_SHORT).show();
        }
        else if(state.equals("NOK"))
        {
            Toast.makeText(getApplicationContext(), "خطا در ویرایش حساب کاربری", Toast.LENGTH_SHORT).show();
            Log.i("Edit", "false");
            //return false;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
            Log.i("Edit", "false");
            //return false;
        }
        finish();
        progressDialog.dismiss();
        btn_update.setEnabled(true);
    }
}
