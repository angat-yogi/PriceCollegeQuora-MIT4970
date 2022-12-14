package com.angat.askmeanything.data.remote;

import com.angat.askmeanything.feature.auth.LoginActivity;
import com.angat.askmeanything.feature.postupload.post.PostResponse;
import com.angat.askmeanything.feature.profile.ProfileActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.auth.AuthResponse;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.model.profile.ProfileResponse;
import com.angat.askmeanything.model.search.SearchResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
   @GET("search")
   Call<SearchResponse> search(@QueryMap Map<String, String> params);
   @GET("getnewsfeed")
   Call<PostResponse> getNewsFeed(@QueryMap Map<String, String> params);
   @POST("performaction")
   Call<GeneralResponse> performAction(@Body ProfileActivity.PerformAction performAction);
   @GET("loadfriends")
   Call<FriendResponse> loadFriends(@Query("uid") String uid);

}
