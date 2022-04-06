package com.example.test.SetUpProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.test.HelperClasses.UserHelperClass;
import com.example.test.R;
import com.example.test.User.UserDashBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class WelcomeActivity extends AppCompatActivity {

    ImageView closeBtn;
    Uri imageUri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference StorageRef;

    RelativeLayout progressBar;
    ImageView backBtn;

    String fullName, address, username, profileImg, date, gender, phoneNo;

    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        lottieAnimationView = findViewById(R.id.welcome_lottie);

        backBtn = findViewById(R.id.setup_back_btn);
        progressBar = findViewById(R.id.welcome_progress_bar_);
        progressBar.setVisibility(View.GONE);

        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        address = getIntent().getStringExtra("address");
        username = getIntent().getStringExtra("username");
        profileImg = getIntent().getStringExtra("profileImg");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        phoneNo = getIntent().getStringExtra("phoneNo");

        closeBtn = findViewById(R.id.setup_back_btn);

        imageUri = Uri.parse(profileImg);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WelcomeActivity.super.onBackPressed();
            }
        });

    }

    public void callNextScreenFromWelCome(View view){
        storeNewUsersData();
    }

    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        mRef = rootNode.getReference("Users");
        StorageRef= FirebaseStorage.getInstance().getReference().child("ProfileImages");

        progressBar.setVisibility(View.VISIBLE);

        //Create helperClass reference and store data using firebase

        StorageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    StorageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImg = uri.toString();
                            UserHelperClass addNewUser = new UserHelperClass(fullName, address, username, profileImg, date, gender,phoneNo);
                            mRef.child(mUser.getUid()).setValue(addNewUser);
                            startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                            finish();
                        }
                    });
                }
            }
        });

    }
}