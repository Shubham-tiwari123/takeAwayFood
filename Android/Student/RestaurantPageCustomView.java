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

public class RestaurantPageCustomView extends BaseAdapter {

    private List nameList;
    private List openTime;
    private List closeTime;
    private List restaurantIDs;
    LayoutInflater layoutInflater;
    private  Context context;

    public RestaurantPageCustomView(Context context, List<String> nameList, List<String> openTime,
                                    List<String> closeTime, List<Integer> restaurantIDs){
        this.context = context;
        this.nameList = nameList;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.restaurantIDs = restaurantIDs;
        layoutInflater = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.restaurant_name,null);
        TextView restaurantName = view.findViewById(R.id.names);
        TextView openTimeing = view.findViewById(R.id.openTime);
        TextView closeTimeing = view.findViewById(R.id.closeTime);

        restaurantName.setText(nameList.get(position).toString());
        openTimeing.setText(openTime.get(position).toString());
        closeTimeing.setText(closeTime.get(position).toString());

        return view;
    }
}
