package com.example.shubham.takeaway.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shubham.takeaway.R;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

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
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PayUMethod extends AppCompatActivity {

    static String restaurantId;
    static String orderId;
    static String amount;
    static String phoneNumber;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umethod);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        phoneNumber = bundle.getString("phoneNumber");
        restaurantId = bundle.getString("restaurantId");
        orderId = bundle.getString("orderId");
        amount = bundle.getString("amount");

        final EditText firstName = findViewById(R.id.firstName);
        final EditText email = findViewById(R.id.emailId);

        Button continuePayment = findViewById(R.id.continuePayment);
        continuePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = firstName.getText().toString();
                String emailId = email.getText().toString();
                SendData sendData = new SendData();
                sendData.execute(emailId,name,phoneNumber,amount,orderId);
            }
        });
    }

    private class SendData extends AsyncTask<String,Void,Void> {

        int flag=0;
        JSONObject jsonObject = new JSONObject();
        StringBuffer output = new StringBuffer();
        String emailId;
        String name;
        String number;
        String amount;
        String txnid;
        String hash;

        ProgressDialog progress = new ProgressDialog(PayUMethod.this);
        @Override
        protected void onPreExecute() {
            progress.setTitle("Connecting...");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            emailId = strings[0];
            name = strings[1];
            number = strings[2];
            amount = strings[3];
            txnid = strings[4];
            Log.e("trymsg","msg");
            String URL_POST = "http://192.168.1.198:22849/TakeAway/GenerateHash";
            try {
                jsonObject.put("email",emailId);
                jsonObject.put("firstName",name);
                jsonObject.put("amount",amount);
                jsonObject.put("txnid",txnid);
                //jsonObject.put("key","t5BFXZtl");
                //jsonObject.put("salt","X3PUtWzgW3");
                jsonObject.put("productinfo","TakeAway");


                URL url = new URL(URL_POST);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                osw.write(jsonObject.toString());
                osw.flush();
                osw.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    flag=1;
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);
                    Log.e("hash:-", output.toString());
                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject hashObj;

                    hashObj = outputObject.getJSONObject("hashObj");
                    hash = hashObj.getString("hashValue");

                }
                Log.e("response", String.valueOf(responseCode));
            } catch (MalformedURLException e) {
                Log.e("errors",e.toString());
            } catch (IOException e) {
                Log.e("errors",e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(progress.isShowing()){
                        progress.dismiss();
                        //TextView hashValue = (TextView) findViewById(R.id.hashValue);
                        if (flag == 1) {
                            Toast.makeText(getApplicationContext(), "Data Send", Toast.LENGTH_SHORT).show();
                            //hashValue.setText(hash);
                            launchPayUMoneyFlow(amount,name,emailId,txnid,number,"TakeAway",
                                    hash);
                        } else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            handler.postDelayed(runnable,5000);
        }
    }

    public void launchPayUMoneyFlow(String amount,String firstName,String email,String txnId,
                                    String phone, String productName,String hash){

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnId)                                             // Transaction ID
                .setPhone(phone)                                           // User Phone number
                .setProductName(productName)                   // Product Name or description
                .setFirstName(firstName)                              // User First name
                .setEmail(email)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                .setKey("t5BFXZtl")                        // Merchant key
                .setMerchantId("X3PUtWzgW3");

        PayUmoneySdkInitializer.PaymentParam paymentParam = null;
        try {
            paymentParam = builder.build();
            paymentParam.setMerchantHash(hash);
            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
                    this, R.style.AppTheme_default, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode==RESULT_OK & data!=null){
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();
                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    SendResponseData responseData = new SendResponseData();
                    responseData.execute(String.valueOf(requestCode),"1",payuResponse,merchantResponse);
                } else{
                    SendResponseData responseData = new SendResponseData();
                    responseData.execute(String.valueOf(requestCode),"-1",payuResponse,merchantResponse);
                }
            }
        }
    }

    private class SendResponseData extends AsyncTask<String,Void,Void>{

        int flag=0;
        ProgressDialog progress = new ProgressDialog(PayUMethod.this);
        @Override
        protected void onPreExecute() {
            progress.setTitle("");
            progress.setMessage("Please Wait");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... ints) {
            String resultCode = ints[0];
            String responseCodes = ints[1];
            String payuResponse = ints[2];
            String merchantResponse = ints[3];
            StringBuffer output = new StringBuffer();
            String URL_POST = "http://192.168.1.198:22849/TakeAway/PayUResponse";
            URL url = null;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("code",resultCode);
                jsonObject.put("responseD",responseCodes);
                jsonObject.put("merchantResponse",merchantResponse);
                jsonObject.put("payuResponse",payuResponse);
                url = new URL(URL_POST);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                osw.write(jsonObject.toString());
                osw.flush();
                osw.close();
                int responseCode = conn.getResponseCode();

                if(responseCode==HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = bufferedReader.readLine()) != null)
                        output.append(s);
                    Log.e("output:",output.toString());
                    JSONObject outputObject = new JSONObject(output.toString());
                    JSONObject respoObject;
                    respoObject = outputObject.getJSONObject("respoObj");
                    //Log.e("msgss",msgObject.toString());
                    String respo = respoObject.getString("respo");
                    if(respo.equals("success"))
                        flag=1;
                    else
                        flag=0;
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(progress.isShowing()){
                        progress.dismiss();
                        if(flag==1) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(PayUMethod.this, BillingPage.class);
                            bundle.putString("phoneNumber", phoneNumber);
                            bundle.putString("restaurantId", restaurantId);
                            bundle.putString("orderId", orderId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(),
                                    "Sorry error in  bank server",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            handler.postDelayed(runnable,5000);
        }
    }

}
