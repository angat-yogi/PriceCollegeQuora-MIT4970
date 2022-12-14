package com.angat.askmeanything.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.angat.askmeanything.feature.auth.LoginActivity;
import com.angat.askmeanything.feature.postupload.post.PostResponse;
import com.angat.askmeanything.feature.profile.ProfileActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.auth.AuthResponse;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.model.profile.Profile;
import com.angat.askmeanything.model.profile.ProfileResponse;
import com.angat.askmeanything.model.search.SearchResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private final ApiService apiService;
    private static Repository repository=null;

    private Repository(ApiService apiService){
        this.apiService=apiService;
    }
    public static Repository getRepository(ApiService apiService){
        if (repository==null){
            repository = new Repository(apiService);
        }
        return repository;
    }

    public LiveData<AuthResponse> login(LoginActivity.UserInfo userInfo){
        MutableLiveData<AuthResponse> auth= new MutableLiveData<>();
        Call<AuthResponse> call = apiService.login(userInfo);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()){
                    auth.postValue(response.body());
                }
                else{
                    Gson gson = new Gson();
                    AuthResponse authResponse = null;
                    try {
                        authResponse=gson.fromJson(response.errorBody().string(),AuthResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        authResponse=new AuthResponse(errorMessage.message,errorMessage.status);

                    }

                    auth.postValue(authResponse);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                AuthResponse authResponse = new AuthResponse(errorMessage.message,errorMessage.status);
                auth.postValue(authResponse);
            }
        });
        return auth;

    }
    public LiveData<ProfileResponse> fetchProfileInfo(Map<String,String> params){
        MutableLiveData<ProfileResponse> userInfo = new MutableLiveData<>();
        Call<ProfileResponse> call = apiService.fetchProfileInfo(params);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()){
                    userInfo.postValue(response.body());
                }
                else   {
                    Gson gson = new Gson();
                    ProfileResponse profileResponse = null;
                    try {
                        profileResponse=gson.fromJson(response.errorBody().string(),ProfileResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        profileResponse=new ProfileResponse(errorMessage.message,errorMessage.status);

                    }

                    userInfo.postValue(profileResponse);

                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                ProfileResponse profileResponse = new ProfileResponse(errorMessage.message,errorMessage.status);
                userInfo.postValue(profileResponse);
            }
        });

        return userInfo;
    }

    public LiveData<GeneralResponse> uploadPost(MultipartBody multipartBody, Boolean isCoverOrProfilPost){
        MutableLiveData<GeneralResponse> postUp = new MutableLiveData<>();
        Call<GeneralResponse> call=null;
        if (isCoverOrProfilPost){
           call= apiService.uploadImage(multipartBody);
        }
        else{
            call = apiService.uploadPost(multipartBody);
        }

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()){
                    postUp.postValue(response.body());
                }else{
                    Gson gson = new Gson();
                    GeneralResponse generalResponse = null;
                    try {
                        generalResponse=gson.fromJson(response.errorBody().string(),GeneralResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        generalResponse=new GeneralResponse(errorMessage.message,errorMessage.status);

                    }

                    postUp.postValue(generalResponse);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                GeneralResponse generalResponse = new GeneralResponse(errorMessage.message,errorMessage.status);
                postUp.postValue(generalResponse);
            }
        });
        return postUp;
    }

    public LiveData<SearchResponse> search(Map<String,String> params){
        MutableLiveData<SearchResponse> searchInfo = new MutableLiveData<>();
        Call<SearchResponse> call = apiService.search(params);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()){
                    searchInfo.postValue(response.body());
                }
                else   {
                    Gson gson = new Gson();
                    SearchResponse searchResponse = null;
                    try {
                        searchResponse=gson.fromJson(response.errorBody().string(),SearchResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        searchResponse=new SearchResponse(errorMessage.message,errorMessage.status);

                    }

                    searchInfo.postValue(searchResponse);

                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                SearchResponse searchResponse = new SearchResponse(errorMessage.message,errorMessage.status);
                searchInfo.postValue(searchResponse);
            }
        });

        return searchInfo;
    }
    public LiveData<FriendResponse> loadFriends(String uid){
        MutableLiveData<FriendResponse> searchInfo = new MutableLiveData<>();
        Call<FriendResponse> call = apiService.loadFriends(uid);
        call.enqueue(new Callback<FriendResponse>() {
            @Override
            public void onResponse(Call<FriendResponse> call, Response<FriendResponse> response) {
                if (response.isSuccessful()){
                    searchInfo.postValue(response.body());
                }
                else   {
                    Gson gson = new Gson();
                    FriendResponse friendResponse = null;
                    try {
                        friendResponse=gson.fromJson(response.errorBody().string(),FriendResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        friendResponse=new FriendResponse(errorMessage.message,errorMessage.status);

                    }

                    searchInfo.postValue(friendResponse);

                }
            }

            @Override
            public void onFailure(Call<FriendResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                FriendResponse searchResponse = new FriendResponse(errorMessage.message,errorMessage.status);
                searchInfo.postValue(searchResponse);
            }
        });

        return searchInfo;
    }

    public LiveData<GeneralResponse> performOperation(ProfileActivity.PerformAction performAction){
        MutableLiveData<GeneralResponse> searchInfo = new MutableLiveData<>();
        Call<GeneralResponse> call = apiService.performAction(performAction);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful()){
                    searchInfo.postValue(response.body());
                }
                else   {
                    Gson gson = new Gson();
                    GeneralResponse generalResponse = null;
                    try {
                        generalResponse=gson.fromJson(response.errorBody().string(),GeneralResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        generalResponse=new GeneralResponse(errorMessage.message,errorMessage.status);

                    }

                    searchInfo.postValue(generalResponse);

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                GeneralResponse generalResponse = new GeneralResponse(errorMessage.message,errorMessage.status);
                searchInfo.postValue(generalResponse);
            }
        });

        return searchInfo;
    }
    public LiveData<PostResponse> getNewsFeed(Map<String,String> params){
        MutableLiveData<PostResponse> post = new MutableLiveData<>();
        Call<PostResponse> call = apiService.getNewsFeed(params);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()){
                    post.postValue(response.body());
                }
                else   {
                    Gson gson = new Gson();
                    PostResponse postResponse = null;
                    try {
                        postResponse=gson.fromJson(response.errorBody().string(),PostResponse.class);

                    }catch (IOException e){
                        ApiError.ErrorMessage errorMessage = ApiError.getErrorFromException(e);
                        postResponse=new PostResponse(errorMessage.message,errorMessage.status);

                    }

                    post.postValue(postResponse);

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                ApiError.ErrorMessage errorMessage = ApiError.getErrorFromThrowable(t);
                PostResponse postResponse = new PostResponse(errorMessage.message,errorMessage.status);
                post.postValue(postResponse);
            }
        });

        return post;
    }

}
