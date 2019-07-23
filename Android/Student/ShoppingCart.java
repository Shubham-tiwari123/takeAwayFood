package com.example.shubham.takeaway.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import org.w3c.dom.Text;

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
import java.util.List;

public class ShoppingCart extends AppCompatActivity {
    ArrayList<String> itemNameObj;
    ArrayList<String> itemPriceObj;
    ArrayList<String> itemQntObj;
    ArrayList<Integer> itemIDObj;
    ArrayList<String> userData;
    CardView amountDisplay;
    int sum;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_shoping_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);

        amountDisplay = findViewById(R.id.cardView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        ArrayList<String> itemNameList = getItemNameObj();
        ArrayList<String> itemPriceList = getItemPriceObj();
        ArrayList<String> itemQntList = getItemQntObj();
        ArrayList<String> userDataList = getUserData();

        Log.e("userdata",userDataList.toString());
        ListView listView = findViewById(R.id.listview);

        String[] itemName = itemNameList.toArray(new String[]{});
        String[] itemQuan = itemQntList.toArray(new String[]{});
        String[] itemPrice = itemPriceList.toArray(new String[]{});
        String[] userDatas = userDataList.toArray(new String[]{});


        Button explore = findViewById(R.id.exploreMenu);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(getBaseContext(),ItemsList.class));
            }
        });
        TextView itemCap = findViewById(R.id.itemCap);
        if(itemNameList.isEmpty()){
            amountDisplay.setVisibility(View.INVISIBLE);
            explore.setVisibility(View.VISIBLE);
            itemCap.setVisibility(View.INVISIBLE);
        }
        else{
            amountDisplay.setVisibility(View.VISIBLE);
            explore.setVisibility(View.INVISIBLE);
            itemCap.setVisibility(View.VISIBLE);
        }

        ShoppingCartCustomView shoppingCartCustomView = new ShoppingCartCustomView(this,itemName,itemPrice,
                itemQuan);

        listView.setAdapter(shoppingCartCustomView);

        TextView subTotal = findViewById(R.id.subTotal);
        TextView serviceCharge = findViewById(R.id.serviceTotal);
        TextView grandAmount = findViewById(R.id.grandTotal);
        int grandTotal=0;

        if(!itemPriceList.isEmpty()) {
            for (int i = 0; i < itemPrice.length; i++) {
                grandTotal = grandTotal + Integer.parseInt(itemPrice[i]);
            }
            subTotal.setText(Integer.toString(grandTotal));
            serviceCharge.setText("2");
            sum = grandTotal+2;
            grandAmount.setText(Integer.toString(sum));
        }

        final String id = userDatas[0];
        final String number =userDatas[1];

        final Button placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Order Placed",Toast.LENGTH_SHORT).show();
                PlaceOrder placeOrder1 = new PlaceOrder();
                String amount = String.valueOf(sum);
                placeOrder1.execute(id,number,amount);
                //startActivity(new Intent(ShoppingCart.this,BillingPage.class));
            }
        });
    }

    public ArrayList<String> getUserData(){
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        userData = (ArrayList<String>)args.getSerializable("userData");
        return userData;
    }
    public ArrayList<String> getItemNameObj(){
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        itemNameObj = (ArrayList<String>)args.getSerializable("itemName");
        return itemNameObj;
    }
    public ArrayList<String> getItemQntObj(){
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        itemQntObj = (ArrayList<String>)args.getSerializable("itemQnt");
        return itemQntObj;
    }
    public ArrayList<String> getItemPriceObj(){
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        itemPriceObj = (ArrayList<String>)args.getSerializable("itemPrice");
        return itemPriceObj;
    }
    public ArrayList<Integer> getItemIDObj(){
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        itemIDObj = (ArrayList<Integer>)args.getSerializable("itemID");
        return itemIDObj;
    }

    public void deleteItemFromList(int itemID){

        amountDisplay = findViewById(R.id.cardView);
        ListView listView = findViewById(R.id.listview);
        Button orderButton = findViewById(R.id.placeOrder);

        ArrayList<String> itemNameList = getItemNameObj();
        ArrayList<String> itemPriceList = getItemPriceObj();
        ArrayList<String> itemQntList = getItemQntObj();
        ArrayList<Integer> itemIDList = getItemIDObj();

        itemNameList.remove(itemID);
        itemPriceList.remove(itemID);
        itemQntList.remove(itemID);
        itemIDList.remove(itemID);

        ItemsList itemsList = new ItemsList();
        itemsList.deleteItem(itemNameList,itemPriceList,itemQntList,itemIDList);

        String[] itemName = itemNameList.toArray(new String[]{});
        String[] itemQuan = itemQntList.toArray(new String[]{});
        String[] itemPrice = itemPriceList.toArray(new String[]{});

        Log.e("itemName:-",itemNameList.toString());
        if(itemName.length==0){
            Log.e("hiii:","if");
            amountDisplay.setVisibility(View.INVISIBLE);
        }
        else{
            Log.e("hiii2:","else");
            amountDisplay.setVisibility(View.VISIBLE);
        }

        ShoppingCartCustomView shoppingCartCustomView = new ShoppingCartCustomView(this,itemName,itemPrice,
                itemQuan);

        listView.setAdapter(shoppingCartCustomView);

        TextView subTotal = findViewById(R.id.subTotal);
        TextView serviceCharge = findViewById(R.id.serviceTotal);
        TextView grandAmount = findViewById(R.id.grandTotal);
        int grandTotal=0;
        Button explore = findViewById(R.id.exploreMenu);

        if(!itemPriceList.isEmpty()) {
            for (int i = 0; i < itemPrice.length; i++) {
                grandTotal = grandTotal + Integer.parseInt(itemPrice[i]);
            }
            subTotal.setText(Integer.toString(grandTotal));
            serviceCharge.setText("2");
            int sum = grandTotal+2;
            grandAmount.setText(Integer.toString(sum));
        }
        else{
            grandTotal = 0;
            subTotal.setText(Integer.toString(grandTotal));
            serviceCharge.setText("0");
            grandAmount.setText(Integer.toString(grandTotal));

            explore.setVisibility(View.VISIBLE);

            if(itemPriceList.isEmpty())
                amountDisplay.setVisibility(View.INVISIBLE);
            orderButton.setVisibility(View.INVISIBLE);
        }

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(getBaseContext(),ItemsList.class));
            }
        });
    }

    private class PlaceOrder extends AsyncTask<String, Void, List<String>>{
        String phoneNumber;
        String restaurantId;
        String orderId;
        String amount;
        @Override
        protected List<String> doInBackground(String... strings) {
            // http://192.168.1.198:22849/takeaway/PlaceUserOrderApi
            String POST_URL = "http://192.168.1.198:22849/TakeAway/OrderTable";
            restaurantId = strings[0];
            phoneNumber = strings[1];
            amount = strings[2];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("restaurantId",restaurantId);
                jsonObject.put("studentPhone",phoneNumber);
                jsonObject.put("totalAmount",amount);
                jsonObject.put("itemName",itemNameObj);
                jsonObject.put("itemPrice",itemPriceObj);
                jsonObject.put("itemQnt",itemQntObj);

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
                    String msg = outputObject.getString("msg");
                    if(msg.equals("True")){
                        orderId = outputObject.getString("itemName");
                    }
                    else
                        orderId = "-1";
                }

            }catch (JSONException e) {
                e.printStackTrace();
                Log.e("error1",e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("error2",e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error3",e.toString());
                }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ShoppingCart.this,PayUMethod.class);
            bundle.putString("phoneNumber",phoneNumber);
            bundle.putString("restaurantId",restaurantId);
            bundle.putString("orderId",orderId);
            bundle.putString("amount",amount);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
