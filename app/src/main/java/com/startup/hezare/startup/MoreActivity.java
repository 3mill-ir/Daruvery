package com.startup.hezare.startup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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

import com.startup.hezare.startup.adapters.CustomListViewAdapterAbout;

import java.util.ArrayList;
import java.util.HashMap;


public class MoreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<HashMap<String, String>> ItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the navigation drawer to open from right
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        ItemsList = new ArrayList<>();

        //Getting Device Display Metrics
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels / 3;
        int width = (displayMetrics.widthPixels *2)/3;
        //Log.i("MY tag", "height:" + height + "width:" + width);

        setContentView(R.layout.activity_more);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Setting the width of ListView to the half of the screen width
        ListView nav_listview = (ListView) findViewById(R.id.left_drawer);
        ViewGroup.LayoutParams params = nav_listview.getLayoutParams();
        params.width = width;
        nav_listview.setLayoutParams(params);


        Typeface BYekan = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/BYekan.ttf");

        //resources for navigation view items
        String nav_names[] = {"پشتیبانی", "تماس با ما", "درباره ما", "حساب کاربری",
                "ویرایش مشخصات", "ویرایش رمز عبور", "خروج از حساب کاربری"};
        int nav_icons_ResID[] = {0, R.drawable.ic_if_phone_call, R.drawable.ic_if_information,
                0, R.drawable.ic_if_account_2190989, R.drawable.ic_if_lock_1891014,
                R.drawable.ic_sign_out32};

        for (int i = 0; i < nav_names.length; i++) {
            HashMap<String, String> Item = new HashMap<>();
            Item.put("name", nav_names[i]);
            Item.put("icon", String.valueOf(nav_icons_ResID[i]));
            ItemsList.add(Item);
        }

        //Set Adapter to ListView
        CustomListViewAdapterAbout customListViewAdapterAbout = new
                CustomListViewAdapterAbout(MoreActivity.this, ItemsList);
        nav_listview.setAdapter(customListViewAdapterAbout);

        //Inflate the Header To Top of ListView
        LayoutInflater Inflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) Inflater.inflate(R.layout.nav_header_more, nav_listview, false);
        ImageView header_profile = (ImageView) (myHeader.findViewById(R.id.header_profile));
        TextView nav_full_name = (TextView) (myHeader.findViewById(R.id.nav_name));
        nav_full_name.setTypeface(BYekan);
        header_profile.setImageResource(R.drawable.ic_if_profle_header);
        nav_listview.addHeaderView(myHeader, null, false);


        nav_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel: +911234567890"));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);

                        //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                        break;

                    case 3:
                        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        //drawer.closeDrawer(GravityCompat.START);
                        //setContentView(R.layout.activity_sign_up);
                        intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                        break;

                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
