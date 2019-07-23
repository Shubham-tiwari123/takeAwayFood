//done
package com.example.shubham.takeaway.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class ItemsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    String phoneNumber;
    ListView listView;
    Integer[] imgId={R.drawable.chickenrice,R.drawable.paneerroll,R.drawable.phonecall,R.drawable.takeawaylogo};

    static ArrayList<Integer> itemID = new ArrayList<>();
    static ArrayList<String> itemNames = new ArrayList<>();
    static ArrayList<String> itemQnts = new ArrayList<>();
    static ArrayList<String> itemPrices = new ArrayList<>();
    ArrayList<String> userData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawerView);
        NavigationView navigationView = findViewById(R.id.navid);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        int restaurantId = getIntent().getExtras().getInt("IDS");
        phoneNumber= getIntent().getExtras().getString("number");
        String restaurantName = getIntent().getExtras().getString("restaurantName");
        TextView restaurantCap = findViewById(R.id.restaurantCap);
        restaurantCap.setText(restaurantName);

        userData.add(String.valueOf(restaurantId));
        userData.add(phoneNumber);

        DisplayItemList displayItemList = new DisplayItemList();
        displayItemList.execute(String.valueOf(restaurantId));
        itemID.clear();
        itemNames.clear();
        itemPrices.clear();
        itemQnts.clear();
    }

    private class DisplayItemList extends AsyncTask<String, Void, List<String>>{

        List<String> itemNames = new LinkedList<>();
        List<String> itemPrices = new LinkedList<>();
        List<String> itemIngrediants = new LinkedList<>();

        @Override
        protected List<String> doInBackground(String... strings) {

            String POST_URL = "http://192.168.1.198:22849/TakeAway/DisplayRestaurantMenu";
            String restaurantId = strings[0];

            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("restaurantId",restaurantId);
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
                Log.e("codes:", String.valueOf(responseCode));
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);

                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject nameObj,priceObj,ingrediantObj;

                    nameObj = outputObject.getJSONObject("itemName");
                    priceObj = outputObject.getJSONObject("itemPrice");
                    ingrediantObj = outputObject.getJSONObject("itemIngrediants");

                    int length = outputObject.length();

                    for(int i=0;i<4;i++){
                        String itemName = (String) nameObj.get(""+i);
                        String itemIngrediant = (String) ingrediantObj.get(""+i);
                        int itemPrice = (Integer) priceObj.get(""+i);
                        itemNames.add(itemName);
                        itemIngrediants.add(itemIngrediant);
                        itemPrices.add(String.valueOf(itemPrice));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return itemNames;
        }

        @Override
        protected void onPostExecute(List<String> strings) {

            listView = findViewById(R.id.item_names);
            ItemsListCustomView customList = new ItemsListCustomView(getApplicationContext(),itemNames,
                    imgId,itemPrices,itemIngrediants);
            listView.setAdapter(customList);
        }
    }

    public void addItemInList(int id,String itemName,String itemQnt,String itemPrice){
        if(itemID.isEmpty()){
            itemID.add(id);
            itemNames.add(itemName);
            itemPrices.add(itemPrice);
            itemQnts.add(itemQnt);
        }
        else{
            if(itemID.contains(id)){
                int index = itemID.indexOf(id);
                String itemQ = itemQnts.get(index);
                int num1 = Integer.parseInt(itemQ);
                int num2 = Integer.parseInt(itemQnt);
                int finalQnt = num1+num2;
                String saveAmount = Integer.toString(finalQnt);
                itemQnts.set(index,saveAmount);

                if(itemQnt.equals("1")){
                    String productAmount = itemPrices.get(index);
                    num1 = Integer.parseInt(productAmount);
                    int finalAmount = num1+Integer.parseInt(itemPrice);
                    saveAmount = Integer.toString(finalAmount);
                    itemPrices.set(index,saveAmount);
                }
                else{
                    String productAmount = itemPrices.get(index);
                    num1 = Integer.parseInt(productAmount);
                    int finalAmount = abs(num1-Integer.parseInt(itemPrice));
                    if(finalAmount==0){
                        itemPrices.remove(index);
                        itemNames.remove(index);
                        itemQnts.remove(index);
                        itemID.remove(index);
                    }
                    else{
                        saveAmount = Integer.toString(finalAmount);
                        itemPrices.set(index,saveAmount);
                    }
                }
            }
            else{
                itemID.add(id);
                itemNames.add(itemName);
                itemPrices.add(itemPrice);
                itemQnts.add(itemQnt);
            }

        }

    }

    public void deleteItem(ArrayList<String> itemNameList,ArrayList<String> itemPriceList
                        ,ArrayList<String> itemQntList,ArrayList<Integer> itemIDList){
            Log.e("items0:",itemNameList.toString());

            itemNames = itemNameList;
            itemPrices = itemPriceList;
            itemQnts = itemQntList;
            itemID = itemIDList;
            Log.e("ite:-",itemNames.toString());

    }

    public void cartButton(View view){
        Intent intent = new Intent(ItemsList.this, ShoppingCart.class);
        Bundle args = new Bundle();
        args.putSerializable("itemName",itemNames);
        args.putSerializable("itemPrice",itemPrices);
        args.putSerializable("itemQnt",itemQnts);
        args.putSerializable("itemID",itemID);
        args.putSerializable("userData",userData);
        Log.e("userD",userData.toString());
        intent.putExtra("Bundle",args);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        Bundle bundle;
        switch (menuItem.getItemId()){
            case R.id.resturantDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                intent= new Intent(this,RestaurantPage.class);
                bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.transactiontDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                intent= new Intent(this,TransactionHistory.class);
                bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.userDetails:
                menuItem.setCheckable(false);
                menuItem.setChecked(false);
                intent= new Intent(this,StudentAccount.class);
                bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
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
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
