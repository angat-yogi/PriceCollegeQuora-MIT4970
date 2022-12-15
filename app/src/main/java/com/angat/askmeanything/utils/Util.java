package com.angat.askmeanything.utils;

public class Util {
    public static class PostComment
    {
        public PostComment(String comment, String commentBy, String commentPostId, String postUserId, String commentOn, String commentUserId, String parentId) {
            this.comment = comment;
            this.commentBy = commentBy;
            this.commentPostId = commentPostId;
            this.postUserId = postUserId;
            this.commentOn = commentOn;
            this.commentUserId = commentUserId;
            this.parentId = parentId;
        }

        String comment, commentBy, commentPostId, postUserId, commentOn, commentUserId, parentId;

    }
    public interface IOnCommentAdded{
        void onCommentAdded(int adapterPosition);
    }
}
