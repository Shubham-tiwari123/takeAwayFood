package com.example.shubham.takeaway.Restaurant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shubham.takeaway.R;

import java.util.List;

public class RestaurantBillsCustomView extends BaseAdapter {
    private List itemName;
    private List orderIds;
    private List orderAmnt;
    private List itemQnt;
    private List orderDate;
    private List itemPrice;
    private List orderStatus;
    private Context context;
    private LayoutInflater layoutInflater;

    public RestaurantBillsCustomView(Context context, List itemName,List orderIds,
                            List orderAmnt,List itemQnt,List orderDate,List itemPrice,List orderStatus){
        this.context = context;
        this.itemName = itemName;
        this.orderIds = orderIds;
        this.orderAmnt = orderAmnt;
        this.itemQnt = itemQnt;
        this.orderDate =orderDate;
        this.itemPrice = itemPrice;
        this.orderStatus = orderStatus;
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
        String s1 = "";
        String s2 = "";
        String s3="";
        view = layoutInflater.inflate(R.layout.bills_restaurant, null);

        TextView orderNumber = view.findViewById(R.id.orderNos);
        TextView itemAmount = view.findViewById(R.id.billAmount);
        //TextView itemTotalNo = (TextView)view.findViewById(R.id.itemQnt);
        TextView itemNames = view.findViewById(R.id.itemList);
        TextView orderDates = view.findViewById(R.id.orderDateDisplay);
        TextView itemQnts = view.findViewById(R.id.qntAmnt);
        TextView itemValue = view.findViewById(R.id.itemValue);
        TextView statusCode = view.findViewById(R.id.orderStatusCode);

        orderNumber.setText(orderIds.get(position).toString());
        itemAmount.setText(orderAmnt.get(position).toString());
        //itemTotalNo.setText(itemNo.get(position).toString());
        statusCode.setText(orderStatus.get(position).toString());
        String code = statusCode.getText().toString();
        if(code.equals("Accepted"))
            statusCode.setTextColor(Color.parseColor("#85bf31"));
        else
            statusCode.setTextColor(Color.parseColor("#F80A0A"));

        orderDates.setText(orderDate.get(position).toString());
        String itemNameText = itemName.get(position).toString();
        String itemQntValue = itemQnt.get(position).toString();
        String itemPriceValue = itemPrice.get(position).toString();

        String[] itemPriceArray = itemPriceValue.split(",");
        String[] itemNameArray = itemNameText.split(",");
        String[] itemQntsArray = itemQntValue.split(",");

        for(int i=0;i<itemNameArray.length;i++){
            String itemname = itemNameArray[i]+"\n";
            s1 = s1+itemname;
            String itemqnt = itemQntsArray[i]+"\n";
            s2 = s2+itemqnt;
            String itemprice = "₹  "+itemPriceArray[i]+"\n";
            s3 =s3+itemprice;
        }
        itemValue.setText(s3);
        itemNames.setText(s1);
        itemQnts.setText(s2);
        return view;
    }
}
