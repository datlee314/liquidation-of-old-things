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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class SetUpProfile2 extends AppCompatActivity {

    ImageView backBtn;
    Button next,login;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        next = findViewById(R.id.setup_next_btn);
        login = findViewById(R.id.setup_login_btn);
        backBtn = findViewById(R.id.setup_back_btn);

        radioGroup = findViewById(R.id.radio_group);
        datePicker = (DatePicker) findViewById(R.id.age_picker);

        progressBar = findViewById(R.id.set_up_profile_progress_bar_2);
        progressBar.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpProfile2.super.onBackPressed();
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

    public void call3rdSignupScreen(View view){

        if(!validateGender() | !validateAge()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String _gender =  selectedGender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();


        String _date = String.valueOf(day+"/"+month+"/"+year);

        //Get all values passed from previous screen using Intent
        String _fullName = getIntent().getStringExtra("fullName");
        String _address = getIntent().getStringExtra("address");
        String _username = getIntent().getStringExtra("username");
        String _profileImg = getIntent().getStringExtra("profileImg");
        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");

        Intent intent = new Intent(getApplicationContext(), SetUpProfile3.class);

        //Pass all fields to the next activity
        intent.putExtra("fullName", _fullName);
        intent.putExtra("address", _address);
        intent.putExtra("username", _username);
        intent.putExtra("profileImg", _profileImg);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);

        //Add Transition
        Pair[] pairs = new Pair[2];

        pairs[0] = new Pair<View, String>(next, "transition_next_btn");
        pairs[1] = new Pair<View, String>(login, "transition_slide_text");

        if(Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SetUpProfile2.this,pairs);
            startActivity(intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    private boolean validateGender(){
        if(radioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if(isAgeValid < 14){
            Toast.makeText(this, "Tuổi của bạn quá nhỏ", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }
}