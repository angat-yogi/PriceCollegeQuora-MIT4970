package com.angat.askmeanything.utils.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.angat.askmeanything.R;
import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.feature.comment.PostCommentReplyBottomSheet;
import com.angat.askmeanything.model.post.PostsItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<PostsItem> postsItems;

    public PostAdapter(Context context, List<PostsItem> postsItems) {
        this.context = context;
        this.postsItems = postsItems;
    }
    public void increasePostCommentCount(int adapterPosition){
        PostsItem postsItem = postsItems.get(adapterPosition);
        postsItem.setCommentCount(postsItem.getCommentCount()+1);
        notifyItemChanged(adapterPosition,postsItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

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
        holder.commentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCommentReplyBottomSheet bottomSheet = new PostCommentReplyBottomSheet();

                Bundle bundle = new Bundle();
                bundle.putString("postId",postsItem.getPostId());
                bundle.putString("postUserId",postsItem.getPostUserId());
                bundle.putString("commentOn","post");
                bundle.putString("commentUserId","-1");
                bundle.putString("parentId","-1");
                bundle.putInt("commentCounter",postsItem.getCommentCount());
                bundle.putBoolean("shouldOpenKeyboard",false);
                bundle.putInt("postAdapterPosition",position);

                bottomSheet.setArguments(bundle);

                FragmentActivity fragmentActivity = (FragmentActivity) context;
                bottomSheet.show(fragmentActivity.getSupportFragmentManager(),"CommentFragment");
            }
        });
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

        if (postsItem.getCommentCount()==0||postsItem.getCommentCount()==1){
            holder.commentCount.setText(postsItem.getCommentCount() +" comment");
        }
        else{
            holder.commentCount.setText(postsItem.getCommentCount() +" comments");

        }

    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView personImage,privacyIcon, statusImage;
        TextView personName,datePosted, post, commentCount;
        LinearLayout commentSection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personImage = itemView.findViewById(R.id.person_image);
            personName = itemView.findViewById(R.id.person_name);
            privacyIcon = itemView.findViewById(R.id.privacy_icon);
            statusImage = itemView.findViewById(R.id.post_image);
            datePosted = itemView.findViewById(R.id.date_posted);
            post = itemView.findViewById(R.id.post_user);
            commentSection = itemView.findViewById(R.id.commentSection);
            commentCount=itemView.findViewById(R.id.comment_txt);



        }
    }
}
