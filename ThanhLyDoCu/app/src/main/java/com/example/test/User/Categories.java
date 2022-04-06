package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.test.HelperClasses.Hinhanh;
import com.example.test.HelperClasses.Recyclerview_Search;
import com.example.test.HelperClasses.Recyclerview_Search1;
import com.example.test.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    RecyclerView gridView;
    DatabaseReference Mdata;
    Button sapxesp;
    EditText text;
    TextView testdanhmuc;
    ArrayList<Hinhanh> listHinhAnh = new ArrayList<>();
    Recyclerview_Search adapter;
    String tendm,dmThoitrang,dientu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        gridView = findViewById(R.id.grxeco);
        testdanhmuc = findViewById(R.id.txtdanhmuc);
        loadData();
        DatafromFirebase();
    }
    private void DatafromFirebase(){
        listHinhAnh = new ArrayList<>();
        Mdata= FirebaseDatabase.getInstance().getReference().child("sanpham");
        Mdata.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //  Log.d("abc", "onDataChange: vao day");
                    String key = ds.getKey();
                    String tendmuc = ds.child("tendm").getValue(String.class);
                    String ten = ds.child("tenhinh").getValue(String.class);
                    String gia = ds.child("gia").getValue(String.class);
                    String hinh = ds.child("link").getValue(String.class);
                    String noidung = ds.child("noidung").getValue(String.class);
                    String sdt = ds.child("sdt").getValue(String.class);
                    String tinhtrang = ds.child("tinhtrang").getValue(String.class);
                    String khu = ds .child("khuvuc").getValue(String.class);
                    if( tendmuc.equals(dientu)) {
                        Hinhanh ha = new Hinhanh("","",khu, tinhtrang, tendm, key, ten, gia, noidung, hinh, sdt);
                        listHinhAnh.add(ha);
                    }else
                    if( tendmuc.equals(tendm)) {
                        Hinhanh ha = new Hinhanh("","",khu, tinhtrang, tendm, key, ten, gia, noidung, hinh, sdt);
                        listHinhAnh.add(ha);
                    }else
                    if( tendmuc.equals(dmThoitrang)) {
                        Hinhanh ha = new Hinhanh("","",khu, tinhtrang, tendm, key, ten, gia, noidung, hinh, sdt);
                        listHinhAnh.add(ha);
                    }
                }

                gridView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Categories.this);
                adapter = new Recyclerview_Search(Categories.this, listHinhAnh);
                gridView.setLayoutManager(layoutManager);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadData(){
        Intent intent = getIntent();
        tendm = intent.getStringExtra("danhmuc");
       dmThoitrang=intent.getStringExtra("danhmucthoitrang");
        dientu=intent.getStringExtra("danhmucdientu");
    }
}