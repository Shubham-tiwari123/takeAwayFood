package com.example.shubham.takeaway.Restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shubham.takeaway.R;

public class    RestaurantOtp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_otp);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Button button = findViewById(R.id.otpVerifyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantOtp.this, NewOrderActivity.class));
            }
        });
    }
}
