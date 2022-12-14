package com.angat.askmeanything.feature.auth.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.feature.postupload.post.PostResponse;
import com.angat.askmeanything.model.friend.FriendResponse;


import java.util.Map;

public class MainViewModel extends ViewModel {

    private final Repository repository;

    public MainViewModel(Repository repository) {
        this.repository=repository;
    }

    public LiveData<PostResponse> getNewsFeed(Map<String,String> params){
        return repository.getNewsFeed(params);
    }
    public LiveData<FriendResponse> loadFriends(String uid){
        return repository.loadFriends(uid);
    }
}
