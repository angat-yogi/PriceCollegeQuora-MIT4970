package com.angat.askmeanything.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.angat.askmeanything.model.auth.AuthResponse;
import com.angat.askmeanything.model.comment.CommentResponse;
import com.angat.askmeanything.utils.Util;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private final ApiService apiService;
    private static CommentRepository instance = null;

    public CommentRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static CommentRepository getRepository(ApiService apiService){
        if (instance==null){
            instance = new CommentRepository(apiService);
        }
        return instance;
    }

    public LiveData<CommentResponse> postComment(Util.PostComment postComment){
        MutableLiveData<CommentResponse> comments= new MutableLiveData<>();
        Call<CommentResponse> call = apiService.postComment(postComment);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()){
                    comments.postValue(response.body());
                }
                else{
                    Gson gson = new Gson();
                    CommentResponse commentResponse = null;
                    try {
                        commentResponse=gson.fromJson(response.errorBody().string(),CommentResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        commentResponse=new CommentResponse(errorMessage.message,errorMessage.status);

                    }

                    comments.postValue(commentResponse);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                CommentResponse commentResponse = new CommentResponse(errorMessage.message,errorMessage.status);
                comments.postValue(commentResponse);
            }
        });
        return comments;
    }

    public LiveData<CommentResponse> getPostComments(String postId, String postUserId){
        MutableLiveData<CommentResponse> comments= new MutableLiveData<>();
        Call<CommentResponse> call = apiService.getPostComments(postId,postUserId);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()){
                    comments.postValue(response.body());
                }
                else{
                    Gson gson = new Gson();
                    CommentResponse commentResponse = null;
                    try {
                        commentResponse=gson.fromJson(response.errorBody().string(),CommentResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        commentResponse=new CommentResponse(errorMessage.message,errorMessage.status);

                    }

                    comments.postValue(commentResponse);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                CommentResponse commentResponse = new CommentResponse(errorMessage.message,errorMessage.status);
                comments.postValue(commentResponse);
            }
        });
        return comments;
    }
}
