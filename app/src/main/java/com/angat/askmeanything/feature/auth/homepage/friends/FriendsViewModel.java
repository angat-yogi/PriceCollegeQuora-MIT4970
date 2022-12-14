package com.angat.askmeanything.feature.auth.homepage.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.model.friend.FriendResponse;

public class FriendsViewModel extends ViewModel {
    private Repository repository;

    public FriendsViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<FriendResponse> loadFriends(String uid){
        return repository.loadFriends(uid);
    }
}
