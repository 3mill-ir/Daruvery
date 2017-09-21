package com.startup.hezare.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Sign_in_Activity extends AppCompatActivity implements AsyncResponse{

    private static final String TAG = "Sign in Activity";
    private static final int REQUEST_SIGNUP = 0;
    private static long back_pressed = 0L;
    ProgressDialog progressDialog;
    SessionManagment sessionManagment;
    SendPostRequest sendPostRequest;
    String phone_number;
    String password;

    @InjectView(R.id.input_phone_number) EditText input_phone_number;
    @InjectView(R.id.input_password) EditText input_password;
    @InjectView(R.id.btn_sign_in) AppCompatButton btn_sign_in;
    @InjectView(R.id.link_forgot_password) TextView Password_forgot_link;
    @InjectView(R.id.link_sign_up) TextView Sign_up_link;
    @InjectView(R.id.link_verification)TextView verification_link;
    @InjectView(R.id.phone_number_layout)TextInputLayout phone_number_layout;
    @InjectView(R.id.password_layout)TextInputLayout password_layout;
    private int View_clicked = 0;

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
        setContentView(R.layout.activity_sing_in);
        ButterKnife.inject(this);

        init();
        sessionManagment=new SessionManagment(getApplicationContext());

        Log.i("",""+sessionManagment.getUserDetails().get("phone"));

        Sign_up_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(getBaseContext(),Sign_up_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                phone_number = input_phone_number.getText().toString();
                password = input_password.getText().toString();

                try {
                    String _password = URLEncoder.encode(password, "UTF-8");
                    String _phone_number = URLEncoder.encode(phone_number, "UTF-8");
                    if (input_validation()) {

                        btn_sign_in.setEnabled(false);
                        Password_forgot_link.setEnabled(false);
                        progressDialog = new ProgressDialog(Sign_in_Activity.this, ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgressStyle(R.style.AppTheme);
                        progressDialog.setCancelable(false);
                        //progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("در حال بررسی...");
                        progressDialog.show();
                       /* new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        //onLoginSuccess();
                                        // onLoginFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 10000);
*/
                        sendPostRequest = new SendPostRequest(getApplicationContext());
                        //this to set delegate/listener drug_header to this class
                        sendPostRequest.delegate = Sign_in_Activity.this;
                        sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/Login?Tell=" + _phone_number + "&Password=" + _password + "&AndroidId=" + App.getAndroidId());
                        Log.i("Android ID:" ,"&AndroidId="+App.getAndroidId());
                        View_clicked = 1;
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Password_forgot_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Signed UP?",String.valueOf(sessionManagment.isSigedUp()));
                Log.e("Sign Up info","phone:"+sessionManagment.getSignedUpSession().get("phone_sign_up")+" Password="+sessionManagment.getSignedUpSession().get("password_sign_up"));


                        Intent intent;
                        intent = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                        startActivity(intent);
                    /*else {
                        try {
                            btn_sign_in.setEnabled(false);
                            Password_forgot_link.setEnabled(false);
                            progressDialog = new ProgressDialog(Sign_in_Activity.this, ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setProgressStyle(R.style.AppTheme);
                            progressDialog.setCancelable(false);
                            //progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("در حال بررسی...");
                            progressDialog.show();
                    *//*new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 10000);
                    *//*
                            sendPostRequest = new SendPostRequest(getApplicationContext());
                            //this to set delegate/listener drug_header to this class
                            sendPostRequest.delegate = Sign_in_Activity.this;
                            sendPostRequest.execute("http://delivery.3mill.ir/api/AndroidAccount/ForgottenPassword?Tell=" + sessionManagment.getSignedUpSession().get("phone_sign_up"));
                            View_clicked = 2;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/

            }
        });
        verification_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(getBaseContext(),VerificationActivity.class);
                startActivity(intent);
                finish();
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

    public boolean input_validation() {
        boolean valid = true;
        String phone_number = input_phone_number.getText().toString();
        String password = input_password.getText().toString();

        password_layout.setError(null);
        phone_number_layout.setError(null);
        if (phone_number.length() < 11) {
            phone_number_layout.setError("این شماره همراه معتبر نیست");
            valid = false;
        } else {
            //
        }
        if (password.length() < 8 || password.length() > 12) {
            password_layout.setError("رمز عبور باید حداقل 8 کاراکتر داشته باشد");
            valid = false;
        } else {
            //
        }
        return valid;
    }


    private void init() {

        progressDialog = new ProgressDialog(Sign_in_Activity.this);

        phone_number_layout.setTypeface(App.BYekan);
        password_layout.setTypeface(App.BYekan);

        input_phone_number.setTypeface(App.BYekan);
        input_password.setTypeface(App.BYekan);

        btn_sign_in.setTypeface(App.BYekan);
        Password_forgot_link.setTypeface(App.BYekan);
        Sign_up_link.setTypeface(App.BYekan);
        verification_link.setTypeface(App.BYekan);
    }



    //this override the implemented method from asyncTask
    @Override
    public void processFinish() {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        if (View_clicked == 1) {
                Log.i("MyTAG", ""+ sendPostRequest.getResult());
                Log.i("MyTAG", ""+ sendPostRequest.getMessage());
                //Log.i("MyTAG",sendPostRequest.getMessage() + " "+ sendPostRequest.getMyStatus());
                String state = "";
                state += sendPostRequest.getMyStatus();
                Log.i("View", "" + View_clicked);

                if (state.equals("OK")) {
                    Log.i("sign in", "OK");
                    sessionManagment.createLoginSession(phone_number,password);
                    Intent intent;
                    intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (state.equals("WrongUserPass")) {
                    Log.i("sign in", "false");
                    Toast.makeText(getApplicationContext(), "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_LONG).show();
                    //return false;
                }
                else if (state.equals("NOK")) {
                    Log.i("sign in", "false");
                    Toast.makeText(getApplicationContext(), "کد تایید هویت تاکنون ثبت نگردیده", Toast.LENGTH_LONG).show();
                    //return false;
                }else {
                    Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_LONG).show();
                }

            progressDialog.dismiss();
            Password_forgot_link.setEnabled(true);
            btn_sign_in.setEnabled(true);
        }
        /*if (View_clicked == 2) {

                Log.i("MyTAG", "" + sendPostRequest.getResult());
                Log.i("MyTAG", "" + sendPostRequest.getMessage());

                String state = "";
                state += sendPostRequest.getMyStatus();
                Log.i("View", "" + View_clicked);

                if (state.equals("OK")) {
                    int i=sessionManagment.get_Times1();
                    Log.i("resend", "OK");
                    Toast.makeText(getApplicationContext(), "رمز عبور جدید به شماره شما ارسال خواهد شد", Toast.LENGTH_SHORT).show();
                    sessionManagment.Increase_Times1(++i);
                } else {
                    Log.i("sign in", "false");
                    Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد ", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                Password_forgot_link.setEnabled(true);
                btn_sign_in.setEnabled(true);

        }*/

    }




}
