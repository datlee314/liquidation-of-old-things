package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.HelperClasses.Hinhanh;
import com.example.test.HelperClasses.Recyclerview_Search;
import com.example.test.HelperClasses.Recyclerview_Search3;
import com.example.test.HelperClasses.sanphamAdapter;
import com.example.test.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainQLSP extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;
    DatabaseReference mRef;
    GridView gridView;
    TextView txt;
    private static String id;
    DatabaseReference Mdata;
    Recyclerview_Search3 adapter;
    ArrayList<Hinhanh> listHinhAnh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qlsp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        recyclerView = findViewById(R.id.RCVSQCB);
        txt=findViewById(R.id.txtdanhmuc);

load();

        DatafromFirebase();


            }

public void load()
{

    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
    mRef = FirebaseDatabase.getInstance().getReference().child("Users");

    mRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
            {
                String key= snapshot.getKey();
                String address = snapshot.child("address").getValue(String.class);
                String fullName = snapshot.child("fullName").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);
                String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                String profileImg = snapshot.child("profileImg").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                String date = snapshot.child("date").getValue(String.class);

                id = key;

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainQLSP.this, "Xin lỗi ! Có gì đó lỗi rồi", Toast.LENGTH_SHORT).show();
        }
    });
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
                    String Id= ds.child("idcustomer").getValue(String.class);
                    String tinhtrang = ds.child("tinhtrang").getValue(String.class);
                    String khu = ds .child("khuvuc").getValue(String.class);
                    if(Id.equals(id)) {
                        Hinhanh ha = new Hinhanh(key,date, khu, tinhtrang, tendm, key, ten, gia, noidung, hinh, sdt);
                        listHinhAnh.add(ha);
                    }
                }

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainQLSP.this);
                adapter = new Recyclerview_Search3(MainQLSP.this, listHinhAnh);
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


