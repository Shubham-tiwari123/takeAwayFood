package com.example.shubham.takeaway.Restaurant;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantAccounts extends AppCompatActivity {

    EditText nameEdit;
    EditText emailEdit;
    EditText openTime;
    EditText closeTime;
    Button editEmail;
    Button editOpenTime;
    Button editCloseTime;
    Button saveDetails;
    private Pattern pattern;
    private Pattern pattern1;
    private Matcher matcher;
    private Matcher matcher1;
    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public RestaurantAccounts(){
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        pattern1 = Pattern.compile(TIME24HOURS_PATTERN);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_accounts);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        final TextView restaurantId = findViewById(R.id.restaurantId);
        restaurantId.setText("3456");

        nameEdit = findViewById(R.id.nameView);
        emailEdit = findViewById(R.id.emailId);
        openTime = findViewById(R.id.openTime);
        closeTime = findViewById(R.id.closeTime);
        editEmail = findViewById(R.id.editEmail);
        editOpenTime = findViewById(R.id.editOpenTime);
        editCloseTime = findViewById(R.id.editCloseTime);
        saveDetails = findViewById(R.id.saveDetails);


        emailEdit.setEnabled(false);
        nameEdit.setEnabled(false);
        openTime.setEnabled(false);
        closeTime.setEnabled(false);

        GetUserDetails getUserDetails = new GetUserDetails();
        getUserDetails.execute("3456");

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEdit.setEnabled(true);
                emailEdit.requestFocus();
                saveDetails.setVisibility(View.VISIBLE);
            }
        });

        editOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTime.setEnabled(true);
                openTime.requestFocus();
                saveDetails.setVisibility(View.VISIBLE);
            }
        });

        editCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTime.setEnabled(true);
                closeTime.requestFocus();
                saveDetails.setVisibility(View.VISIBLE);
            }
        });
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdit.setEnabled(false);
                emailEdit.setEnabled(false);
                openTime.setEnabled(false);
                closeTime.setEnabled(false);

                String email = emailEdit.getText().toString();
                String openTiming = openTime.getText().toString();
                String closeTiming = closeTime.getText().toString();
                int time1 = Integer.parseInt(openTiming.substring(0,2));
                int time2 = Integer.parseInt(closeTiming.substring(0,2));
                Log.e("opnT",time1+" "+time2);
                matcher = pattern.matcher(openTiming);
                matcher1 = pattern1.matcher(closeTiming);

                if(openTiming.isEmpty()){
                    openTime.requestFocus();
                    openTime.setError("FIELD CANNOT BE EMPTY");
                }
                else if(closeTiming.isEmpty()){
                    closeTime.requestFocus();
                    closeTime.setError("FIELD CANNOT BE EMPTY");
                }
                else if(!email.isEmpty() && !email.matches(emailPattern)){
                    emailEdit.requestFocus();
                    emailEdit.setError("INVALID EMAIL");
                }
                else if(!matcher.matches()){
                    openTime.requestFocus();
                    openTime.setError("INVALID (HH:MM)");
                }
                else if(!matcher1.matches()){
                    closeTime.requestFocus();
                    closeTime.setError("INVALID (HH:MM)");
                }
                else if(time1>time2||time1==time2){
                    openTime.requestFocus();
                    openTime.setError("INVALID TIME");
                }
                else {
                    SaveUserDetails saveUserDetails = new SaveUserDetails();
                    saveUserDetails.execute(email, openTiming, closeTiming, "3456");
                    saveDetails.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private class GetUserDetails extends AsyncTask<String,Void,Void>{
        String restaurantId;
        String name;
        String email;
        String openTiming;
        String closeTiming;
        @Override
        protected Void doInBackground(String... strings) {
            restaurantId = strings[0];
            String POST_URL = "http://192.168.1.198:22849/TakeAway/GetRestaurantDetails";
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id",restaurantId);
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
                    JSONObject resultObj;

                    resultObj = outputObject.getJSONObject("result");
                    name = resultObj.getString("userName");
                    email = resultObj.getString("userEmail");
                    openTiming = resultObj.getString("openTime");
                    closeTiming = resultObj.getString("closeTime");
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
            if(!email.equals("not")){
                nameEdit.setText(name);
                emailEdit.setText(email);
                openTime.setText(openTiming);
                closeTime.setText(closeTiming);
            }
            else if(email.equals("not")){
                nameEdit.setText(name);
                emailEdit.setHint("Email");
                openTime.setText(openTiming);
                closeTime.setText(closeTiming);
            }
        }
    }

    private class SaveUserDetails extends AsyncTask<String,Void,Void>{

        int status;
        String email;
        String openTime;
        String closeTime;
        String phoneNo;
        @Override
        protected Void doInBackground(String... strings) {
            email = strings[0];
            openTime= strings[1];
            closeTime = strings[2];
            phoneNo = strings[3];
            String POST_URL = "http://192.168.1.198:22849/TakeAway/SaveRestaurantDetails";
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userPhoneNo",phoneNo);
                jsonObject.put("userEmail",email);
                jsonObject.put("openTime",openTime);
                jsonObject.put("closeTime",closeTime);
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
                    status=1;
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
    }
}
