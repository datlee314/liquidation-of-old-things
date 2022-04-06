package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.test.HelperClasses.Hinhanh;
import com.example.test.HelperClasses.Recyclerview_Search;
import com.example.test.HelperClasses.Recyclerview_Search3;
import com.example.test.HelperClasses.TiengViet;
import com.example.test.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProFileCustomer extends AppCompatActivity {
    CircleImageView img;
    TextView fullname,phone;
    RecyclerView recyclerView;
    DatabaseReference Mdata;
    private static String id;
    Recyclerview_Search adapter;
    ArrayList<Hinhanh> listHinhAnh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_file_customer);
        img=findViewById(R.id.profile_imgcutomer);

        fullname=findViewById(R.id.profile1);
        phone=findViewById(R.id.profil2);
        recyclerView=findViewById(R.id.rcw_cutomer);
        loadata();
       DatafromFirebase();
    }
    public  void loadata()
    {
        Intent intent  = getIntent();
        String url = intent.getStringExtra("Profileimg");
        Picasso.get().load(url).into(img);
   fullname.setText(intent.getStringExtra("name"));
     phone.setText(intent.getStringExtra("Phone"));
     id=intent.getStringExtra("id");


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
                    String date = ds.child("date").getValue(String.class);
                    String tendm = ds.child("tendm").getValue(String.class);
                    String ten = ds.child("tenhinh").getValue(String.class);
                    String gia = ds.child("gia").getValue(String.class);
                    String hinh = ds.child("link").getValue(String.class);
                    String noidung = ds.child("noidung").getValue(String.class);
                    String sdt = ds.child("sdt").getValue(String.class);
                    String ID= ds.child("idcustomer").getValue(String.class);
                    String tinhtrang = ds.child("tinhtrang").getValue(String.class);
                    String khu = ds .child("khuvuc").getValue(String.class);
                    if(ID.equals(id)) {
                        Hinhanh ha = new Hinhanh(ID,date, khu, tinhtrang, tendm, key, ten, gia, noidung, hinh, sdt);
                        listHinhAnh.add(ha);
                    }
                }

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProFileCustomer.this);
                adapter = new Recyclerview_Search(ProFileCustomer.this, listHinhAnh);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}