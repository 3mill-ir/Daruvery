package com.startup.hezare.startup;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;


public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);


        String rules="قبل از انجام هرگونه سفارشی باید ثبت نام انجام گیرد و اطلاعات لازم را وارد شود.\n" +
                "امکان سفارش اقلام دارویی بدون نسخه پزشک از طریق دارووری وجود ندارد. برای سفارش دارو باید تصویری واضح و کامل از نسخه ی تجویزی پزشک گرفته شود و از طریق اپلیکیشن دارووری ارسال گردد. اگر نسخه ناخوانا باشد توسط داروخانه ها برگشت داده می شود و لازم است تصویری جدید و وشفاف از نسخه گرفه شود در غیر اینصورت سفارش حذف می گردد.\n" +
                "اگر نسخه خوانده شود و دارو موجود نباشد درخواست به داروخانه های دیگر فرستاده می شود تا در نهایت داروخانه ای که داروی موجود در نسخه را دارد سفارش را پاسخ دهد.\n" +
                "اگر نسخه خوانده شود و دارو موجود باشد قیمت کل داروهای موجود در نسخه از طرف داروخانه اعلام میشود و کابر موظف به رد و یا پذیرش آن طی مدت 10 دقیقه هست، در غیر انصورت درخواست حذف خواهد شد. اگر قیمت مورد تایید کاربر بود با زدن دکمه تایید آن را به اطلاع داروخانه می رساند تا داروخانه داروهای وی را آماده کرده و از طریق پیک ارسال نماید.\n" +
                "در هنگام مراجعه پیک به کاربر ابتدا نسخه ای که تصویر آن را در هنگام سفارش فرستاده بود را به پیک تحویل می دهد و سپس در صورت تایید تطابق تصویر و اصل نسخه، پیک داروهای وی را تحویل می دهد و با توجه به فاکتور ارسالی دارای مهر داروخانه مربوطه، تسویه حساب انجام می گیرد و پیک اصل نسخه تجویز شده توسط پزشک را تحویل داروخانه خواهد داد. در صورت عدم مطابقت تصویر فرستاده شده در هنگام سفارش با اصل نسخه تجویز شده توسط پزشک، سفارش تحویل داده نمی شود و حساب کابری سفارش دهنده از دارووری مسدود می گردد.\n" +
                "دارووری نماینده\u200Cای برای برقراری ارتباط بین مشتریان آنلاین و داروخانه های طرف قرارداد با دارووری است و لذا مسئولیتی در قبال کیفیت دارو و سایر اقلام خریداری شده عهده دار نیست. کیفیت و هر آنچه به محصول خریداری شده مربوط است بر عهده داروخانه مورد نظر می\u200Cباشد و هرگونه شکایت، درخواست و اظهار نظر در این موارد به مدیر داروخانه مربوطه منتقل خواهد شد.\n" +
                "هزیه تحویل سفارشات در بازه های مورد نظر دارووری (بازه حداکثر 3 ساعته) رایگان بوده و در صورتی که بخواهید سریعتر سفارشتان را دریافت کنید هزینه پیک دریافت می شود.\n" +
                "از ساعات 11 شب الی 6 صبح علاوه بر اقلام غیر دارویی ، آندسته از داروهایی که نیاز به تجویز پزشک ندارند نیز قابل سفارش از طریق دارووری هستند.\n" +
                "برای سفارشات شبانه (از ساعت 11 شب الی 6 صبح) هزینه پیک دریافت می شود.\n" +
                "اگر کاربر در حین سفارش در آدرسی که مشخص کرده است نباشد سفارش درصورتی به جای دیگری که حضور دارد تحویل داده می شود که کاربر هزینه پیک را  تقبل کند.\n" +
                "در صورت مشاهده هر گونه ارسال توهین آمیز، تمسخر آمیز و یا بی احترامی  به کاربر خاطی اخطار داده می شود و در صورت لزوم کاربری وی مسدود خواهد شد.\n";
        TextView rules_title=(TextView)findViewById(R.id.title_rule);
        TextView rules_text=(TextView)findViewById(R.id.text_rule);


        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BYekan.ttf");
        Typeface BHoma = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BHoma.ttf");

        rules_title.setTypeface(BHoma);
        rules_text.setText(rules);
        rules_text.setTypeface(BYekan);

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
