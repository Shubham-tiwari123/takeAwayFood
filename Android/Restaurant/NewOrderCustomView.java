package com.example.shubham.takeaway.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
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

public class NewOrderCustomView extends BaseAdapter {
    private List itemName;
    private List orderIds;
    private List orderAmnt;
    private List itemQnt;
    private List orderTime;
    private Context context;
    private String restaurantId;
    private LayoutInflater layoutInflater;

    public NewOrderCustomView(Context context, List itemName, List orderIds,
                              List orderAmnt, List itemQnt, List orderTime,String restaurantId){
        this.context = context;
        this.itemName = itemName;
        this.orderIds = orderIds;
        this.orderAmnt = orderAmnt;
        this.itemQnt = itemQnt;
        this.orderTime =orderTime;
        this.restaurantId = restaurantId;
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

    public View getView(final int position, View view, ViewGroup parent) {
        String s1="";
        String s2="";
        view = layoutInflater.inflate(R.layout.new_order_list,null);

        final TextView orderNumber = view.findViewById(R.id.orderNos);
        TextView itemAmount = view.findViewById(R.id.billAmount);
        TextView timeDisplay = view.findViewById(R.id.orderTimeDisplay);
        TextView itemNames = view.findViewById(R.id.itemList);
        TextView itemQnts = view.findViewById(R.id.qntAmnt);
        TextView emptyMsg = view.findViewById(R.id.emptyOrder);
        final CardView cardView = view.findViewById(R.id.cardView);

        orderNumber.setText(orderIds.get(position).toString());
        itemAmount.setText(orderAmnt.get(position).toString());
        timeDisplay.setText(orderTime.get(position).toString());
        String itemNameText = itemName.get(position).toString();
        String itemQntValue = itemQnt.get(position).toString();
        String[] itemNameArray = itemNameText.split(",");
        String[] itemQntsArray = itemQntValue.split(",");
        for (int i = 0; i < itemNameArray.length; i++) {
            String itemname = itemNameArray[i] + "\n";
            s1 = s1 + itemname;
            String itemqnt = itemQntsArray[i] + "\n";
            s2 = s2 + itemqnt;
        }
        itemNames.setText(s1);
        itemQnts.setText(s2);

        final Button acceptOrder = view.findViewById(R.id.acceptOrder);
        final Button rejectOrder = view.findViewById(R.id.rejectOrder);
        /*
         * 0 - for no status
         * 1 - for order accepted
         * 2 - for order rejected
         * 3 - for order cooking
         * 4 - for order delivered
         * */
        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = orderNumber.getText().toString();
                String flag = "1";
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.execute(id, restaurantId, flag);
            }
        });

        rejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = orderNumber.getText().toString();
                String flag = "2";
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.execute(id, restaurantId, flag);
            }
        });

        return view;
    }

    private class OrderStatus extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String POST_URL = "http://192.168.1.198:22849/TakeAway/OrderStatus";
            String itemId = strings[0];
            String restaurantID = strings[1];
            String flag = strings[2];
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

            context.startActivity(new Intent(context,NewOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
