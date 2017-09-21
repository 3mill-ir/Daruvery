package com.startup.hezare.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.UtilClasses.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Password_UpdateActivity extends Activity implements AsyncResponse{

    ProgressDialog progressDialog;
    SessionManagment sessionManagment;
    SendPostRequest sendPostRequest;
    String current_password;
    String new_password;

    @InjectView(R.id.input_current_password) EditText input_current_password;
    @InjectView(R.id.input_new_password) EditText input_new_password;
    @InjectView(R.id.input_new_password_confirm) EditText input_new_password_confirm;
    @InjectView(R.id.btn_password_update) AppCompatButton btn_password_update;

    @InjectView(R.id.current_password_layout) TextInputLayout current_password_layout;
    @InjectView(R.id.new_password_layout) TextInputLayout new_password_layout;
    @InjectView(R.id.new_confirm_password_layout) TextInputLayout new_confirm_password_layout;

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
        setContentView(R.layout.activity_password_update);
        ButterKnife.inject(this);
        init();
        sendPostRequest=new SendPostRequest(getApplicationContext());
        sessionManagment=new SessionManagment(getApplicationContext());

        btn_password_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManagment.IS_LEGAL()) {
                    if (password_validation()) {
                        Log.i("Password Changes Time:",String.valueOf(sessionManagment.get_Times()));

                        try {
                            String _password = URLEncoder.encode(current_password, "UTF-8");
                            String _new_password = URLEncoder.encode(new_password, "UTF-8");

                            btn_password_update.setEnabled(false);
                            progressDialog = new ProgressDialog(Password_UpdateActivity.this, ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setProgressStyle(R.style.AppTheme);
                            progressDialog.setCancelable(false);
                            //progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("در حال بررسی...");
                            progressDialog.show();
                            //this to set delegate/listener drug_header to this class
                            sendPostRequest.delegate = Password_UpdateActivity.this;

                            sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/AndroidChangePassword?OldPassword=" + _password + "&NewPassword=" + _new_password);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(Password_UpdateActivity.this, "تعداد دفعات ویرایش رمز عبور بیش از حد مجاز است", Toast.LENGTH_SHORT).show();
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



    public boolean password_validation() {
        boolean valid = true;

        current_password = input_current_password.getText().toString();
        new_password = input_new_password.getText().toString();
        String new_password_confirm=input_new_password_confirm.getText().toString();

        current_password_layout.setError(null);
        new_password_layout.setError(null);
        new_confirm_password_layout.setError(null);

        if (current_password.length() < 8 ) {
            current_password_layout.setError("رمز عبور باید حداقل 8 کاراکتر داشته باشد");
            valid = false;
        }
        if (new_password.length()<8) {
            new_password_layout.setError("رمز عبور باید حداقل 8 کاراکتر داشته باشد");;
            valid = false;
        }

        if(current_password.equals(sessionManagment.getUserDetails().get("password")))
        {}
        else
        {
            current_password_layout.setError("رمز عبور فعلی اشتباه است");
            valid =false;
        }
        if(new_password.equals(new_password_confirm))
        {

        }
        else
        {
            new_confirm_password_layout.setError("رمز عبور جدید با تکرار آن یکسان نیست");
            valid=false;
        }
        return valid;
    }

    private void init() {

        progressDialog = new ProgressDialog(Password_UpdateActivity.this);

        current_password_layout.setTypeface(App.BYekan);
        new_password_layout.setTypeface(App.BYekan);
        new_confirm_password_layout.setTypeface(App.BYekan);

        input_current_password.setTypeface(App.BYekan);
        input_new_password.setTypeface(App.BYekan);
        input_new_password_confirm.setTypeface(App.BYekan);

        btn_password_update.setTypeface(App.BHoma);
    }

    @Override
    public void processFinish() {
        String state = "";
        state += sendPostRequest.getMyStatus();

        if (state.equals("Success")) {
            int i=sessionManagment.get_Times();
            Log.i("password_change", "OK");
            Log.i("","Sessions:" +sessionManagment.getUserDetails().get("phone") + new_password);
            sessionManagment.createLoginSession(sessionManagment.getUserDetails().get("phone"),new_password);
            Toast.makeText(getApplicationContext(), "رمز عبور با موفقیت ویرایش شد!", Toast.LENGTH_SHORT).show();
            sessionManagment.Increase_Times(++i);
        }
        else if(state.equals("ChangePasswordError")) {
            Log.i("password_change", "failed");
            Toast.makeText(getApplicationContext(), "خطا در ویرایش رمز عبور", Toast.LENGTH_SHORT).show();

        }
        else if(state.equals("NoSuchUser")){
            Log.i("password_change", "failed");
            Toast.makeText(getApplicationContext(), "حساب کاربری با این مشخصات یافت نشد", Toast.LENGTH_SHORT).show();

        }else {
            Log.i("password_change", "failed");
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();

        }
        finish();
        progressDialog.dismiss();
        btn_password_update.setEnabled(true);
    }
}
