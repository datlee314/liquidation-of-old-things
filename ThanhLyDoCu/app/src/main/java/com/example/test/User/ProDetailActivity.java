package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private ImageView productImg;
    ImageView imageView,imgCall,sms;
    private String img;
    private CircleImageView profileImg;
    private TextView price, name, category, phoneNo, status, area, date, description, profileName;
    FloatingActionButton backBtn;
    ImageButton sendMessageButton,favoriteBtn, commentBtn;
    EditText userMessageInput;
    Toolbar toolbar;

    String sellerID, productID, customerID, saveCurrentDate, saveCurrentTime;

    FirebaseAuth mAuth;
    DatabaseReference UserRef, ProductRef, ContactsRef, rootRef, NotificationRef, FavoritesRef;

    Boolean favoriteChecker = false;
    int countFavorites, countComments;
    TextView displayNoOfFavorites, displayNoOfComment;

    private long countFav = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_detail);
        imgCall=findViewById(R.id.product_detail_phone_btn1);
        productImg = findViewById(R.id.products_detail_img);
        profileImg = findViewById(R.id.product_detail_profile_img);
        price = findViewById(R.id.product_detail_price);
        name = findViewById(R.id.product_detail_name);
        category = findViewById(R.id.product_detail_category);
        phoneNo = findViewById(R.id.product_detail_phoneNo);
        status = findViewById(R.id.product_detail_status);
        area = findViewById(R.id.product_detail_local);
        date = findViewById(R.id.product_detail_time);
        description = findViewById(R.id.product_detail_description);
        profileName = findViewById(R.id.product_detail_full_name);
        sendMessageButton=findViewById(R.id.product_detail_send_message_button);
        userMessageInput = findViewById(R.id.product_detail_input_message);
        displayNoOfFavorites = findViewById(R.id.display_no_of_favorites);
        displayNoOfComment = findViewById(R.id.display_no_of_comment);

        commentBtn = findViewById(R.id.product_detail_comment_btn);

        rootRef = FirebaseDatabase.getInstance().getReference();
        ProductRef = FirebaseDatabase.getInstance().getReference().child("sanpham");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        //NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        FavoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        customerID = mAuth.getCurrentUser().getUid();

        sellerID = getIntent().getStringExtra("seller_id");
        productID = getIntent().getStringExtra("product_id");

        // toolbar
        toolbar =  findViewById(R.id.tool_bar);
        backBtn =  findViewById(R.id.product_detail_back_pressed);
        favoriteBtn =  findViewById(R.id.product_detail_favorite_group_btn);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //OnClick

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(getApplicationContext(),CommentsActivity.class);
                commentIntent.putExtra("productID",productID);
                startActivity(commentIntent);
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProFileCustomer.class);


                intent.putExtra("name",profileName.getText().toString());
                intent.putExtra("Profileimg",img);
                intent.putExtra("Phone",phoneNo.getText().toString());
                intent.putExtra("id",sellerID);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProDetailActivity.super.onBackPressed();
            }
        });

        setFavoriteButtonStatus(productID);
        
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FavoritesValidate();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendMessageValidate();

                SendMessage();

            }

        });


        DisplayProduct();

        DisplaySeller();

        DisplayCommentCount();
    }

    private void DisplayCommentCount() {
        ProductRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countComments = (int) snapshot.child("Comments").getChildrenCount();
                displayNoOfComment.setText((Integer.toString(countComments) + (" Comments")));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendMessageValidate() {
        UserRef.child(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String myImage = snapshot.child("profileImg").getValue().toString();
                    String myName = snapshot.child("username").getValue().toString();

                    Intent chatIntent = new Intent(ProDetailActivity.this, ChatActivity.class);
                    chatIntent.putExtra("seller_id", sellerID);
                    chatIntent.putExtra("fullName", myName);
                    chatIntent.putExtra("profileImg", myImage);
                    startActivity(chatIntent);

                    ContactToAPerson();
                    //SendNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FavoritesValidate() {
        
        FavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    countFav = snapshot.getChildrenCount();
                }
                else
                {
                    countFav = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favoriteChecker = true;

        FavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(favoriteChecker.equals(true))
                {
                    if(snapshot.child(productID).hasChild(customerID))
                    {
                        FavoritesRef.child(productID).child(customerID).removeValue();
                        FavoritesRef.child(customerID).child(productID).removeValue();
                    }
                    else{

                        ProductRef.child(productID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {

                                    String name = snapshot.child("tenhinh").getValue().toString();
                                    String price = snapshot.child("gia").getValue().toString();
                                    String image = snapshot.child("link").getValue().toString();

                                    HashMap favoriteMap = new HashMap();
                                    favoriteMap.put("productID", productID);
                                    favoriteMap.put("sellerID", sellerID);
                                    favoriteMap.put("name", name);
                                    favoriteMap.put("price", price);
                                    favoriteMap.put("image", image);
                                    favoriteMap.put("counter", countFav);

                                    FavoritesRef.child(productID).child(customerID).setValue(true)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        FavoritesRef.child(customerID).child(productID)
                                                                .updateChildren(favoriteMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful())
                                                                        {

                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    favoriteChecker = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setFavoriteButtonStatus(final String productID) {
        FavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(productID).hasChild(customerID))
                {
                    countFavorites = (int) snapshot.child(productID).getChildrenCount();
                    favoriteBtn.setImageResource(R.drawable.favor_icon);
                    displayNoOfFavorites.setText((Integer.toString(countFavorites) + (" Favorites")));
                }
                else
                {
                    countFavorites = (int) snapshot.child(productID).getChildrenCount();
                    favoriteBtn.setImageResource(R.drawable.disfavor_icon);
                    displayNoOfFavorites.setText((Integer.toString(countFavorites) + (" Favorites")));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void SendMessage() {

        updateUserStatus("online");

        String messageText = userMessageInput.getText().toString();

        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "Hãy viết gì đó....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String message_sender_ref = "Messages/" + customerID + "/" + sellerID;
            String message_receiver_ref = "Messages/" + sellerID + "/" + customerID;

            DatabaseReference user_message_key = rootRef.child("Messages").child(customerID)
                    .child(sellerID).push();
            String message_push_id = user_message_key.getKey();

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
            saveCurrentTime = currentTime.format(calFordTime.getTime());

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("type", "Inbox");
            messageTextBody.put("productID", productID);
            messageTextBody.put("sellerID", sellerID);
            messageTextBody.put("from", customerID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);
            messageBodyDetails.put(message_receiver_ref + "/" + message_push_id, messageTextBody);


            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Gửi tin nhắn thành công", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    }
                    userMessageInput.setText("");


                }
            });
        }
    }

    private void SendNotification() {

        Map notificationMap = new HashMap();
        notificationMap.put("from", customerID);
        notificationMap.put("type", "Contacts Request");

        NotificationRef.child(customerID).child(sellerID)
                .updateChildren(notificationMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                        }
                    }
                });
    }

    private void ContactToAPerson() {

        ContactsRef.child(customerID).child(sellerID)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            ContactsRef.child(sellerID).child(customerID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {

                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    public void updateUserStatus(String state)
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        saveCurrentTime = currentTime.format(calFordTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time", saveCurrentTime);
        currentStateMap.put("date", saveCurrentDate);
        currentStateMap.put("type", state);

        UserRef.child(customerID).child("userState")
                .updateChildren(currentStateMap);
    }

    private void DisplaySeller() {

        UserRef.child(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String myImage = snapshot.child("profileImg").getValue().toString();
                    String myName = snapshot.child("username").getValue().toString();
                    img=myImage;
                    Picasso.get().load(myImage).placeholder(R.drawable.profile_img).into(profileImg);
                    profileName.setText(myName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayProduct() {

        ProductRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    String proImg = snapshot.child("link").getValue().toString();
                    String proPrice = snapshot.child("gia").getValue().toString();
                    String proDate = snapshot.child("date").getValue().toString();
                    String proAre = snapshot.child("khuvuc").getValue().toString();
                    String proDescription = snapshot.child("noidung").getValue().toString();
                    String proPhoneNo = snapshot.child("sdt").getValue().toString();
                    String proCategory = snapshot.child("tendm").getValue().toString();
                    String proName = snapshot.child("tenhinh").getValue().toString();
                    String proStatus = snapshot.child("tinhtrang").getValue().toString();

                    Picasso.get().load(proImg).placeholder(R.drawable.product_img).into(productImg);
                    price.setText(proPrice);
                    date.setText(proDate);
                    area.setText(proAre);
                    description.setText(proDescription);
                    phoneNo.setText(proPhoneNo);
                    category.setText(proCategory);
                    name.setText(proName);
                    name.setText(proStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void makePhoneCall() {
        String number = phoneNo.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(ProDetailActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProDetailActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(ProDetailActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}