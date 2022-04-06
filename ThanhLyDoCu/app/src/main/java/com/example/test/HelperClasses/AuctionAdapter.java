package com.example.test.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;

import java.util.ArrayList;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {

    ArrayList<AuctionHelperClass> auction;

    public AuctionAdapter(ArrayList<AuctionHelperClass> auction) {
        this.auction = auction;
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_card_design, parent, false);
        AuctionViewHolder auctionViewHolder = new AuctionViewHolder(view);
        return auctionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionViewHolder holder, int position) {

        AuctionHelperClass auctionHelperClass = auction.get(position);

        holder.image.setImageResource(auctionHelperClass.getImage());
        holder.title.setText(auctionHelperClass.getTitle());
        holder.time.setText(auctionHelperClass.getTime());
        holder.price.setText(auctionHelperClass.getPrice());

    }

    @Override
    public int getItemCount() {
        return auction.size();
    }

    public static class AuctionViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, time, price;

        public AuctionViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.auction_img);
            title = itemView.findViewById(R.id.auction_title);
            time = itemView.findViewById(R.id.auction_time);
            price = itemView.findViewById(R.id.auction_price);
        }
    }
}
