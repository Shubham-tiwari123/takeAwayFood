//done
package com.example.shubham.takeaway.Student;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;

public class TrackOrder extends AppCompatActivity {

    static String number;
    static String orderID;
    TimerTask callDisplayOrders;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
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

        Button button = findViewById(R.id.trackOrder);
        ImageView cookingFood = findViewById(R.id.cookingFood);
        ImageView parcelReady = findViewById(R.id.parcelReady);
        final EditText phoneNumber = findViewById(R.id.phoneNo);
        final EditText orderId = findViewById(R.id.orderID);

        cookingFood.setImageResource(R.drawable.cookingfood);
        parcelReady.setImageResource(R.drawable.parcelready);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = phoneNumber.getText().toString();
                phoneNumber.setText("");
                orderID = orderId.getText().toString();
                orderId.setText("");
                GetParcelStatus getParcelStatus = new GetParcelStatus();
                getParcelStatus.execute(orderID);
            }
        });
    }

    void setValue(int code){
        ImageView cookingFood = findViewById(R.id.cookingFood);
        ImageView parcelReady = findViewById(R.id.parcelReady);
        TextView note = findViewById(R.id.note);
        if(code==1){
            cookingFood.setImageResource(R.drawable.cookingfoods);
            note.setVisibility(View.INVISIBLE);
        }
        else if(code==2){
            cookingFood.setImageResource(R.drawable.cookingfoods);
            parcelReady.setImageResource(R.drawable.parcel);
            note.setVisibility(View.INVISIBLE);
        }
        else if(code==3){
            cookingFood.setImageResource(R.drawable.cookingfoods);
            parcelReady.setImageResource(R.drawable.parcel);
            note.setVisibility(View.VISIBLE);
            note.setText("Order delivered");
        }
        else if(code==4){
            note.setVisibility(View.VISIBLE);
            note.setText("Cooking not started yet");
        }
    }

    private class GetParcelStatus extends AsyncTask<String,Void,Void>{
        int statusCode;
        @Override
        protected Void doInBackground(String... strings) {
            //TrackOrderStatusApi
            String POST_URL = "http://192.168.1.198:22849/TakeAway/GetCookingStatus";
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

                Log.e("responses:-",orderId);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);
                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject orderStatusObj;
                    orderStatusObj = outputObject.getJSONObject("status");
                    String orderStatus = orderStatusObj.getString("orderStatus");
                    if(orderStatus.equals("cooking")){
                        statusCode =1;
                    }
                    else if(orderStatus.equals("ready")){
                        statusCode = 2;
                    }
                    else if(orderStatus.equals("delivered")){
                        statusCode =3;
                    }
                    else if(orderStatus.equals("notStarted"))
                        statusCode=4;
                }
                else{
                    Log.e("negative:-","else");
                   // note.setVisibility(View.VISIBLE);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setValue(statusCode);
        }
    }


}
