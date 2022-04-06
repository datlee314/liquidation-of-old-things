package com.example.test.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class SetNewPassword extends AppCompatActivity {

    private ImageView screenIcon,backBtn;
    private TextView title, description;
    private TextInputLayout setNewPass, confirmSetNewPass;
    private Button btnSetNewPass;
    private Animation animation;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        screenIcon = findViewById(R.id.update_new_password_icon);
        backBtn = findViewById(R.id.update_new_password_back_btn);
        title = findViewById(R.id.update_new_password_title);
        description = findViewById(R.id.update_new_password_desc);
        setNewPass = findViewById(R.id.set_new_password);
        confirmSetNewPass = findViewById(R.id.confirm_set_new_password);
        btnSetNewPass = findViewById(R.id.btn_set_new_pass);

//Animation Hooks
        animation = AnimationUtils.loadAnimation(this, R.anim.o_b_anim);

        //Set animation to all the elements
        screenIcon.setAnimation(animation);
        btnSetNewPass.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        setNewPass.setAnimation(animation);
        confirmSetNewPass.setAnimation(animation);
        backBtn.setAnimation(animation);
    }

    public void setNewPasswordBtn(View view){

        //validate phone number
        if(!validatePassword() | !validateConfirmPassword()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Get data from fields
        String _newPassword = setNewPass.getEditText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        //Update Data in Firebase and in Sessions
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.child(_phoneNumber).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
        finish();
    }

    private boolean validatePassword() {
        String val = setNewPass.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            setNewPass.setError("Không được để trống");
            return false;
        } else if (!val.matches(checkPassword)) {
            setNewPass.setError("Mật khẩu phải bao gồm 4 kiểu kí tự!");
            return false;
        } else {
            setNewPass.setError(null);
            setNewPass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String val = confirmSetNewPass.getEditText().getText().toString().trim();
        String _newPassword = setNewPass.getEditText().getText().toString().trim();

        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            confirmSetNewPass.setError("Không được để trống");
            return false;
        } else if (!val.matches(checkPassword)) {
            confirmSetNewPass.setError("Mật khẩu phải bao gồm 4 kiểu kí tự!");
            return false;
        }
        else if(!_newPassword.equals(val))
        {
            confirmSetNewPass.setError("Xác nhận mật khẩu sai");
            return true;
        }else {
            confirmSetNewPass.setError(null);
            confirmSetNewPass.setErrorEnabled(false);
            return true;
        }
    }
}