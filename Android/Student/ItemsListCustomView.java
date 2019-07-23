//done
package com.example.shubham.takeaway.Student;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.takeaway.R;

import java.util.LinkedList;
import java.util.List;

public class ItemsListCustomView extends BaseAdapter {
    private List dishName;
    private Integer[] imgId;
    private List dishPrice;
    private List dishIngrediants;
    private Context context;
    private List<Integer>itemNo = new LinkedList<>();
    private List<Integer>itemQnt = new LinkedList<>();
    private LayoutInflater layoutInflater;

    public ItemsListCustomView(Context context, List dishName, Integer[] imgId, List dishPrice, List dishIngrediants){
        this.context = context;
        this.dishName = dishName;
        this.imgId = imgId;
        this.dishPrice = dishPrice;
        this.dishIngrediants = dishIngrediants;
        Log.e("tags:",dishName.toString());
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return dishName.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        Log.e("getView","get");
        //final View view1 = view;
        if(view==null){
            //view = layoutInflater.inflate(R.layout.menu_items_customview,null);
            view = LayoutInflater.from(context).inflate(R.layout.menu_items_customview,parent,false);
            Log.e("nulView","yes");
        }

        TextView textView1 = view.findViewById(R.id.menu_item_price);
        TextView textView2 = view.findViewById(R.id.ingrediants);
        TextView textView = view.findViewById(R.id.menu_item_name);
        ImageView imageView = view.findViewById(R.id.itemImage);

        textView.setText(dishName.get(position).toString());
        textView2.setText(dishIngrediants.get(position).toString());
        textView1.setText(dishPrice.get(position).toString());
        imageView.setImageResource(imgId[position]);

        //Log.e("dihN",dishName.toString());

        final Button addItemToCart = view.findViewById(R.id.addItemInCart);
        final TextView menuItemName = view.findViewById(R.id.menu_item_name);
        final TextView menuItemPrice = view.findViewById(R.id.menu_item_price);

        final Button incQnt = view.findViewById(R.id.incItemInCart);
        final Button decQnt = view.findViewById(R.id.decItemInCart);
        final TextView showQnt = view.findViewById(R.id.viewQnt);

        if(!itemNo.isEmpty()){
            for(int i=0;i<itemNo.size();i++){
                if(position==itemNo.get(i)){
                    Log.e("iff",itemNo.toString());
                    incQnt.setVisibility(View.VISIBLE);
                    decQnt.setVisibility(View.VISIBLE);
                    showQnt.setVisibility(View.VISIBLE);
                    addItemToCart.setVisibility(View.INVISIBLE);
                }
            }
        }

        addItemToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"ADDED",Toast.LENGTH_SHORT).show();
                final String itemName = menuItemName.getText().toString();
                final String itemPrices = menuItemPrice.getText().toString();

                addItemToArray(position,itemName,"1",itemPrices);
                incQnt.setVisibility(View.VISIBLE);
                decQnt.setVisibility(View.VISIBLE);
                showQnt.setVisibility(View.VISIBLE);
                addItemToCart.setVisibility(View.INVISIBLE);

                itemNo.add(position);
            }
        });

        incQnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"ADDED",Toast.LENGTH_SHORT).show();
                final String itemName = menuItemName.getText().toString();
                final String itemPrices = menuItemPrice.getText().toString();

                addItemToArray(position,itemName,"1",itemPrices);
                String text = showQnt.getText().toString();
                int num = Integer.parseInt(text);
                int incQnt = num+1;
                showQnt.setText(String.valueOf(incQnt));
            }
        });

        decQnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"REMOVED",Toast.LENGTH_SHORT).show();
                final String itemName = menuItemName.getText().toString();
                final String itemPrices = menuItemPrice.getText().toString();

                addItemToArray(position,itemName,"-1",itemPrices);
                String text = showQnt.getText().toString();
                int num = Integer.parseInt(text);
                int incQnts = num-1;
                showQnt.setText(String.valueOf(incQnts));
                if(text.equals("1")){
                    showQnt.setText("1");
                    incQnt.setVisibility(View.INVISIBLE);
                    decQnt.setVisibility(View.INVISIBLE);
                    showQnt.setVisibility(View.INVISIBLE);
                    addItemToCart.setVisibility(View.VISIBLE);
                    itemNo.remove(position);
                }


            }
        });

        return view;
    }
    public void addItemToArray(int itemId,String itemName,String itemQnt,String itemPrice){
        ItemsList itemsList = new ItemsList();
        itemsList.addItemInList(itemId,itemName,itemQnt,itemPrice);
    }
}
