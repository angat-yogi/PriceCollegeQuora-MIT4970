package com.angat.askmeanything.feature.comment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angat.askmeanything.R;
import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.model.comment.CommentsItem;
import com.angat.askmeanything.model.post.PostsItem;
import com.angat.askmeanything.utils.adapter.PostAdapter;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<CommentsItem> commentsItems;

    public CommentAdapter(Context context, List<CommentsItem> commentsItems) {
        this.context = context;
        this.commentsItems = commentsItems;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        CommentsItem commentsItem = commentsItems.get(position);

        holder.commentPerson.setText(commentsItem.getName());
        holder.commentBody.setText(commentsItem.getComment());

        String profilePicture = commentsItem.getProfileUrl();
        if (Uri.parse(commentsItem.getProfileUrl()).getAuthority()==null){
            profilePicture= ApiClient.BASE_URL+profilePicture;
        }
        Glide.with(context).load(profilePicture).into(holder.commentProfile);

        //comment reply data pending
        int totalCommentReplies = commentsItem.getComments().size();

        if (totalCommentReplies>=1){
            holder.sub_comment_section.setVisibility(View.VISIBLE);
            if (totalCommentReplies>1){
                holder.sub_comment_section.setVisibility(View.VISIBLE);
            }
            else   {
                holder.sub_comment_section.setVisibility(View.GONE);
            }

            if (totalCommentReplies==2){
                holder.moreComments.setText("view 1 more comment");
            }
            else{
                holder.moreComments.setText("view "+totalCommentReplies+" more comments");
            }
            if (commentsItem.getComments().size()>=1){
                CommentsItem latestComment = commentsItem.getComments().get(0);
                holder.subCommentBody.setText(latestComment.getComment());
                String subProfilePicture = latestComment.getProfileUrl();

                if (Uri.parse(subProfilePicture).getAuthority()==null){
                    subProfilePicture= ApiClient.BASE_URL+subProfilePicture;
                }
                Glide.with(context).load(subProfilePicture).into(holder.subCommentProfile);
            }

        }
        else   {
            holder.sub_comment_section.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return commentsItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView commentProfile;
        TextView commentPerson, commentBody, commentDate, replyTxt;

        LinearLayout sub_comment_section;
        TextView moreComments, subCommentPerson, subCommentBody, subCommentDate;
        ImageView subCommentProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentProfile=itemView.findViewById(R.id.comment_profile);
            commentPerson=itemView.findViewById(R.id.comment_person);
            commentBody=itemView.findViewById(R.id.comment_body);
            commentDate=itemView.findViewById(R.id.comment_date);
            replyTxt=itemView.findViewById(R.id.reply_text);

            sub_comment_section=itemView.findViewById(R.id.sub_comment_section);
            moreComments=itemView.findViewById(R.id.more_comment);
            subCommentPerson=itemView.findViewById(R.id.sub_comment_person);
            subCommentBody=itemView.findViewById(R.id.sub_comment_body);
            subCommentDate=itemView.findViewById(R.id.sub_comment_date);
            subCommentProfile=itemView.findViewById(R.id.sub_comment_profile);



        }
    }
}
