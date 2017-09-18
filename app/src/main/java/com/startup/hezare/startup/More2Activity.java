package com.startup.hezare.startup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.adapters.CustomListViewAdapterAbout;

import java.util.ArrayList;
import java.util.HashMap;

public class More2Activity extends Activity  {
    ArrayList<HashMap<String, String>> ItemsList;
    private static final String TAG = "More Activity";
    SendPostRequest sendPostRequest;
    TextView nav_full_name;
    SessionManagment sessionManagment;
    private int Item_clicked = 0;
    AlertDialog dialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManagment.set_splash(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav_full_name.setText(sessionManagment.getUserDetailsname().get("name") + " " + sessionManagment.getUserDetailsname().get("family"));
    }
    private static long back_pressed = 0L;
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

        setContentView(R.layout.activity_more2);

        sessionManagment=new SessionManagment(getApplicationContext());
        ItemsList = new ArrayList<>();

        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/BYekan.ttf");


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
                    case R.id.tab_share:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
                        intent1.putExtra("sms_body", "می توانید این برنامه را از بازار دریافت کنید \n لینک دریافت برنامه در کافه بازار:");
                        startActivity(intent1);
                        break;
                    case R.id.tab_home:
                        intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });

        //resources for navigation view items
        String nav_names[] = {"پشتیبانی", "تماس با ما", "درباره ما","قوانین و مقررات","حریم خصوصی", "حساب کاربری",
                "ویرایش مشخصات", "ویرایش رمز عبور", "خروج از حساب کاربری"};
        int nav_icons_ResID[] = {0, R.drawable.ic_if_phone_call, R.drawable.ic_if_information,
                R.drawable.ic_security_black_24dp,R.drawable.ic_fingerprint_black_30dp,
                0, R.drawable.ic_if_account_2190989, R.drawable.ic_if_lock_1891014,
                R.drawable.ic_sign_out32};

        for (int i = 0; i < nav_names.length; i++) {
            HashMap<String, String> Item = new HashMap<>();
            Item.put("name", nav_names[i]);
            Item.put("icon", String.valueOf(nav_icons_ResID[i]));
            Item.put("Licon", String.valueOf(R.drawable.ic_chevron_left_black_30dp));
            ItemsList.add(Item);
        }

        ListView More_Items = (ListView) findViewById(R.id.list_more);
        //Set Adapter to ListView
        //Inflate the Header To Top of ListView
        LayoutInflater Inflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) Inflater.inflate(R.layout.nav_header_more, More_Items, false);
        ImageView header_profile = (ImageView) (myHeader.findViewById(R.id.header_profile));
        nav_full_name = (TextView) (myHeader.findViewById(R.id.nav_name));
        nav_full_name.setTypeface(BYekan);
        header_profile.setImageResource(R.drawable.ic_if_profle_header);
        More_Items.addHeaderView(myHeader, null, false);

        CustomListViewAdapterAbout customListViewAdapterAbout = new
                CustomListViewAdapterAbout(More2Activity.this, ItemsList);
        More_Items.setAdapter(customListViewAdapterAbout);



        More_Items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 2:
                        intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0123456789"));
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getBaseContext(), AboutActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 4:
                        intent = new Intent(getBaseContext(), RulesActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 5:
                        intent = new Intent(getBaseContext(), PrivacyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 7:
                        intent = new Intent(getBaseContext(), EditActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 8:
                        intent = new Intent(getBaseContext(), Password_UpdateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case 9:
                        dialog.show();
                        break;
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(More2Activity.this);

        builder.setMessage("آیا می خواهید خارج شوید؟");
        builder.setIcon(R.drawable.ic_sign_out32);
        builder.setTitle("خروج");
        builder.setCancelable(false);

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sessionManagment.logoutUser();
                Intent intent = new Intent(getBaseContext(), Sign_in_Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                Item_clicked=2;


            }
        });
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Item_clicked=0;
                dialog.cancel();
            }
        });
        dialog = builder.create();
    }

}
