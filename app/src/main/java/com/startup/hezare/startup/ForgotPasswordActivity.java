package com.startup.hezare.startup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ForgotPasswordActivity extends Activity implements AsyncResponse {
    SessionManagment sessionManagment;
    SendPostRequest sendPostRequest;
    String phone_number;
    ProgressDialog progressDialog;
    @InjectView(R.id.input_phone_number) EditText input_phone_number;
    @InjectView(R.id.btn_forgot_password) AppCompatButton btn_forgot_password;
    @InjectView(R.id.input_phone_number_layout)
    TextInputLayout phone_number_layout;

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
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);
        init();


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


        sessionManagment=new SessionManagment(getApplicationContext());
        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number = input_phone_number.getText().toString();
                if(sessionManagment.IS_LEGAL1()) {
                    Log.i("Password Changes Time:",String.valueOf(sessionManagment.get_Times1()));
                    if (phone_number.length() < 11) {
                        phone_number_layout.setError("این شماره همراه معتبر نیست");
                    } else {
                        //
                        try {
                            String _phone_number = URLEncoder.encode(phone_number, "UTF-8");
                            btn_forgot_password.setEnabled(false);
                            progressDialog = new ProgressDialog(ForgotPasswordActivity.this, ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setProgressStyle(R.style.AppTheme);
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
                    */
                            sendPostRequest = new SendPostRequest(getApplicationContext());
                            //this to set delegate/listener drug_header to this class
                            sendPostRequest.delegate = ForgotPasswordActivity.this;
                            sendPostRequest.execute("http://delivery.3mill.ir/api/AndroidAccount/ForgottenPassword?Tell=" + _phone_number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "تعداد دفعات فراموشی رمز عبور بیش از حد مجاز است", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private void init() {
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);

        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan.ttf");
        Typeface BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");

        phone_number_layout.setTypeface(BYekan);
        input_phone_number.setTypeface(BYekan);
        btn_forgot_password.setTypeface(BHoma);

    }

    @Override
    public void processFinish() {
        Log.i("MyTAG", "" + sendPostRequest.getResult());
        Log.i("MyTAG", "" + sendPostRequest.getMessage());

        String state = "";
        state += sendPostRequest.getMyStatus();

        if (state.equals("OK")) {
            int i=sessionManagment.get_Times1();
            Log.i("resend", "OK");
            Toast.makeText(getApplicationContext(), "رمز عبور جدید به شماره شما ارسال خواهد شد", Toast.LENGTH_SHORT).show();
            sessionManagment.Increase_Times1(++i);
        } else {
            Log.i("resend", "false");
            Toast.makeText(getApplicationContext(), "ارتباط با سرور قطع می باشد", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
        btn_forgot_password.setEnabled(true);
        finish();
    }
}
