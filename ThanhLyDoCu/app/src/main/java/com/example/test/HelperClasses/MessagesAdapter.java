package com.example.test.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.User.ProDetailActivity;
import com.example.test.User.UserDashBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private List<MessagesHelperClass> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef, productRef;

    private Context mContext;

    public MessagesAdapter (List<MessagesHelperClass> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }



    public class MessagesViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, senderProductTitle, senderProductPrice, receiverMessageText, receiverProductTitle, receiverProductPrice;
        public CircleImageView receiverProfileImage;
        public ImageView senderProductImg, receiverProductImg;
        public RelativeLayout senderProductLayout, receiverProductLayout;


        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);


            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProductImg = itemView.findViewById(R.id.received_inbox_pro_img);
            receiverProductTitle = itemView.findViewById(R.id.received_inbox_pro_title);
            receiverProductPrice = itemView.findViewById(R.id.received_inbox_pro_price);
            receiverProductLayout = itemView.findViewById(R.id.message_received_inbox_product);

            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);

            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            senderProductTitle = itemView.findViewById(R.id.inbox_pro_title);
            senderProductPrice = itemView.findViewById(R.id.inbox_pro_title);
            senderProductImg = itemView.findViewById(R.id.inbox_pro_img);
            senderProductLayout = itemView.findViewById(R.id.message_inbox_product);

        }
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout_of_users, parent, false);

        mAuth = FirebaseAuth.getInstance();
        mContext = parent.getContext();

        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {

        String messageSenderID = mAuth.getCurrentUser().getUid();
        MessagesHelperClass messagesHelperClass = userMessagesList.get(position);

        String fromUserID = messagesHelperClass.getFrom();
        String fromMessageType = messagesHelperClass.getType();
        String fromProductID = messagesHelperClass.getProductID();
        String fromSellerID = messagesHelperClass.getSellerID();


        //OnClick
        holder.receiverProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(mContext, ProDetailActivity.class);
                productIntent.putExtra("product_id", fromProductID);
                productIntent.putExtra("seller_id", fromSellerID);
                mContext.startActivity(productIntent);
            }
        });

        holder.senderProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(mContext, ProDetailActivity.class);
                productIntent.putExtra("product_id", fromProductID);
                productIntent.putExtra("seller_id", fromSellerID);
                mContext.startActivity(productIntent);
            }
        });


        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String image = snapshot.child("profileImg").getValue().toString();

                    Picasso.get().load(image).placeholder(R.drawable.profile_img).into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(fromMessageType.equals("text"))
        {

            holder.senderProductLayout.removeAllViewsInLayout();
            holder.receiverProductLayout.removeAllViewsInLayout();

            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverProfileImage.setVisibility(View.INVISIBLE);

            if(fromUserID.equals(messageSenderID))
            {

                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.senderMessageText.setTextColor(Color.WHITE);
                holder.senderMessageText.setGravity(Gravity.LEFT);
                holder.senderMessageText.setText(messagesHelperClass.getMessage());
            }
            else
            {

                holder.senderMessageText.setVisibility(View.INVISIBLE);

                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
                holder.receiverMessageText.setTextColor(Color.WHITE);
                holder.receiverMessageText.setGravity(Gravity.LEFT);
                holder.receiverMessageText.setText(messagesHelperClass.getMessage());
            }
        }if(fromMessageType.equals("Inbox"))
        {

            productRef = FirebaseDatabase.getInstance().getReference().child("sanpham").child(fromProductID);
            productRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String proImg = snapshot.child("link").getValue().toString();
                        String proName = snapshot.child("tenhinh").getValue().toString();
                        String proPrice = snapshot.child("gia").getValue().toString();

                        Picasso.get().load(proImg).placeholder(R.drawable.auction_img_1).into(holder.senderProductImg);
                        holder.senderProductTitle.setText("Tên: " + proName);
                        holder.senderProductPrice.setText("Giá" + proPrice + "K");

                        Picasso.get().load(proImg).placeholder(R.drawable.auction_img_1).into(holder.receiverProductImg);
                        holder.receiverProductTitle.setText("Tên: " + proName);
                        holder.receiverProductPrice.setText("Giá: " + proPrice + "K");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.senderProductImg.setVisibility(View.INVISIBLE);
            holder.senderProductPrice.setVisibility(View.INVISIBLE);
            holder.senderProductTitle.setVisibility(View.INVISIBLE);
            holder.senderProductLayout.setVisibility(View.INVISIBLE);
            holder.senderProductLayout.setBackgroundResource(R.drawable.sender_message_text_background);

            holder.receiverProductImg.setVisibility(View.INVISIBLE);
            holder.receiverProductPrice.setVisibility(View.INVISIBLE);
            holder.receiverProductTitle.setVisibility(View.INVISIBLE);
            holder.receiverProductLayout.setVisibility(View.INVISIBLE);
            holder.receiverProductLayout.setBackgroundResource(R.drawable.receiver_message_text_background);

            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverProfileImage.setVisibility(View.INVISIBLE);

            if(fromUserID.equals(messageSenderID))
            {
                holder.receiverProductLayout.removeAllViewsInLayout();

                holder.senderProductImg.setVisibility(View.VISIBLE);
                holder.senderProductPrice.setVisibility(View.VISIBLE);
                holder.senderProductTitle.setVisibility(View.VISIBLE);
                holder.senderProductLayout.setVisibility(View.VISIBLE);

                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.senderMessageText.setTextColor(Color.WHITE);
                holder.senderMessageText.setGravity(Gravity.LEFT);
                holder.senderMessageText.setText(messagesHelperClass.getMessage());
            }
            else
            {
                holder.senderProductLayout.removeAllViewsInLayout();

                holder.receiverProductImg.setVisibility(View.VISIBLE);
                holder.receiverProductPrice.setVisibility(View.VISIBLE);
                holder.receiverProductTitle.setVisibility(View.VISIBLE);
                holder.receiverProductLayout.setVisibility(View.VISIBLE);

                holder.senderMessageText.setVisibility(View.INVISIBLE);

                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
                holder.receiverMessageText.setTextColor(Color.WHITE);
                holder.receiverMessageText.setGravity(Gravity.LEFT);
                holder.receiverMessageText.setText(messagesHelperClass.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {

        return userMessagesList.size();
    }
}
