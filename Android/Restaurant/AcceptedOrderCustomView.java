package com.example.shubham.takeaway.Restaurant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.takeaway.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AcceptedOrderCustomView extends BaseAdapter {
    private List itemName;
    private List orderIds;
    private List orderAmnt;
    private List itemQnt;
    private List orderTime;
    private Context context;
    private String restaurantId;
    private List cookingStatus;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public AcceptedOrderCustomView(Context context, Activity activity,List itemName, List orderIds,
                                   List orderAmnt, List itemQnt, List orderTime, String restaurantId,
                                   List cookingStatus){
        this.context = context;
        this.itemName = itemName;
        this.orderIds = orderIds;
        this.orderAmnt = orderAmnt;
        this.itemQnt = itemQnt;
        this.orderTime =orderTime;
        this.restaurantId = restaurantId;
        this.activity=activity;
        this.cookingStatus = cookingStatus;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return itemName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(final int position, View view, ViewGroup parent) {
        String s1="";
        String s2="";
        view = layoutInflater.inflate(R.layout.accepted_orders_customview,null);

        final TextView orderNumber = view.findViewById(R.id.orderNos);
        TextView itemAmount = view.findViewById(R.id.billAmount);
        TextView timeDisplay = view.findViewById(R.id.orderTimeDisplay);
        TextView itemNames = view.findViewById(R.id.itemList);
        TextView itemQnts = view.findViewById(R.id.qntAmnt);

        orderNumber.setText(orderIds.get(position).toString());
        itemAmount.setText(orderAmnt.get(position).toString());
        timeDisplay.setText(orderTime.get(position).toString());
        String itemNameText = itemName.get(position).toString();
        String itemQntValue = itemQnt.get(position).toString();
        String[] itemNameArray = itemNameText.split(",");
        String[] itemQntsArray = itemQntValue.split(",");
        for(int i=0;i<itemNameArray.length;i++){
            String itemname = itemNameArray[i]+"\n";
            s1 = s1+itemname;
            String itemqnt = itemQntsArray[i]+"\n";
            s2 = s2+itemqnt;
        }
        itemNames.setText(s1);
        itemQnts.setText(s2);
        /*
        * 0 - for no status
        * 1 - for order accepted
        * 2 - for order rejected
        * 3 - for order cooking
        * 4 - for order delivered
        * */


        final Button cookingOrder = view.findViewById(R.id.cookingOrder);
        final Button deliveredOrder = view.findViewById(R.id.deliveredOrder);

        if(cookingStatus.get(position).equals("1")){
            Log.e("status:-","hii");
            cookingOrder.setEnabled(false);
            cookingOrder.setBackgroundColor(R.color.blocked);
            deliveredOrder.setBackgroundColor(R.color.statusCooking);
            deliveredOrder.setEnabled(true);
        }
        else {
            deliveredOrder.setBackgroundColor(R.color.blocked);
            deliveredOrder.setEnabled(false);
        }
        cookingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cookingOrder.setBackgroundColor(R.color.status);
                String id = orderNumber.getText().toString();
                String flag = "3";
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.execute(id, restaurantId, flag);
                cookingOrder.setEnabled(false);
                cookingOrder.setBackgroundColor(R.color.blocked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deliveredOrder.setEnabled(true);
                        deliveredOrder.setBackgroundColor(R.color.statusCooking);
                    }
                },5000);
            }
        });

        deliveredOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deliveredOrder.setBackgroundColor(R.color.status);
                String id = orderNumber.getText().toString();
                String flag = "4";
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.execute(id, restaurantId, flag);
                //((AcceptedOrder)activity).refreshList();
            }
        });
        return view;
    }

    private class OrderStatus extends AsyncTask<String,Void,String> {
        String flag;
        @Override
        protected String doInBackground(String... strings) {
            String POST_URL = "http://192.168.1.198:22849/TakeAway/OrderStatus";
            String itemId = strings[0];
            String restaurantID = strings[1];
            flag = strings[2];
            StringBuffer output = new StringBuffer();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("orderId",itemId);
                jsonObject.put("restaurantID",restaurantID);
                jsonObject.put("flagValue",flag);
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
                    Toast.makeText(context,"Accepted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Rejected",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("error",e.toString());
            }
            return itemId;
        }

        @Override
        protected void onPostExecute(String s) {
            //context.startActivity(new Intent(context,NewOrderActivity.class));
            if (flag.equals("4")){
                ((AcceptedOrder)activity).refreshList();
            }
        }
    }
}
