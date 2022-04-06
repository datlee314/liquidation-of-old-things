package com.example.test.SetUpProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SetUpProfile3 extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;

    Button login;
    ImageView backBtn;

    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_up_profile3);

        //Hooks
        scrollView = findViewById(R.id.setup_3rd_screen_scroll_view);
        phoneNumber = findViewById(R.id.setup_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);

        login = findViewById(R.id.setup_login_btn);
        backBtn = findViewById(R.id.setup_back_btn);

        progressBar = findViewById(R.id.set_up_profile_progress_bar_3);
        progressBar.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpProfile3.super.onBackPressed();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    }

    public void callVerifyOTPScreen(View view){

        //Validate fields
        if(!validatePhoneNumber()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //Get all values passed from previous screen using Intent
        String _fullName = getIntent().getStringExtra("fullName");
        String _address = getIntent().getStringExtra("address");
        String _username = getIntent().getStringExtra("username");
        String _profileImg = getIntent().getStringExtra("profileImg");
        String _date = getIntent().getStringExtra(" date");
        String _gender = getIntent().getStringExtra("gender");

        //Get Phone Numbẻr
        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();

        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
        final String _phoneNo = "+"+countryCodePicker.getSelectedCountryCodeWithPlus()+_getUserEnteredPhoneNumber;

        Intent intent =new Intent(getApplicationContext(), WelcomeActivity.class);

        //Pass all fields to the next activity
        intent.putExtra("fullName", _fullName);
        intent.putExtra("address", _address);
        intent.putExtra("username", _username);
        intent.putExtra("profileImg", _profileImg);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);
        intent.putExtra("phoneNo", _phoneNo);

        //Add Transition
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(scrollView, "transition_OTP_Screen");
        if(Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SetUpProfile3.this,pairs);
            startActivity(intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }
    private boolean validatePhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumber.setError("Nhập đúng số điện thoại");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("Không được có khoảng cách!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }
}