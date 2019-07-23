//done
package com.example.shubham.takeaway.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
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

public class BillingPage extends AppCompatActivity {
    ProgressDialog progress;
    boolean doubleBackToExitPressedOnce = false;
    CardView amountDisplay;
    static String orderID;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_page);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);

        amountDisplay = findViewById(R.id.cardView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String phoneNumber = bundle.getString("phoneNumber");
        String restaurantId = bundle.getString("restaurantId");
        orderID = bundle.getString("orderId");
        TextView orderIdText = findViewById(R.id.orderIDValue);
        orderIdText.setText(orderID);

        DisplayOrderDetails displayOrderDetails = new DisplayOrderDetails();
        displayOrderDetails.execute(orderID);

        GetStatus getStatus = new GetStatus();
        getStatus.execute(orderID);
    }

    public void checkStatus(String status){
        if(status.equals("rejected")){
            TextView orderStatusCap = findViewById(R.id.orderStatus);
            ImageView tick = findViewById(R.id.tickSign);
            tick.setImageResource(R.drawable.tick_logo);
            orderStatusCap.setText("Sorry Order Rejected");
        }
        else if(status.equals("accepted")){
            TextView orderStatusCap = findViewById(R.id.orderStatus);
            ImageView tick = findViewById(R.id.tickSign);
            tick.setImageResource(R.drawable.tick_logo);
            orderStatusCap.setText("Order Confirmed");
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetStatus getStatus = new GetStatus();
                    getStatus.execute(orderID);
                }
            },8000);

        }
    }

    public class GetStatus extends AsyncTask<String,Void,String>{
        String msg;
        @Override
        protected String doInBackground(String... strings) {
            //http://192.168.1.198:22849/TakeAway/GetPlacedOrderStatus"
            String POST_URL = "http://192.168.1.198:22849/TakeAway/GetOrderStatus";
            //String studentPhone = strings[0];
            String orderId = strings[0];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", orderId);
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

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);
                    Log.e("output:",output.toString());
                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject msgObject;
                    msgObject = outputObject.getJSONObject("msgObj");
                    Log.e("msgss",msgObject.toString());
                    msg = msgObject.getString("msgs");

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            checkStatus(msg);
        }
    }

    private class DisplayOrderDetails extends AsyncTask<String, Void, List<String>> {
        List<String> itemNames = new LinkedList<>();
        List<String> itemQnts = new LinkedList<>();
        List<String> itemPrices = new LinkedList<>();
        String orderTime;
        String totalAmount;
        int status;

        //ProgressDialog progress;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BillingPage.this, "ProgressDialog", "Wait for seconds");
            progress.show();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            String POST_URL = "http://192.168.1.198:22849/TakeAway/FinalBillSummery";
            //String studentPhone = strings[0];
            String orderId = strings[0];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId", orderId);
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

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);

                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject nameObj, priceObj, qntObj, orderTimeObj, totalAmountObj;
                    nameObj = outputObject.getJSONObject("itemName");
                    priceObj = outputObject.getJSONObject("itemPrice");
                    qntObj = outputObject.getJSONObject("itemQnt");
                    orderTimeObj = outputObject.getJSONObject("orderTime");
                    totalAmountObj = outputObject.getJSONObject("totalAmount");
                    int length = nameObj.length();

                    for (int i = 0; i < length; i++) {
                        String itemName = (String) nameObj.get("" + i);
                        String itemQnt = (String) qntObj.get("" + i);
                        String itemPrice = (String) priceObj.get("" + i);
                        itemNames.add(itemName);
                        itemQnts.add(itemQnt);
                        itemPrices.add(itemPrice);
                    }
                    orderTime = (String) orderTimeObj.get("orderTime");
                    totalAmount = (String) totalAmountObj.get("orderAmount");
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
            progress.dismiss();
            TextView orderTimeView = findViewById(R.id.orderTime);
            TextView billAmount = findViewById(R.id.billAmount);
            TextView orderStatusCap = findViewById(R.id.orderStatus);
            ImageView tick = findViewById(R.id.tickSign);
            //tick.setImageResource(R.drawable.waitinglogo);
            orderStatusCap.setText("Waiting.....");
            billAmount.setText(totalAmount);
            orderTimeView.setText(orderTime);
            ListView listView = findViewById(R.id.listviews);
            BillingPageCustomView customView = new BillingPageCustomView(getApplicationContext(),
                    itemNames, itemQnts, itemPrices);
            listView.setAdapter(customView);
            //progress.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
