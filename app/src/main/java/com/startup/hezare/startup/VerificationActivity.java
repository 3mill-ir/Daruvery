package com.startup.hezare.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
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

public class VerificationActivity extends Activity implements AsyncResponse {

    private static long back_pressed = 0L;
    SessionManagment sessionManagment;
    SendPostRequest sendPostRequest;
    ProgressDialog progressDialog;
    @InjectView(R.id.input_verification_code) EditText input_verification_code;
    @InjectView(R.id.btn_verification) AppCompatButton btn_verification;
    @InjectView(R.id.link_resend_code) TextView link_resend_code;
    private int View_clicked = 0;

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "برای خروج از برنامه دوباره فشار دهید", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.inject(this);
        init();
        sessionManagment = new SessionManagment(getApplicationContext());


        Log.i("MY:", "" + sessionManagment.getUserDetails().get("phone") + "   "+sessionManagment.getUserDetails().get("password") );

        btn_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Authorize_Code = input_verification_code.getText().toString();

                    try {
                        String _authorize_code = URLEncoder.encode(Authorize_Code, "UTF-8");

                        if (Authorize_Code.isEmpty()) {
                            input_verification_code.setError("کد تائید احراز هویت خود را وارد کنید");
                        } else {

                            btn_verification.setEnabled(false);
                            link_resend_code.setEnabled(false);
                            progressDialog = new ProgressDialog(VerificationActivity.this, ProgressDialog.THEME_HOLO_DARK);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            //progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("در حال بررسی...");
                            progressDialog.show();

                            sendPostRequest = new SendPostRequest(getApplicationContext());
                            //this to set delegate/listener drug_header to this class
                            sendPostRequest.delegate = VerificationActivity.this;
                            sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/AndroidAuthorizeClient?Tell="
                                    + sessionManagment.getSignedUpSession().get("phone_sign_up") + "&Password=" + sessionManagment.getSignedUpSession().get("password_sign_up")
                                    + "&RndValue=" + _authorize_code);
                            View_clicked = 1;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

            }
        });
        link_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagment.IS_LEGAL2()) {
                    Log.i("Password Changes Time:",String.valueOf(sessionManagment.get_Times2()));
                    btn_verification.setEnabled(false);
                    link_resend_code.setEnabled(false);
                    progressDialog = new ProgressDialog(VerificationActivity.this, ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressStyle(R.style.AppTheme);
                    progressDialog.setCancelable(false);
                    //progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("در حال بررسی...");
                    progressDialog.show();

                    Log.e("Sign Up info", "phone:" + sessionManagment.getSignedUpSession().get("phone_sign_up") + " Password=" + sessionManagment.getSignedUpSession().get("password_sign_up"));
                    sendPostRequest = new SendPostRequest(getApplicationContext());
                    //this to set delegate/listener drug_header to this class
                    sendPostRequest.delegate = VerificationActivity.this;
                    sendPostRequest.execute(Utils.Main_URL + "api/AndroidAccount/GetRandomValue?Tell=" + sessionManagment.getSignedUpSession().get("phone_sign_up")
                            + "&Password=" + sessionManagment.getSignedUpSession().get("password_sign_up"));
                    View_clicked = 2;
                }
                else
                {
                    Toast.makeText(VerificationActivity.this, "تعداد دفعات ارسال مجدد بیش از حد مجاز است", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        TextInputLayout Verification_layout = (TextInputLayout) findViewById(R.id.verification_layout);
        Verification_layout.setTypeface(App.BYekan);
        input_verification_code.setTypeface(App.BYekan);
        btn_verification.setTypeface(App.BHoma);
        link_resend_code.setTypeface(App.BHoma);
    }

    @Override
    public void processFinish() {

        if (View_clicked == 1) {
            //Log.i("MyTAG",sendPostRequest.getMessage() + " "+ sendPostRequest.getMyStatus());
            Log.i("MyTAG", ""+ sendPostRequest.getResult());
            Log.i("MyTAG", ""+ sendPostRequest.getMessage());
            String state="";
            state += sendPostRequest.getMyStatus();
            Log.i("View", "" + View_clicked);

            if (state.equals("OK")) {

                Log.i("Verification:", "OK");

                Toast.makeText(getApplicationContext(), "حساب کاربری شما با موفقیت ثبت دائمی گردید\nمی توانید از اینجا وارد حساب کاربری خود گردید", Toast.LENGTH_LONG).show();
                Intent intent;
                intent = new Intent(getBaseContext(), Sign_in_Activity.class);
                startActivity(intent);
                finish();

            } else if(state.equals("Error")){
                Log.i("Verification", "failed");
                Toast.makeText(getApplicationContext(), "کد 6 رقمی وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();

            }else if(state.equals("NOK"))
            {
                Log.i("Verification", "failed user pass");
                //user password wrong
                Toast.makeText(getApplicationContext(), "خطا در تایید هویت", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
            btn_verification.setEnabled(true);
            link_resend_code.setEnabled(true);
        } if (View_clicked == 2) {
            String state = "";
            state += sendPostRequest.getMyStatus();
            Log.i("View", "" + View_clicked);

            if (state.equals("OK")) {
                int i=sessionManagment.get_Times2();
                sessionManagment.Increase_Times2(++i);
                Toast.makeText(getApplicationContext(), "کد احراز هویت با موفقیت ارسال مجدد گردید", Toast.LENGTH_LONG).show();
                Log.i("resend", "OK");
            }
            else if(state.equals("NOK"))
            {
                Toast.makeText(getApplicationContext(), "خطا در دریافت کد 6 رقمی", Toast.LENGTH_LONG).show();
                Log.i("resend", "NOK");
            }
            else if(state.equals("Error"))
            {
                Toast.makeText(getApplicationContext(), "مشخصات وارد شده در سیستم ثبت نگردیده است", Toast.LENGTH_LONG).show();
                Log.i("resend", "Error");
            }
            else {
                Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_LONG).show();
                Log.i("resend", "false");
                //return false;
            }
            progressDialog.dismiss();
            btn_verification.setEnabled(true);
            link_resend_code.setEnabled(true);
        }
    }
}
