package com.example.shubham.takeaway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.shubham.takeaway.Restaurant.RestaurantOtp;
import com.example.shubham.takeaway.Student.PayUMethod;
import com.example.shubham.takeaway.Student.StudentOtp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button studentSide = findViewById(R.id.verifyBtnStu);
        Button restaurantSide = findViewById(R.id.verifyBtnRes);

        studentSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentOtp.class));
            }
        });
        restaurantSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RestaurantOtp.class));
            }
        });
    }
    public void RadioButtonClicked(View view){
        RelativeLayout relativeLayout = findViewById(R.id.verifyForm);
        Button button = findViewById(R.id.verifyBtnStu);
        Button button1 = findViewById(R.id.verifyBtnRes);
        boolean checked = ((RadioButton)view).isChecked();
        EditText phoneNo = findViewById(R.id.phoneNoEdit1);
        EditText restaurantId = findViewById(R.id.restaurantId);

        switch (view.getId()){
            case R.id.radio1:
                if(checked){
                    relativeLayout.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    phoneNo.setVisibility(View.INVISIBLE);
                    restaurantId.setVisibility(View.VISIBLE);
                    phoneNo.setText("");
                    restaurantId.setText("");
                }
                break;
            case R.id.radio2:{
                if (checked){
                    relativeLayout.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    phoneNo.setVisibility(View.VISIBLE);
                    restaurantId.setVisibility(View.INVISIBLE);
                    phoneNo.setText("");
                    restaurantId.setText("");
                }
                break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText editText1 = findViewById(R.id.phoneNoEdit1);
        editText1.setText("");
    }
}
