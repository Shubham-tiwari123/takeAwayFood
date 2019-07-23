//done
package com.example.shubham.takeaway.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shubham.takeaway.R;

import java.util.List;

public class BillingPageCustomView extends BaseAdapter {
    private List itemName;
    private List itemQnt;
    private List itemPrice;
    private Context context;
    private LayoutInflater layoutInflater;

    public BillingPageCustomView(Context context, List itemName,List itemQnt,
                                 List itemPrice){
        this.context = context;
        this.itemName = itemName;
        this.itemQnt = itemQnt;
        this.itemPrice = itemPrice;
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
        view = layoutInflater.inflate(R.layout.student_billing_page,null);

        TextView itemNames = view.findViewById(R.id.itemName);
        TextView itemQnts = view.findViewById(R.id.itemQnt);
        TextView itemValue = view.findViewById(R.id.itemPrice);

        itemNames.setText(itemName.get(position).toString());
        itemQnts.setText(itemQnt.get(position).toString());
        itemValue.setText(itemPrice.get(position).toString());
        return view;
    }
}
