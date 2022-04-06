package com.example.test.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.test.R;
import com.hbb20.CountryCodePicker;

public class PhoneAuth extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText phoneNumber;
    Button getOTP;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        phoneNumber = findViewById(R.id.etPhoneNumber);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNumber);
        getOTP = findViewById(R.id.btnGetOTP);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);



        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageOTP.class);
                intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace("",""));
                startActivity(intent);

            }
        });
    }
}