package com.example.test.SetUpProfile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;
import com.example.test.User.AllCategories;
import com.example.test.User.UserDashBoard;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpProfile1 extends AppCompatActivity {

    ImageView backBtn;
    Button next, login;
    TextView titleText;
    RelativeLayout progressBar;

    CircleImageView profileImg;
    Uri imageUri;

    TextInputLayout fullName,address,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        next = findViewById(R.id.setup_next_btn);
        titleText = findViewById(R.id.setup_title_text);
        login = findViewById(R.id.setup_login_btn);
        backBtn = findViewById(R.id.setup_back_btn);

        progressBar = findViewById(R.id.set_up_profile_progress_bar_1);
        progressBar.setVisibility(View.GONE);

        fullName = findViewById(R.id.setup_full_name);
        username = findViewById(R.id.setup_user_name);
        address = findViewById(R.id.setup_address);
        profileImg = findViewById(R.id.profile_image);



        //OnClick
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to pick image from gallery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //set type
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpProfile1.super.onBackPressed();
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

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will handle the result of out intent
                    if(result.getResultCode() == Activity.RESULT_OK){
                        //image picked
                        //get uri of image
                        Intent data = result.getData();
                        imageUri = data.getData();
                        profileImg.setImageURI(imageUri);
                    }else{
                        //cancelled
                        Toast.makeText(getApplicationContext(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    public void call2rdSignupScreen(View view){

        if(!validateFullName() | !validateUserName() | !validateAddress() ){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String _fullName = fullName.getEditText().getText().toString().trim();
        String _address = address.getEditText().getText().toString().trim();
        String _username = username.getEditText().getText().toString().trim();
        String _profileImg = imageUri.toString();

        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");

        Intent intent = new Intent(SetUpProfile1.this, SetUpProfile2.class);

        //Pass all fields to the next activity
        intent.putExtra("fullName", _fullName);
        intent.putExtra("address", _address);
        intent.putExtra("username", _username);
        intent.putExtra("profileImg", _profileImg);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);

        //Add Transition
        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View, String>(next, "transition_next_btn");
        pairs[1] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");

        if(Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SetUpProfile1.this,pairs);
            startActivity(intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    private boolean validateFullName(){
        String val = fullName.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            fullName.setError("Không được để trống");
            return false;
        }else{
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(val.isEmpty()){
            username.setError("Không được để trống");
            return false;
        }else if(val.length()>20){
            username.setError("Tên đăng nhập quá dài!");
            return false;
        }
        else if(!val.matches(checkspaces)){
            username.setError("không được có khoảng trắng");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAddress(){
        String val = address.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            address.setError("Không được để trống");
            return false;
        }
        else{
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

}