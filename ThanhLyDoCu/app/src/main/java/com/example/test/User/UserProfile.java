package com.example.test.User;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Databases.SessionManager;
import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ImageView backBtn;
    EditText pFullName, pUsername;
    TextInputLayout pPhoneNo, pAddress, pDate, pGender, pEmail;
    CircleImageView pImage;
    private Button updateAccountSettings;
    TextView displayNoOfFavorites;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef, FavoritesRef;
    private StorageReference UserProfileImageRef;

    private ProgressDialog mLoadingBar;
    private String currentUserID;
    Uri imageUri;

    RelativeLayout favoriteLayout;

    int countFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        backBtn = findViewById(R.id.back_pressed);

        pFullName = findViewById(R.id.profile_full_name);
        pUsername = findViewById(R.id.profile_user_name);
        pPhoneNo = findViewById(R.id.profile_phoneNo);
        pAddress = findViewById(R.id.profile_address);
        pDate = findViewById(R.id.profile_day_of_birth);
        pImage = findViewById(R.id.profile_img);
        pGender = findViewById(R.id.profile_gender);
        pEmail = findViewById(R.id.profile_email);
        favoriteLayout = findViewById(R.id.user_favorite_product_layout);
        displayNoOfFavorites = findViewById(R.id.quantity_pro_favorite);

        mLoadingBar = new ProgressDialog(this);

        updateAccountSettings = findViewById(R.id.settings_update_account_btn);

        //Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(currentUserID + ".jpg");;
        FavoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorites").child(currentUserID);;

        //Onclick

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfile.super.onBackPressed();
            }
        });

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to pick image from gallery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //set type
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAccountInfo();
            }
        });

        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToUserFavoriteProduct();
            }
        });

        DisplayCountMyFavorites();



    }

    private void DisplayCountMyFavorites() {
        FavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countFavorites = (int) snapshot.getChildrenCount();
                displayNoOfFavorites.setText(Integer.toString(countFavorites));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        mLoadingBar.setTitle("Profile Image");
                        mLoadingBar.setMessage("Please wait, while we updating your profile image...");
                        mLoadingBar.show();
                        mLoadingBar.setCanceledOnTouchOutside(true);
                        //image picked
                        //get uri of image
                        Intent data = result.getData();
                        imageUri = data.getData();
                        pImage.setImageURI(imageUri);

                        UserProfileImageRef.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final String downloadUrl = uri.toString();
                                                mRef.child("profileImg").setValue(downloadUrl)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Intent selfIntent = new Intent(UserProfile.this, UserProfile.class);
                                                                    startActivity(selfIntent);

                                                                    Toast.makeText(UserProfile.this, "Profile Image stored to  Successfully...", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else
                                                                {
                                                                    String message = task.getException().getMessage();
                                                                    Toast.makeText(UserProfile.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                                                                }
                                                                mLoadingBar.dismiss();
                                                            }
                                                        });
                                            }
                                        });

                                    }
                                });

                    }else{
                        //cancelled
                        Toast.makeText(getApplicationContext(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser == null)
        {
            SendUserToLoginActivity();
        }else{
            //Database
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String address = snapshot.child("address").getValue(String.class);
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                        String profileImg = snapshot.child("profileImg").getValue(String.class);
                        String username = snapshot.child("username").getValue(String.class);
                        String date = snapshot.child("date").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        pAddress.getEditText().setText(address);
                        pFullName.setText(fullName);
                        pGender.getEditText().setText(gender);
                        pPhoneNo.getEditText().setText(phoneNo);
                        Picasso.get().load(profileImg).into(pImage);
                        pUsername.setText(username);
                        pDate.getEditText().setText(date);
                        pEmail.getEditText().setText(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserProfile.this, "Xin lỗi ! Có gì đó lỗi rồi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(UserProfile.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void ValidateAccountInfo() {

        String username = pUsername.getText().toString();
        String fullName = pFullName.getText().toString();
        String address = pAddress.getEditText().getText().toString();
        String gender = pGender.getEditText().getText().toString();
        String phoneNo = pPhoneNo.getEditText().getText().toString();
        String birth = pDate.getEditText().getText().toString();
        String email = pEmail.getEditText().getText().toString();


        if(username.isEmpty() || username.length()<3)
        {
            pUsername.setError("plz write your Username...");
        }else if(fullName.isEmpty() || fullName.length()<3){
            pFullName.setError("plz write your fullName...");
        }else if(address.isEmpty() || address.length()<3){
            pAddress.setError("plz write your address");
        }else if(gender.isEmpty() || gender.length()<3){
            pGender.setError("plz write your gender");
        }else if(phoneNo.isEmpty() || phoneNo.length()<3){
            pPhoneNo.setError("plz write your phone number");
        }else if(birth.isEmpty() || birth.length()<3){
            pDate.setError("plz write your date of birth");
        }else if(email.isEmpty() || email.length()<3){
            pEmail.setError("plz write your Email");
        }
        else{

            UpdateAccountInfo(username, fullName, address, phoneNo, birth, gender, email);
        }
    }

    private void UpdateAccountInfo(String username, String fullName, String address, String phoneNo, String birth, String gender, String email) {

        HashMap userMap = new HashMap();
        userMap.put("username", username);
        userMap.put("fullName", fullName);
        userMap.put("address", address);
        userMap.put("phoneNo", phoneNo);
        userMap.put("date", birth);
        userMap.put("gender", gender);
        userMap.put("email", email);
        mRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    SendUserToMainActivity();
                    Toast.makeText(UserProfile.this, "Account Settings Updated Successfully...", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserProfile.this, "Error Occured, while updating account settings info..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(UserProfile.this, UserDashBoard.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToUserFavoriteProduct()
    {
        Intent favoriteIntent = new Intent(UserProfile.this, UserFavoriteProduct.class);
        startActivity(favoriteIntent);
        finish();
    }
}