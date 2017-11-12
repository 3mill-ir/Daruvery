package com.startup.hezare.startup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Sign_up_Activity extends AppCompatActivity implements AsyncResponse{

    private static final String TAG = "Sign up Activity";
    private static final int REQUEST_SIGNUP = 0;
    private static long back_pressed = 0L;
    ProgressDialog progressDialog;
    SendPostRequest sendPostRequest;
    SessionManagment sessionManagment;
    @InjectView(R.id.input_name) EditText input_name;
    @InjectView(R.id.input_family) EditText input_family;
    @InjectView(R.id.input_phone_number) EditText input_phone_number;
    @InjectView(R.id.input_password) EditText input_password;
    @InjectView(R.id.input_address) EditText input_address;
    @InjectView(R.id.btn_sign_up) Button btn_sign_up;
    @InjectView(R.id.link_login) TextView link_login;
    @InjectView(R.id.phone_number_layout)TextInputLayout phone_number_layout;
    @InjectView(R.id.password_layout)TextInputLayout password_layout;
    @InjectView(R.id.name_layout)TextInputLayout name_layout;
    @InjectView(R.id.family_layout)TextInputLayout family_layout;
    @InjectView(R.id.address_layout)TextInputLayout address_layout;
    String Name;
    String Family;
    String Phone_Number;
    String Address;
    String Password;
    AlertDialog dialog;

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
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "برای خروج از برنامه دوباره فشار دهید", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sessionManagment=new SessionManagment(getApplicationContext());
        ButterKnife.inject(this);
        Init();
         link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(getBaseContext(),Sign_in_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Input_Validate()) {
                    try {
                        String _password = URLEncoder.encode(Password, "UTF-8");
                        String _name = URLEncoder.encode(Name, "UTF-8");
                        String _family = URLEncoder.encode(Family, "UTF-8");
                        String _phone_number = URLEncoder.encode(Phone_Number, "UTF-8");
                        String _address = URLEncoder.encode(Address, "UTF-8");

                        progressDialog = new ProgressDialog(Sign_up_Activity.this, ProgressDialog.THEME_HOLO_DARK);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        //progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("در حال بررسی...");
                        progressDialog.show();
                        /*new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        //onLoginSuccess();
                                        // onLoginFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 10000);

               */       sendPostRequest = new SendPostRequest(getApplicationContext());
                        //this to set delegate/listener drug_header to this class
                        sendPostRequest.delegate = Sign_up_Activity.this;

                        sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/AndroidRegisterClient?Tell=" + _phone_number + "&Password=" + _password + "&Address=" + _address + "&FirstName=" + _name + "&LastName=" + _family + "&AndroidId=" + App.getAndroidId());

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
                else
                {}

            }
        });

        input_phone_number.setText("09");
        input_phone_number.setTextDirection(View.TEXT_DIRECTION_LTR);
        Selection.setSelection(input_phone_number.getText(), input_phone_number.getText().length());
        input_phone_number.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                if (!arg0.toString().contains("09")) {
                    input_phone_number.setText("09");
                    input_phone_number.setTextDirection(View.TEXT_DIRECTION_LTR);
                    Selection.setSelection(input_phone_number.getText(), input_phone_number.getText().length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });


    }

    @Override
    public void processFinish() {

        Log.i("MyTAG", ""+ sendPostRequest.getResult());
        Log.i("MyTAG", ""+ sendPostRequest.getMessage());
        Log.i("MyTAG",""+sendPostRequest.getMessage() + " "+ sendPostRequest.getMyStatus());
        String state = "";
        state += sendPostRequest.getMyStatus();

        if (state.equals("OK")) {
            Log.i("sign up", "OK");
            sessionManagment.createSignUpSession(Phone_Number,Password);
            Toast.makeText(getApplicationContext(), "حساب کاربری شما به صورت موقت ثبت شد، یک کد تایید هویت برای شما ارسال خواهد گردید", Toast.LENGTH_LONG).show();
            Intent intent;
            intent = new Intent(getBaseContext(), VerificationActivity.class);
            startActivity(intent);
            finish();

        } else if(state.equals("Iterative")) {
            Log.i("sign up", "false");
            Toast.makeText(getApplicationContext(), "این شماره قبلا در سیستم ثبت شده است!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.i("server down", "false");
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_LONG).show();
            //return false;
        }
        progressDialog.dismiss();
        btn_sign_up.setEnabled(true);
    }

    public boolean Input_Validate() {
        boolean valid = true;

        Name = input_name.getText().toString();
        Family=input_family.getText().toString();
        Phone_Number = input_phone_number.getText().toString();
        Address=input_address.getText().toString();
        Password = input_password.getText().toString();

        name_layout.setError(null);
        family_layout.setError(null);
        phone_number_layout.setError(null);
        password_layout.setError(null);
        address_layout.setError(null);
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
        if (Phone_Number.length() < 11) {
            phone_number_layout.setError("این شماره همراه معتبر نیست");
            valid = false;
        } else {
            //
        }

        if (Password.length() < 8 || Password.length() > 12) {
            password_layout.setError("رمز عبور باید حداقل 8 کاراکتر داشته باشد");
            valid = false;
        } else {
            //
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
        phone_number_layout.setTypeface(App.BYekan);
        password_layout.setTypeface(App.BYekan);
        address_layout.setTypeface(App.BYekan);

        input_name.setTypeface(App.BYekan);
        input_family.setTypeface(App.BYekan);
        input_phone_number.setTypeface(App.BYekan);
        input_password.setTypeface(App.BYekan);
        input_address.setTypeface(App.BYekan);

        btn_sign_up.setTypeface(App.BHoma);
        link_login.setTypeface(App.iransans);
    }
}
