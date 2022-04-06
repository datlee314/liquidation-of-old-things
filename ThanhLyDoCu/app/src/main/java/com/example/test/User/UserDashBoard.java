package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.HelperClasses.AuctionAdapter;
import com.example.test.HelperClasses.AuctionHelperClass;
import com.example.test.HelperClasses.CategoriesAdapter;
import com.example.test.HelperClasses.CategoriesHelperClass;
import com.example.test.HelperClasses.Hinhanh;
import com.example.test.HelperClasses.Recyclerview_Search1;
import com.example.test.HelperClasses.SurroundingsProductsHelperClass;
import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    TextView timkiem;
    static final float END_SCALE = 0.7f;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference Mdata;
    ArrayList<Hinhanh> listHinhAnh;
    RecyclerView.LayoutManager layoutManager;
    GridView lvhinhanh;
    Recyclerview_Search1 adapter1;
    RecyclerView auctionRecycler, surroundingsRecycler, categoriesRecycler;
    RecyclerView.Adapter adapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    ImageView menuIcon;
    LinearLayout contentView;

    TextView seeAllCategories,QLSP;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private FirebaseAuth mAuth;
    private DatabaseReference ProductRef, UserRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
DatafromFirebase();

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("sanpham");
        currentUserID = mAuth.getCurrentUser().getUid();

        //Hooks
        auctionRecycler = findViewById(R.id.auction_recycler);
        surroundingsRecycler = findViewById(R.id.sur_pro_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
        menuIcon = findViewById(R.id.menu_icon);
        timkiem=findViewById(R.id.timkiem);
        contentView = findViewById(R.id.content);
QLSP=findViewById(R.id.qlsp);
QLSP.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MainQLSP.class);
        startActivity(intent);
    }
});
        seeAllCategories = findViewById(R.id.see_all_categories);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();
DatafromFirebase();
        //Functions will be executed automatically when this activity will be created
       // auctionRecycler();
        surroundingsRecycler();
        categoriesRecycler();
       timkiem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), Mainseach.class);
               startActivity(intent);
           }
       });
        //Onclick
        seeAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
            }
        });

    }

    //Navigation Drawer Functions
    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_all_categories:
                startActivity(new Intent(getApplicationContext(), AllCategories.class));
                break;

            case R.id.nav_messages:
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                break;
            case R.id.nav_liquidation:
                startActivity(new Intent(getApplicationContext(), MainPost.class));
                break;
            case R.id.SPcuaban:
                startActivity(new Intent(getApplicationContext(), MainQLSP.class));
                break;

            case R.id.nav_logout:
                updateUserStatus("offline");
                logout();
                break;
        }

        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginActivity);
        finish();
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

        UserRef.child(currentUserID).child("userState")
                .updateChildren(currentStateMap);
    }


    //Recycler views Functions
    private void auctionRecycler() {

        auctionRecycler.setHasFixedSize(true);
        auctionRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<AuctionHelperClass> auction = new ArrayList<>();

        auction.add(new AuctionHelperClass(R.drawable.auction_img_1, "Đấu giá Iphone 13 Pro Max xanh dương", "59:99", "10,000đ"));
        auction.add(new AuctionHelperClass(R.drawable.auction_img_2, "Đấu giá đèn thần của Aladin", "9:99", "99,999đ "));
        auction.add(new AuctionHelperClass(R.drawable.auction_img_3, "Đấu giá Xe tăng thể thao bao đẹp", "19:99", "199,999đ"));

        adapter = new AuctionAdapter(auction);
        auctionRecycler.setAdapter(adapter);


    }

    private void surroundingsRecycler() {

        surroundingsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        surroundingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<SurroundingsProductsHelperClass> options =
                new FirebaseRecyclerOptions.Builder<SurroundingsProductsHelperClass>()
                        .setQuery(ProductRef, SurroundingsProductsHelperClass.class)
                        .build();

        FirebaseRecyclerAdapter<SurroundingsProductsHelperClass, SurroundingsViewHolder> adapter =
                new FirebaseRecyclerAdapter<SurroundingsProductsHelperClass, SurroundingsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SurroundingsViewHolder holder, int position, @NonNull SurroundingsProductsHelperClass model) {
                        holder.title.setText(model.getTenhinh());
                        holder.price.setText(model.getGia() + "K");
                        holder.local.setText(model.getKhuvuc());
                        Picasso.get().load(model.getLink()).placeholder(R.drawable.auction_img_1).into(holder.image);

                        final String product_id = getRef(position).getKey();

                        ProductRef.child(product_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists())
                                {
                                    String sellerID = snapshot.child("idcustomer").getValue().toString();

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent productIntent = new Intent(UserDashBoard.this, ProDetailActivity.class);
                                            productIntent.putExtra("product_id", product_id);
                                            productIntent.putExtra("seller_id", sellerID);
                                            startActivity(productIntent);
                                        }
                                    });
                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                    @NonNull
                    @Override
                    public SurroundingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.surroundings_products_card_design, parent, false);
                        SurroundingsViewHolder viewHolder = new SurroundingsViewHolder(view);
                        return viewHolder;
                    }
                };

        surroundingsRecycler.setAdapter(adapter);

        adapter.startListening();

        updateUserStatus("online");
    }

    public static class SurroundingsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, price, local;

        public SurroundingsViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.sur_pro_img);
            title = itemView.findViewById(R.id.sur_pro_title);
            price = itemView.findViewById(R.id.sur_pro_price);
            local = itemView.findViewById(R.id.sur_pro_local);
        }
    }


    private void categoriesRecycler() {

        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.technology_img, "Đồ công nghệ", gradient1));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.categories_furniture_img, "Nội thất", gradient2));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.categories_clothes_img, "Quần áo", gradient3));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.categories_car_icon, "Xe", gradient4));


        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
       categoriesRecycler.setAdapter(adapter);

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
                    String tendm = ds.child("tendm").getValue(String.class);
                    String ten = ds.child("tenhinh").getValue(String.class);
                    String gia = ds.child("gia").getValue(String.class);
                    String hinh = ds.child("link").getValue(String.class);
                    String noidung = ds.child("noidung").getValue(String.class);
                    String sdt = ds.child("sdt").getValue(String.class);
                    String tinhtrang = ds.child("tinhtrang").getValue(String.class);
                    String khu = ds .child("khuvuc").getValue(String.class);
                    String date = ds .child("date").getValue(String.class);
                    Hinhanh ha = new Hinhanh("",date,khu,tinhtrang,tendm,key,ten,gia,noidung,hinh,sdt);
                    listHinhAnh.add(ha);
                }

                auctionRecycler.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserDashBoard.this);
                adapter1 = new Recyclerview_Search1(UserDashBoard.this, listHinhAnh);
                auctionRecycler.setLayoutManager(layoutManager);
                auctionRecycler.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}