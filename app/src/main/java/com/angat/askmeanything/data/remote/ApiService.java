package com.angat.askmeanything.data.remote;

import com.angat.askmeanything.feature.auth.LoginActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.auth.AuthResponse;
import com.angat.askmeanything.model.profile.ProfileResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {
   @POST("login")
   Call<AuthResponse> login(@Body LoginActivity.UserInfo userInfo);
   @POST("uploadpost")
   Call<GeneralResponse> uploadPost(@Body MultipartBody body);
   @POST("uploadImage")
   Call<GeneralResponse> uploadImage(@Body MultipartBody body);
   @GET("loadprofileinfo")
   Call<ProfileResponse> fetchProfileInfo(@QueryMap Map<String, String> params);
}
