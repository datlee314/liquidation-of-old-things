package com.example.test.HelperClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.User.ProFileCustomer;
import com.example.test.User.ProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recyclerview_user extends RecyclerView.Adapter<Recyclerview_user.ViewHolder> {
    @NonNull
    private Context mcontext;
    ArrayList<UserHelperClass> list;
    ArrayList<UserHelperClass> filterList;
ArrayList<UserHelperClass>listuser;
    TiengViet xuLyChuoiTiengViet = new TiengViet();
    public Recyclerview_user(Context mcontext, ArrayList<UserHelperClass> list) {
        this.mcontext = mcontext;
        this.filterList = list;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String url = filterList.get(position).getProfileImg();
        holder.name.setText(filterList.get(position).getFullName());
        Picasso.get().load(url).into(holder.imvHSP);



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, ProFileCustomer.class);
                intent.putExtra("img",filterList.get(position).getProfileImg());
               intent.putExtra("fullname",filterList.get(position).getFullName());
                intent.putExtra("name",filterList.get(position).getUsername());
                intent.putExtra("sdt",filterList.get(position).getPhoneNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Object coordinatorlayout;
        CircleImageView imvHSP;
        TextView name;
        CoordinatorLayout t;

        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvHSP = itemView.findViewById(R.id.product_detail_profile_img);
name =itemView.findViewById(R.id.product_detail_full_name);
            relativeLayout = itemView.findViewById(R.id.relative_item_user);
        }
    }
    private Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            /// khởi tạo biến result
            FilterResults filterResults= new FilterResults();
            String keySearch = constraint.toString();
            /// khi keysearch co gia tri
            if (keySearch != null && keySearch.toString().length() > 0) {
                /// thì mình khởi tạo list trống để add dữ liệu sao khi check vào
                ArrayList<UserHelperClass> filteredList = new ArrayList<>();
                String pattrn= keySearch.toLowerCase().trim();
                for(UserHelperClass item : list){
                    /// check dử liệu để add
                    if (xuLyChuoiTiengViet.ConvertString(item.getFullName().toLowerCase())
                            .contains(xuLyChuoiTiengViet.ConvertString(pattrn))) {
                        filteredList.add(item);
                    }
                }
                /// gán vào giá trị sao khi check xong
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }
            else{
                /// gán lại giá trị all
                filterResults.values = list;
                filterResults.count = list.size();
//                synchronized (list) {
//                    filterResults.values = list;
//                    filterResults.count = list.size();
//                }
            }
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (ArrayList<UserHelperClass>) results.values;
            notifyDataSetChanged();
        }
    };
}
