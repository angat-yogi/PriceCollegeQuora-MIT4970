package com.angat.askmeanything.feature.search;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angat.askmeanything.R;
import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.model.search.User;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    List<User> userList;

    public SearchAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        String userImage = "";

        Uri userUri = Uri.parse(user.getProfileUrl());
        if (userUri.getAuthority()==null){
            userImage= ApiClient.BASE_URL+user.getProfileUrl();
        }
        else{
            userImage=user.getProfileUrl();
        }

        if (!userImage.isEmpty()){
            Glide.with(context).load(userImage).placeholder(R.drawable.default_profile_placeholder).into(holder.profileImage);
        }

        holder.profileName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileImage;
        TextView profileName;
        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.user_image);
            profileName = itemView.findViewById(R.id.user_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}