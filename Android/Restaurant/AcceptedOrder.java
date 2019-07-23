package com.example.shubham.takeaway.Restaurant;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import java.util.LinkedList;
import java.util.List;

public class AcceptedOrder extends AppCompatActivity {

    private DrawerLayout drawer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_order);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Button backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
            }
        });

        DisplayAcceptedOrders displayAcceptedOrders = new DisplayAcceptedOrders();
        displayAcceptedOrders.execute("3456");
    }
    public void refreshList(){
        Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();
        DisplayAcceptedOrders displayAcceptedOrders = new DisplayAcceptedOrders();
        displayAcceptedOrders.execute("3456");
    }

    private class DisplayAcceptedOrders extends AsyncTask<String, Void, List<String>> {
        List<String> itemNames = new LinkedList<>();
        List<String> orderIds = new LinkedList<>();
        List<String> orderAmount = new LinkedList<>();
        List<String> itemQnts = new LinkedList<>();
        List<String> time = new LinkedList<>();
        List<String> cookingOrder = new LinkedList<>();
        int flag=0;
        String restaurantID;
        @Override
        protected List<String> doInBackground(String... strings) {

            String POST_URL = "http://192.168.1.198:22849/TakeAway/DisplayRestaurantAcceptedOrder";
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
                    JSONObject nameObj,cookingStatusObj,qntObj,itemIDObj,billAmountObj,orderTimeObj;

                    nameObj = outputObject.getJSONObject("itemName");
                    qntObj = outputObject.getJSONObject("itemQnt");
                    itemIDObj = outputObject.getJSONObject("orderId");
                    billAmountObj = outputObject.getJSONObject("totalBill");
                    orderTimeObj = outputObject.getJSONObject("orderTime");
                    cookingStatusObj = outputObject.getJSONObject("cookingStatus");
                    Log.e("itemNames:-",nameObj.toString());

                    int length = itemIDObj.length();

                    for(int i=0;i<length;i++){
                        int no = (int) itemIDObj.get(""+i);
                        String itemName = (String)nameObj.get(""+no);
                        String itemQnt = (String) qntObj.get(""+no);
                        int itemFinalBill = (int)billAmountObj.get(""+no);
                        String orderDate = (String) orderTimeObj.get(""+no);
                        int cooking = (int) cookingStatusObj.get(""+no);

                        itemNames.add(itemName);
                        itemQnts.add(itemQnt);
                        orderAmount.add(String.valueOf(itemFinalBill));
                        orderIds.add(String.valueOf(no));
                        time.add(String.valueOf(orderDate));
                        cookingOrder.add(String.valueOf(cooking));
                    }
                    if(itemNames.isEmpty())
                        flag=1;
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
                ListView listView = findViewById(R.id.order_list);
                AcceptedOrderCustomView customView = new AcceptedOrderCustomView(getApplicationContext(),
                        AcceptedOrder.this, itemNames, orderIds, orderAmount, itemQnts, time, restaurantID,
                        cookingOrder);
                listView.setAdapter(customView);
                TextView displayMsg = findViewById(R.id.textMsg);
                displayMsg.setVisibility(View.VISIBLE);
            }
            else {
                ListView listView = findViewById(R.id.order_list);
                AcceptedOrderCustomView customView = new AcceptedOrderCustomView(getApplicationContext(),
                        AcceptedOrder.this, itemNames, orderIds, orderAmount, itemQnts, time, restaurantID,
                        cookingOrder);
                listView.setAdapter(customView);
            }
        }
    }
}
