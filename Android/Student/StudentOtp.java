package com.example.shubham.takeaway.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shubham.takeaway.R;

public class StudentOtp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Button button = findViewById(R.id.otpVerifyBtn);
        final String phoneNumber = "9004318447";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StudentOtp.this, RestaurantPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("phoneNumber",phoneNumber);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
