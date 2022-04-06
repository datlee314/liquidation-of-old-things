package com.example.test.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;

public class AllCategories extends AppCompatActivity {
TextView car,Thoitrang,dientu;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thoitrang=findViewById(R.id.ThoiTrang);
        car=findViewById(R.id.Catorixe);
        dientu=findViewById(R.id.Dientu);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( AllCategories.this,Categories.class);

                intent.putExtra("danhmuc",car.getText());
                startActivity(intent);
            }
        });
        Thoitrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( AllCategories.this,Categories.class);

                intent.putExtra("danhmucthoitrang",Thoitrang.getText());
                startActivity(intent);
            }
        });
        dientu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( AllCategories.this,Categories.class);

                intent.putExtra("danhmucdientu",dientu.getText());
                startActivity(intent);
            }
        });
        backBtn = findViewById(R.id.back_pressed);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllCategories.super.onBackPressed();
            }
        });
    }
}