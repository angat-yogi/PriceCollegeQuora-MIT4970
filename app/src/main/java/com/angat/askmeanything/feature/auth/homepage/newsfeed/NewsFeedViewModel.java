package com.angat.askmeanything.feature.auth.homepage.newsfeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.model.post.PostResponse;

import java.util.Map;

public class NewsFeedViewModel extends ViewModel {
    private Repository repository;

    public NewsFeedViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<PostResponse> getNewsFeed(Map<String, String> params){
        return repository.getNewsFeed(params);
    }
}
