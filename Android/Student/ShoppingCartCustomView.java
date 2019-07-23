package com.example.shubham.takeaway.Student;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shubham.takeaway.R;

public class ShoppingCartCustomView extends ArrayAdapter<String> {
    private String[] itemName;
    private String[] itemPrice;
    private String[] itemQnt;
    private Activity context;

    public ShoppingCartCustomView(Activity context, String[] itemName, String[] itemPrice,
                                  String[] itemQnt){
        super(context, R.layout.cartcustomview,itemName);
        this.context = context;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQnt = itemQnt;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ShoppingCartCustomView.ViewHolder viewHolder=null;

        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r= layoutInflater.inflate(R.layout.cartcustomview,null,true);
            viewHolder = new ShoppingCartCustomView.ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ShoppingCartCustomView.ViewHolder)r.getTag();
        }
        viewHolder.nameView.setText(itemName[position]);
        viewHolder.priceView.setText(itemPrice[position]);
        viewHolder.qntView.setText(itemQnt[position]);


        Button deleteBtn = r.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShoppingCart)context).deleteItemFromList(position);
            }
        });

        return r;
    }

    class ViewHolder{
        TextView nameView;
        TextView priceView;
        TextView qntView;
        ViewHolder(View view){
            nameView = view.findViewById(R.id.itemName);
            priceView = view.findViewById(R.id.itemPrice);
            qntView = view.findViewById(R.id.itemQuantity);
        }
    }
}
