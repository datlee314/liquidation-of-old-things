package com.example.test.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.squareup.picasso.Picasso;

public class MainActivityProdectDetail extends AppCompatActivity {
    ImageView imageView;
    TextView ten,gia,noidung,sdt,tinhtrang,khuvuc,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prodect_detail);
        imageView= findViewById(R.id.chitiet);
        ten=findViewById(R.id.chiietten);
        sdt=findViewById(R.id.chitietsdt);
       gia=findViewById(R.id.chitietgia);
      // mail=findViewById(R.id.fab);
        tinhtrang=findViewById(R.id.chitiettinhtrang);
        noidung=findViewById(R.id.chitietnoidung);
        khuvuc = findViewById(R.id.chitietkhuvuc);
        loadData();
    }
    private void loadData(){
        Intent intent = getIntent();
        ten.setText("Tên SP:"+" "+intent.getStringExtra("ten"));
        gia.setText(intent.getStringExtra(("gia"))+" "+"VNĐ");
        noidung.setText(intent.getStringExtra("noidung"));
        tinhtrang.setText("Tình Trang:"+" "+intent.getStringExtra("tinhtrang"));
        sdt.setText(intent.getStringExtra("sdt"));
        khuvuc.setText("Khu Vực:"+" "+intent.getStringExtra("khuvuc"));
        String url = intent.getStringExtra("hinh");
        Picasso.get().load(url).into(imageView);
    }
}