package com.angat.askmeanything.utils.adapter;

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
import com.angat.askmeanything.feature.postupload.post.PostsItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<PostsItem> postsItems;

    public PostAdapter(Context context, List<PostsItem> postsItems) {
        this.context = context;
        this.postsItems = postsItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostsItem postsItem = postsItems.get(position);
        holder.personName.setText(postsItem.getName());
        holder.datePosted.setText(postsItem.getStatusTime());

        if (postsItem.getPrivacy().equals("0")){
            holder.privacyIcon.setImageResource(R.drawable.ic_privacy_friends);
        }
        else if (postsItem.getPrivacy().equals("1")){
            holder.privacyIcon.setImageResource(R.drawable.ic_privacy_me);
        }
        else{
            holder.privacyIcon.setImageResource(R.drawable.ic_privacy_public);

        }

        String profileImage = postsItem.getProfileUrl();

        if (!postsItem.getProfileUrl().isEmpty()){
            if (Uri.parse(postsItem.getProfileUrl()).getAuthority()==null){
                profileImage=ApiClient.BASE_URL+postsItem.getProfileUrl();
            }
            Glide.with(context).load(profileImage).placeholder(R.drawable.default_profile_placeholder).into(holder.personImage);
        }
        if (!postsItem.getStatusImage().isEmpty()){
            holder.statusImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(ApiClient.BASE_URL+postsItem.getStatusImage()).placeholder(R.drawable.default_profile_placeholder).into(holder.statusImage);
        }
        else{
            holder.statusImage.setVisibility(View.GONE);

        }
        if (postsItem.getPost().isEmpty()){
            holder.post.setVisibility(View.GONE);
        }
        else{
            holder.post.setVisibility(View.VISIBLE);
            holder.post.setText(postsItem.getPost());
        }

    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView personImage,privacyIcon, statusImage;
        TextView personName,datePosted, post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personImage = itemView.findViewById(R.id.person_image);
            personName = itemView.findViewById(R.id.person_name);
            privacyIcon = itemView.findViewById(R.id.privacy_icon);
            statusImage = itemView.findViewById(R.id.post_image);
            datePosted = itemView.findViewById(R.id.date_posted);
            post = itemView.findViewById(R.id.post_main);



        }
    }
}
