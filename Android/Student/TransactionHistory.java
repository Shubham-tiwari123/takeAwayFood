package com.example.shubham.takeaway.Student;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

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

public class TransactionHistory extends AppCompatActivity {
    private DrawerLayout drawer;
    ListView listView;
    String userPhoneNumber;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_transaction_history);
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

        Bundle bundle = getIntent().getExtras();
        userPhoneNumber = bundle.getString("phoneNumber");

        DisplayHistory displayHistory = new DisplayHistory();
        displayHistory.execute(userPhoneNumber);
    }

    private class DisplayHistory extends AsyncTask<String, Void, List<String>>{
        List<String> itemNames = new LinkedList<>();
        List<String> orderIds = new LinkedList<>();
        List<String> orderAmount = new LinkedList<>();
        List<String> itemQnts = new LinkedList<>();
        List<String> date = new LinkedList<>();
        List<String> itemPrices = new LinkedList<>();
        List<String> orderStatus = new LinkedList<>();

        @Override
        protected List<String> doInBackground(String... strings) {


            String POST_URL = "http://192.168.1.198:22849/TakeAway/DisplayStudentTransaction";
            String studentPhone = strings[0];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("studentPhone",studentPhone);
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

                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject nameObj,priceObj,qntObj,itemIDObj,billAmountObj,orderDateObj,
                            orderStatusObj;

                    nameObj = outputObject.getJSONObject("itemName");
                    priceObj = outputObject.getJSONObject("itemPrice");
                    qntObj = outputObject.getJSONObject("itemQnt");
                    itemIDObj = outputObject.getJSONObject("orderId");
                    billAmountObj = outputObject.getJSONObject("totalBill");
                    orderDateObj = outputObject.getJSONObject("orderDate");
                    orderStatusObj = outputObject.getJSONObject("statusCode");

                    int length = itemIDObj.length();

                    for(int i=0;i<length;i++){
                        int no = (int) itemIDObj.get(""+i);
                        String itemName = (String)nameObj.get(""+no);
                        String itemQnt = (String) qntObj.get(""+no);
                        String itemPrice = (String) priceObj.get(""+no);
                        int itemFinalBill = (int)billAmountObj.get(""+no);
                        String orderDate = (String) orderDateObj.get(""+no);
                        int orderStatusCode = (int)orderStatusObj.get(""+no);
                        if(orderStatusCode==4)
                            orderStatus.add("Accepted");
                        else if(orderStatusCode==2)
                            orderStatus.add("Rejected");
                        itemNames.add(itemName);
                        itemQnts.add(itemQnt);
                        orderAmount.add(String.valueOf(itemFinalBill));
                        orderIds.add(String.valueOf(no));
                        date.add(orderDate);
                        itemPrices.add(itemPrice);
                    }
                }
                Collections.reverse(itemNames);
                Collections.reverse(itemQnts);
                Collections.reverse(orderAmount);
                Collections.reverse(orderIds);
                Collections.reverse(date);
                Collections.reverse(itemPrices);
                Collections.reverse(orderStatus);

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
            listView = findViewById(R.id.listviews);
            TransactionCustomView customView = new TransactionCustomView(getApplicationContext(),
                    itemNames,orderIds,orderAmount,itemQnts,date,itemPrices,orderStatus);
            listView.setAdapter(customView);
        }
    }
}

