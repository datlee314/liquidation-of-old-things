package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.HelperClasses.FavoritesProductsHelperClass;
import com.example.test.HelperClasses.SurroundingsProductsHelperClass;
import com.example.test.LoginAndRegister.LoginActivity;
import com.example.test.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFavoriteProduct extends AppCompatActivity {

    ImageView backBtn;
    EditText pFullName, pUsername;
    CircleImageView pImage;
    TextView displayNoOfFavorites;

    RecyclerView favoritesList;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef, FavoritesRef;
    private String currentUserID;

    int countFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorite_product);

        //Hooks
        backBtn = findViewById(R.id.back_pressed);

        pFullName = findViewById(R.id.profile_full_name);
        pUsername = findViewById(R.id.profile_user_name);
        pImage = findViewById(R.id.profile_img);
        displayNoOfFavorites = findViewById(R.id.quanity_pro_cart);

        favoritesList = findViewById(R.id.user_favorite_product);

        //Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        FavoritesRef = FirebaseDatabase.getInstance().getReference().child("Favorites").child(currentUserID);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFavoriteProduct.super.onBackPressed();
            }
        });

        DisplayUserProfile();

        DisplayCountMyFavorites();

    }



    private void DisplayUserProfile() {
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

                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String profileImg = snapshot.child("profileImg").getValue(String.class);
                        String username = snapshot.child("username").getValue(String.class);

                        pFullName.setText(fullName);
                        Picasso.get().load(profileImg).into(pImage);
                        pUsername.setText(username);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserFavoriteProduct.this, "Xin lỗi ! Có gì đó lỗi rồi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        favoritesList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        favoritesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Query SortPostsInDescendingOrder = FavoritesRef.orderByChild("counter");

        FirebaseRecyclerOptions<FavoritesProductsHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FavoritesProductsHelperClass>()
                        .setQuery(SortPostsInDescendingOrder, FavoritesProductsHelperClass.class)
                        .build();

        FirebaseRecyclerAdapter<FavoritesProductsHelperClass, favoritesProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<FavoritesProductsHelperClass, favoritesProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull favoritesProductsViewHolder holder, int position, @NonNull FavoritesProductsHelperClass model) {
                        holder.title.setText(model.getName());
                        holder.price.setText("Giá: " + model.getPrice() + "K");
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.auction_img_1).into(holder.image);

                        final String product_id = getRef(position).getKey();

                        FavoritesRef.child(product_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists())
                                {
                                    String sellerID = snapshot.child("sellerID").getValue().toString();

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent productIntent = new Intent(UserFavoriteProduct.this, ProDetailActivity.class);
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
                    public favoritesProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_favorites_products_layout, parent, false);
                        favoritesProductsViewHolder viewHolder = new favoritesProductsViewHolder(view);
                        return viewHolder;
                    }
                };

        favoritesList.setAdapter(adapter);

        adapter.startListening();
    }

    public static class favoritesProductsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, price;

        public favoritesProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.pro_img);
            title = itemView.findViewById(R.id.pro_title);
            price = itemView.findViewById(R.id.pro_price);

        }
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

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(UserFavoriteProduct.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}