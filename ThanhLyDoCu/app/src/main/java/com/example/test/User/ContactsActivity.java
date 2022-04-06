package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.HelperClasses.ContactsHelperClass;
import com.example.test.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView myContactsList;

    private DatabaseReference ContactsRef, UserRef, MessagesRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myContactsList = findViewById(R.id.chat_list_recyclerView);
        myContactsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);//set new post in top
        linearLayoutManager.setStackFromEnd(true);
        myContactsList.setLayoutManager(linearLayoutManager);

        DisplayAllContacts();
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

    private void DisplayAllContacts() {

        FirebaseRecyclerOptions<ContactsHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ContactsHelperClass>()
                        .setQuery(ContactsRef, ContactsHelperClass.class)
                        .build();

        FirebaseRecyclerAdapter<ContactsHelperClass, ContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<ContactsHelperClass, ContactsViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull ContactsViewHolder holder, int position, @NonNull ContactsHelperClass model) {

                        final String sellerID = getRef(position).getKey();

                        UserRef.child(sellerID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    final String fullName = snapshot.child("fullName").getValue().toString();
                                    final String profileImage = snapshot.child("profileImg").getValue().toString();
                                    final String type;
                                    final String lastDate = snapshot.child("userState").child("date").getValue().toString();
                                    final String lastTime = snapshot.child("userState").child("time").getValue().toString();

                                    if(snapshot.hasChild("userState"))
                                    {
                                        type = snapshot.child("userState").child("type").getValue().toString();

                                        if(type.equals("online"))
                                        {
                                            holder.onlineStatusView.setVisibility(View.VISIBLE);
                                            holder.myStatus.setText("online");
                                        }
                                        else
                                        {
                                            holder.onlineStatusView.setVisibility(View.INVISIBLE);
                                            holder.myStatus.setText("Truy cập lần cuối: " + lastTime + " " + lastDate);
                                        }
                                    }

                                    Picasso.get().load(profileImage).placeholder(R.drawable.profile_img).into(holder.myImage);
                                    holder.myName.setText(fullName);


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            CharSequence options[] = new CharSequence[]
                                                    {
                                                            "Thông tin cá nhân của " + fullName  ,
                                                            "Gửi tin nhắn"
                                                    };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                                            builder.setTitle("Lựa chọn");

                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if(i == 0)
                                                    {
                                                        Intent profileIntent = new Intent(ContactsActivity.this, ProFileCustomer.class);
                                                        profileIntent.putExtra("seller_id", sellerID);
                                                        startActivity(profileIntent);
                                                    }
                                                    if(i == 1)
                                                    {
                                                        Intent chatIntent = new Intent(ContactsActivity.this, ChatActivity.class);
                                                        chatIntent.putExtra("seller_id", sellerID);
                                                        chatIntent.putExtra("fullName", fullName);
                                                        startActivity(chatIntent);
                                                    }
                                                }
                                            });
                                            builder.show();
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
                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                        return viewHolder;
                    }
                };

        myContactsList.setAdapter(adapter);

        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView myImage;
        TextView myName, myStatus;
        ImageView onlineStatusView;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            myName = itemView.findViewById(R.id.all_users_profile_full_name);
            myImage = itemView.findViewById(R.id.all_users_profile_image);
            myStatus = itemView.findViewById(R.id.all_users_profile_status);
            onlineStatusView = itemView.findViewById(R.id.all_users_online_icon);
        }
    }


}