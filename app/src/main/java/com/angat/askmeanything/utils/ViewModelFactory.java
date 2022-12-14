package com.angat.askmeanything.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.data.remote.ApiService;
import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.feature.auth.LoginViewModel;
import com.angat.askmeanything.feature.auth.homepage.MainViewModel;
import com.angat.askmeanything.feature.auth.homepage.friends.FriendsViewModel;
import com.angat.askmeanything.feature.auth.homepage.newsfeed.NewsFeedViewModel;
import com.angat.askmeanything.feature.auth.homepage.newsfeed.NewsLoadActivity;
import com.angat.askmeanything.feature.postupload.PostUploadViewModel;
import com.angat.askmeanything.feature.profile.ProfileViewModel;
import com.angat.askmeanything.feature.search.SearchViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public ViewModelFactory() {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        repository = Repository.getRepository(apiService);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        } else if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(repository);
        } else if(modelClass.isAssignableFrom(PostUploadViewModel.class)){
            return (T) new PostUploadViewModel(repository);
        }
        else if(modelClass.isAssignableFrom(SearchViewModel.class)){
            return (T) new SearchViewModel(repository);
        }
        else if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(repository);
        }
        else if(modelClass.isAssignableFrom(FriendsViewModel.class)){
            return (T) new FriendsViewModel(repository);
        }
        else if(modelClass.isAssignableFrom(NewsFeedViewModel.class)){
            return (T) new NewsFeedViewModel(repository);
        }
        else{
            throw new IllegalArgumentException("View Model not found");
        }


    }
}
