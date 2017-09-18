package com.startup.hezare.startup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;

public class PrivacyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        String rules="دارووری با رعایت استانداردهای امنیتی طراحی گردیده و نهایت دقت در افزایش ضریب امنیت در آن به کار گرفته شده است.\n" +
                "ما متعهد به حفظ و نگهداری تمامی اطلاعات شخصی شما از قبیل نام، نام خانوادگی، آدرس، تلفن های تماس و حساب شما هستیم. تمامی این اطلاعات به منظور ارسال سفارش و اطلاع رسانی به شما می باشد. از ایمیل شما به منظور ارسال اخبار و Promotion استفاده می شود. در صورت عدم تمایل به دریافت ایمیل می توانید در ایمیل های ارسالی بر روی \"لغو دریافت ایمیل\" کلیکی کنید.\n";
        TextView privacy_title=(TextView)findViewById(R.id.title_rule);
        TextView privacy_text=(TextView)findViewById(R.id.text_rule);


        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan.ttf");
        Typeface BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");

        privacy_title.setTypeface(BHoma);
        privacy_text.setText(rules);
        privacy_text.setTypeface(BYekan);



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
                        intent=new Intent(getBaseContext(),MapsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_list:
                        intent=new Intent(getBaseContext(),ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.tab_home:
                        intent=new Intent(getBaseContext(),HomeActivity.class);
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
}
