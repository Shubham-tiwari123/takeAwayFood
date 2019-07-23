//done
package com.example.shubham.takeaway.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shubham.takeaway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class RestaurantPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;
    String userPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_resturant_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawerView);
        NavigationView navigationView = findViewById(R.id.navid);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Getting the bundl data i.e phoneNumber

        Bundle bundle = getIntent().getExtras();
        userPhoneNumber = bundle.getString("phoneNumber");

        //navigationView.getMenu().getItem(0).setChecked(false);
        DisplayRestaurants displayRestaurants = new DisplayRestaurants();
        displayRestaurants.execute();

    }

    private class DisplayRestaurants extends AsyncTask<String, Void, List<String>>{
        List<String> restaurantName = new LinkedList<>();
        List<String> openTime = new LinkedList<>();
        List<String> closeTime = new LinkedList<>();
        List<Integer> restaurantID = new LinkedList<>();

        @Override
        protected List<String> doInBackground(String... strings) {
            String POST_URL = "http://192.168.1.198:22849/TakeAway/DisplayRestaurants";

            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();

            try {
                URL url = new URL(POST_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();
                Log.e("codess", String.valueOf(responseCode));
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);

                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject nameObj,openTimeObj,closeTimeObj,restaurantIdObj;

                    nameObj = outputObject.getJSONObject("name");
                    openTimeObj = outputObject.getJSONObject("openTime");
                    closeTimeObj = outputObject.getJSONObject("closeTime");
                    restaurantIdObj = outputObject.getJSONObject("restaurantId");

                    for(int i=0;i<3;i++){
                        String restaurantNames = (String) nameObj.get(""+i);
                        String openTimes = (String) openTimeObj.get(""+i);
                        String closeTimes = (String) closeTimeObj.get(""+i);
                        Integer id =(Integer)restaurantIdObj.get(""+i);

                        restaurantName.add(restaurantNames);
                        openTime.add(openTimes);
                        closeTime.add(closeTimes);
                        restaurantID.add(id);
                    }
                    Log.e("tags::",restaurantName.toString());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return restaurantName;
        }

        protected void onPostExecute(List<String> strings) {

            ListView listView = findViewById(R.id.restaurantName);
            RestaurantPageCustomView customList = new RestaurantPageCustomView(getApplicationContext(),restaurantName,
                    openTime,closeTime,restaurantID);
            listView.setAdapter(customList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int restaurantIDs = restaurantID.get(position);
                    String restaurantNames=restaurantName.get(position);
                    String time = closeTime.get(position);
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String time2 = dateFormat.format(Calendar.getInstance().getTime());
                    Log.e("time2",time2);
                    //if(time.compareTo(time2)>0){
                        Intent intent = new Intent(getApplicationContext(),ItemsList.class);
                        intent.putExtra("IDS",restaurantIDs);
                        intent.putExtra("number",userPhoneNumber);
                        intent.putExtra("restaurantName",restaurantNames);
                        startActivity(intent);
                    /*}
                    else*/
                        //Toast.makeText(getApplicationContext(),"Sorry Shop is closed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        Bundle bundle;
        switch (menuItem.getItemId()){
            case R.id.resturantDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                break;
            case R.id.transactiontDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                intent= new Intent(this,TransactionHistory.class);
                bundle = new Bundle();
                bundle.putString("phoneNumber",userPhoneNumber);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.userDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                intent= new Intent(this,StudentAccount.class);
                bundle = new Bundle();
                bundle.putString("phoneNumber",userPhoneNumber);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.aboutTeam:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                break;
            case R.id.trackYourOrder:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                startActivity(new Intent(this,TrackOrder.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
