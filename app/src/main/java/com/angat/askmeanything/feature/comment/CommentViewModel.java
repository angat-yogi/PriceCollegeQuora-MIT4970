package com.angat.askmeanything.feature.comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.CommentRepository;
import com.angat.askmeanything.model.comment.CommentResponse;
import com.angat.askmeanything.utils.Util;

public class CommentViewModel extends ViewModel {

    public final CommentRepository commentRepository;

    public CommentViewModel(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public LiveData<CommentResponse> postComment(Util.PostComment postComment){
        return commentRepository.postComment(postComment);
    }
    public LiveData<CommentResponse> getPostComments(String postId, String postUserId){
        return commentRepository.getPostComments(postId,postUserId);
    }
}
