package com.example.shubham.takeaway.Restaurant;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.takeaway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewOrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer;
    TimerTask callDisplayOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawerView);
        NavigationView navigationView = findViewById(R.id.navid);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        callDisplayOrders = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DisplayNewOrders displayNewOrders = new DisplayNewOrders();
                        displayNewOrders.execute("3456");
                    }
                });
            }
        };
        timer.schedule(callDisplayOrders,0,8000);
    }

    private class DisplayNewOrders extends AsyncTask<String, Void, List<String>> {
        List<String> itemNames = new LinkedList<>();
        List<String> orderIds = new LinkedList<>();
        List<String> orderAmount = new LinkedList<>();
        List<String> itemQnts = new LinkedList<>();
        List<String> time = new LinkedList<>();
        int flag=0;
        String restaurantID;
        @Override
        protected List<String> doInBackground(String... strings) {

            String POST_URL = "http://192.168.1.198:22849/TakeAway/DisplayRestaurantOrder";
            restaurantID = strings[0];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("restaurantID",restaurantID);
                URL url = new URL(POST_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                osw.write(jsonObject.toString());
                osw.flush();
                osw.close();
                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);

                    Log.e("itemNAme:-",output.toString());
                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject nameObj,priceObj,qntObj,itemIDObj,billAmountObj,orderTimeObj;

                    nameObj = outputObject.getJSONObject("itemName");
                    qntObj = outputObject.getJSONObject("itemQnt");
                    itemIDObj = outputObject.getJSONObject("orderId");
                    billAmountObj = outputObject.getJSONObject("totalBill");
                    orderTimeObj = outputObject.getJSONObject("orderTime");
                    Log.e("itemNames:-",nameObj.toString());

                    int length = itemIDObj.length();

                    for(int i=0;i<length;i++){
                        int no = (int) itemIDObj.get(""+i);
                        String itemName = (String)nameObj.get(""+no);
                        String itemQnt = (String) qntObj.get(""+no);
                        int itemFinalBill = (int)billAmountObj.get(""+no);
                        String orderDate = (String) orderTimeObj.get(""+no);

                        itemNames.add(itemName);
                        itemQnts.add(itemQnt);
                        orderAmount.add(String.valueOf(itemFinalBill));
                        orderIds.add(String.valueOf(no));
                        time.add(String.valueOf(orderDate));
                    }
                    if(itemNames.isEmpty())
                        flag=1;
                    else{
                        flag=0;
                        Collections.reverse(itemNames);
                        Collections.reverse(itemQnts);
                        Collections.reverse(orderAmount);
                        Collections.reverse(orderIds);
                        Collections.reverse(time);
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return itemNames;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if(flag==1){
                TextView displayMsg = findViewById(R.id.textMsg);
                displayMsg.setVisibility(View.VISIBLE);
            }
            else {
                ListView listView = findViewById(R.id.order_list);
                NewOrderCustomView customView = new NewOrderCustomView(getApplicationContext(),
                        itemNames, orderIds, orderAmount, itemQnts, time, restaurantID);
                listView.setAdapter(customView);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(false);
        switch (menuItem.getItemId()){
            case R.id.userOrders:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                break;
            case R.id.acceptOrders:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                startActivity(new Intent(this,AcceptedOrder.class));
                break;
            case R.id.billHistory:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                startActivity(new Intent(this, RestaurantBills.class));
                break;
            case R.id.restaurantAccount:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                startActivity(new Intent(this,RestaurantAccounts.class));
                break;
            case R.id.aboutTeam:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callDisplayOrders.cancel();
    }
}
