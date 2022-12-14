package com.angat.askmeanything.feature.auth.homepage.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.feature.profile.ProfileActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.model.profile.ProfileResponse;

import java.util.Map;

import okhttp3.MultipartBody;

public class FriendsViewModel extends ViewModel {
    private Repository repository;

    public FriendsViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<FriendResponse> loadFriends(String uid){
        return repository.loadFriends(uid);
    }
}
