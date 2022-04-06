package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.HelperClasses.Recyclerview_user;
import com.example.test.HelperClasses.UserHelperClass;
import com.example.test.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetail extends AppCompatActivity {
    ImageView imageView,imgCall,sms;
    private static final int REQUEST_CALL = 1;
    TextView ten,gia,noidung,sdt,tinhtrang,khuvuc,danhmuc,date;
    Toolbar toolbar;
    RecyclerView recyclerView;
    DatabaseReference Mdata;
    Recyclerview_user adapter;
    ArrayList<UserHelperClass> listHinhAnh;
ArrayList<UserHelperClass> listuser= new ArrayList<>();
    CircleImageView img;
    FirebaseAuth mAuth;
    String currentUserID, productID, sellerID;

    DatabaseReference mRef, ProductRef;
    String id;
    FloatingActionButton backBtn;
    ImageButton sendMessageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        sellerID = getIntent().getStringExtra("idcustomer");

        ProductRef = FirebaseDatabase.getInstance().getReference().child("sanpham");
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
imgCall=findViewById(R.id.product_detail_phone_btn);
        imageView= findViewById(R.id.products_detail_img);
        ten=findViewById(R.id.tens);
        recyclerView=findViewById(R.id.rcw_user);
        sdt=findViewById(R.id.product_detail_phoneNo);
        img=findViewById(R.id.product_detail_profile_img);
        date=findViewById(R.id.product_detail_time);
            sms=findViewById(R.id.product_detail_mail_btn);
        gia=findViewById(R.id.gias);
        // mail=findViewById(R.id.fab);
        tinhtrang=findViewById(R.id.product_detail_status);
        noidung=findViewById(R.id.product_detail_description);
        khuvuc = findViewById(R.id.product_detail_local);
        danhmuc=findViewById(R.id.product_detail_category);

       loadData();
//DatafromFirebase();

        sendMessageButton=findViewById(R.id.product_detail_send_message_button);

       sms.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent( ProductDetail.this,MainActivitySMS.class);

             intent.putExtra("sdt",sdt.getText());
               intent.putExtra("ten",ten.getText());
               startActivity(intent);
           }
       });
imgCall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       makePhoneCall();
    }
});
        // toolbar
        toolbar =  findViewById(R.id.tool_bar);
        backBtn =  findViewById(R.id.product_detail_back_pressed);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //OnClick
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetail.super.onBackPressed();
            }
        });




    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    private void makePhoneCall() {
        String number = sdt.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(ProductDetail.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProductDetail.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(ProductDetail.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
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
    private void DatafromFirebase(){
        listHinhAnh = new ArrayList<>();
        Mdata= FirebaseDatabase.getInstance().getReference().child("Users");
        Mdata.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //  Log.d("abc", "onDataChange: vao day");

                    String key = ds.getKey();
                    String address = ds.child("address").getValue(String.class);
                    String fullName = ds.child("fullName").getValue(String.class);
                    String gender = ds.child("gender").getValue(String.class);
                    String phoneNo = ds.child("phoneNo").getValue(String.class);
                    String profileImg = ds.child("profileImg").getValue(String.class);
                    String username = ds.child("username").getValue(String.class);
                    String date =ds.child("date").getValue(String.class);

                    if(phoneNo.equals( sdt.getText().toString())){
                        UserHelperClass ha = new UserHelperClass(fullName, address, username, profileImg, date, gender, phoneNo);
                        listHinhAnh.add(ha);
                    }


                }

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDetail.this);
                adapter = new Recyclerview_user(ProductDetail.this, listHinhAnh);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadData(){
        Intent intent = getIntent();
        ten.setText(intent.getStringExtra("ten"));
       gia.setText(intent.getStringExtra(("gia")));
      noidung.setText(intent.getStringExtra("noidung"));
        tinhtrang.setText(intent.getStringExtra("tinhtrang"));
       sdt.setText(intent.getStringExtra("sdt"));
      danhmuc.setText(intent.getStringExtra("danhmuc"));
        khuvuc.setText(intent.getStringExtra("khuvuc"));
        date.setText(intent.getStringExtra("date"));
       String url = intent.getStringExtra("hinh");
        Picasso.get().load(url).into(imageView);
    }
}