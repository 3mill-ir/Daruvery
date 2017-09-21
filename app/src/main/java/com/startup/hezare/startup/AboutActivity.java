package com.startup.hezare.startup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        String about="دارووری اپلیکیشنی برای سفارش آنلاین دارو (با نسخه پزشک) و اقلام غیر دارویی " +
                "از داروخانه های شهر تهران است. دارووری مجموعه ای از داروخانه منتخب تهران را در هر لحظه" +
                " و هر مکان در دردسترس شما قرار می دهد تا به راحتی به اقلام مورد نیازتان دست یابید.";
        String Why="با دارووری در سریع ترین زمان بدون مراجعه به داروخانه ها می توانید دارو و محصولات مورد نظرتان از بهترین داروخانه های تهران تهیه نمایید، سوابق خرید های خود را داشته باشید،" +
                " از تخفیف های مناسبتی استفاده کنید، داروهای کمیاب را پیدا کنید و سفارش دهید، " +
                "از همه مهمتر دارووری به صورت 24 ساعته همراه شما و آماده ارائه خدمات است.";
        TextView about_us_title=(TextView)findViewById(R.id.title_about);
        TextView about_us_text=(TextView)findViewById(R.id.text_about);
        TextView why_us_title=(TextView)findViewById(R.id.title_why);
        TextView why_us_text=(TextView)findViewById(R.id.text_why);

        about_us_title.setTypeface(App.BHoma);
        about_us_text.setText(about);
        about_us_text.setTypeface(App.BYekan);
        why_us_text.setText(Why);

        why_us_title.setTypeface(App.BHoma);
        why_us_text.setTypeface(App.BYekan);


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
