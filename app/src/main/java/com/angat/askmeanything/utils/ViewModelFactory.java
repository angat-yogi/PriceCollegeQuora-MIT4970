package com.angat.askmeanything.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.data.remote.ApiService;
import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.feature.auth.LoginViewModel;
import com.angat.askmeanything.feature.postupload.PostUploadViewModel;
import com.angat.askmeanything.feature.profile.ProfileViewModel;

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
        else{
            throw new IllegalArgumentException("View Model not found");
        }


    }
}
