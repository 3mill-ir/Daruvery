package com.startup.hezare.startup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.startup.hezare.startup.UtilClasses.BottomNavigationViewHelper;
import com.startup.hezare.startup.UtilClasses.CustomTypefaceSpan;
import com.startup.hezare.startup.UtilClasses.DividerItemDecoration;
import com.startup.hezare.startup.UtilClasses.Utils;
import com.startup.hezare.startup.adapters.CustomListViewAdapter;
import com.startup.hezare.startup.adapters.MyRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements AsyncResponse {

    private static long back_pressed = 0L;
    MyRecyclerAdapter myRecyclerAdapter;
    SendPostRequest sendPostRequest;
    SessionManagment sessionManagment;
    private ListView listView;
    private CustomListViewAdapter customListViewAdapter;
    private RecyclerView recyclerView;
    private String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        sendPostRequest=new SendPostRequest(getApplicationContext());
        sendPostRequest.delegate=ProfileActivity.this;
        sendPostRequest.execute(Utils.Main_URL + "api/AndroidServices/AndroidListRequests");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManagment.set_splash(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManagment=new SessionManagment(getApplicationContext());
        sendPostRequest=new SendPostRequest(getApplicationContext());
        sendPostRequest.delegate=ProfileActivity.this;
        sendPostRequest.execute(Utils.Main_URL + "api/AndroidServices/AndroidListRequests");
        //listView = (ListView) findViewById(R.id.list);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu m = bottomNavigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //the method we have create in activity
            CustomTypefaceSpan.applyFontToMenuItem(mi,getApplicationContext());
        }
        bottomNavigationView.setSelectedItemId(R.id.tab_list);
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
                    case R.id.tab_about:
                        intent = new Intent(getBaseContext(), More2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });


        //Setting OnItemClickListener if any item is clicked
        //By default ListVew Items are disabled in CustomListViewAdapter isEnabled() Method
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public ArrayList<HashMap<String, String>> Parsing()
    {
        ArrayList<HashMap<String, String>> RequestsList=new ArrayList<>();
        try {
            String MSG=sendPostRequest.getResult();
            //replacing backslash with null and removing first and end quotation marks
            MSG=MSG.replace("\\","");
            MSG=MSG.replaceAll("^\"|\"$", "");
            //Log.i("Recived Message", MSG);
            JSONObject jsonObj = new JSONObject(MSG);

            // Getting JSON Array node
            JSONArray array = jsonObj.getJSONArray("Root");

            // looping through All Requests
            for (int i =array.length()-1 ; i >=0 ; i--) {
                JSONObject c = array.getJSONObject(i);
                String Name = c.getString("Name");
                String Date = c.getString("Date");
                String Cost = c.getString("Cost");
                String State = c.getString("Status");
                String ResponseCode=c.getString("TrackingCode");
                String ImageURL = c.getString("Image");

                // tmp hash map for single Request
                HashMap<String, String> Request = new HashMap<>();

                // adding each child node to HashMap key => value
                Request.put("name", Name);
                Request.put("date", Date);
                Request.put("cost", Cost);
                Request.put("state", State);
                Request.put("imageURL", ImageURL);
                Request.put("responsecode",ResponseCode);

                // adding Request to Request list
                RequestsList.add(Request);

            }
        } catch (final JSONException e) {
            Log.e("JJ", "Json parsing error: " + e.getMessage());
            // runOnUiThread(new Runnable() {
            // @Override
            //  public void run() {
            //Toast.makeText(getApplicationContext(),
            //        "Json parsing error: " + e.getMessage(),
            //      Toast.LENGTH_LONG).show();
        }
        finally {
            return RequestsList;
        }
    }

    @Override
    public void processFinish() {
        Log.i(TAG,String.valueOf(Parsing().size()));
            //customListViewAdapter = new CustomListViewAdapter(ProfileActivity.this, Parsing());
            //listView.setAdapter(customListViewAdapter);
        recyclerView=(RecyclerView)findViewById(R.id.my_recycler);
        myRecyclerAdapter=new MyRecyclerAdapter(getApplicationContext(),Parsing());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myRecyclerAdapter);

    }

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "برای خروج از برنامه دوباره فشار دهید", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}




