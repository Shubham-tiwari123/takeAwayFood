//done
package com.example.shubham.takeaway.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class StudentAccount extends AppCompatActivity {

    Button editName;
    Button editEmail;
    EditText emailEdit;
    EditText nameEdit;
    String name,emailId;
    String userPhoneNumber;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);
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


        Bundle bundle = getIntent().getExtras();
        userPhoneNumber = bundle.getString("phoneNumber");

        GetUserDetails getUserDetails = new GetUserDetails();
        getUserDetails.execute(userPhoneNumber);

        emailEdit = findViewById(R.id.emailId);
        emailEdit.setEnabled(false);
        nameEdit = findViewById(R.id.nameView);
        nameEdit.setEnabled(false);
        TextView phoneNo = findViewById(R.id.phoneNumber);
        phoneNo.setText(userPhoneNumber);

        editName= findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        final Button saveDetails = findViewById(R.id.saveDetails);
        Button orderBtn = findViewById(R.id.orderBtn);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentAccount.this, TransactionHistory.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("phoneNumber",userPhoneNumber);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdit.setEnabled(true);
                emailEdit.setEnabled(false);
                saveDetails.setVisibility(View.VISIBLE);
                nameEdit.requestFocus();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEdit.setEnabled(true);
                nameEdit.setEnabled(false);
                saveDetails.setVisibility(View.VISIBLE);
                emailEdit.requestFocus();
            }
        });

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentAccount.this,"Details Saved",Toast.LENGTH_SHORT)
                        .show();
                name = nameEdit.getText().toString();
                emailId = emailEdit.getText().toString();
                saveDetails.setVisibility(View.INVISIBLE);
                emailEdit.setEnabled(false);
                nameEdit.setEnabled(false);
                SaveUserDetails saveUserDetails = new SaveUserDetails();
                saveUserDetails.execute(name,userPhoneNumber,emailId);
            }
        });

    }

    private class GetUserDetails extends AsyncTask<String,Void,Void>{
        String name;
        String email;
        String userNo;
        @Override
        protected Void doInBackground(String... strings) {
            userNo = strings[0];

            String POST_URL = "http://192.168.1.198:22849/TakeAway/GetUserDetails";
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userPhoneNo",userNo);
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
            if(!name.equals("not") && !email.equals("not")){
                nameEdit.setText(name);
                emailEdit.setText(email);
            }
            else if(!name.equals("not") && email.equals("not")){
                nameEdit.setText(name);
                emailEdit.setHint("Email");
            }
            else if(name.equals("not") && !email.equals("not")){
                nameEdit.setHint("Name");
                emailEdit.setText(email);
            }
            else if(name.equals("not") && email.equals("not")){
                nameEdit.setHint("Name");
                emailEdit.setHint("Email");
            }
        }
    }

    private class SaveUserDetails extends AsyncTask<String,Void,Void>{
        int status;
        @Override
        protected Void doInBackground(String... strings) {
            String userName = strings[0];
            String userPhoneNo = strings[1];
            String userEmail = strings[2];

            String POST_URL = "http://192.168.1.198:22849/TakeAway/SaveUserDetails";
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userName",userName);
                jsonObject.put("userPhoneNo",userPhoneNo);
                jsonObject.put("userEmail",userEmail);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(status==1)
                Toast.makeText(getApplicationContext(),"Details Saved...",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Error occurred try again...",Toast.LENGTH_LONG).show();

        }
    }

}
