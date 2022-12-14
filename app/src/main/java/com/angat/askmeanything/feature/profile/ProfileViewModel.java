package com.angat.askmeanything.feature.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.profile.ProfileResponse;

import java.util.Map;

import okhttp3.MultipartBody;


public class ProfileViewModel extends ViewModel {

    private Repository repository;

    public ProfileViewModel(Repository repository) {
        this.repository = repository;
    }
    public LiveData<ProfileResponse> fetchProfileInfo(Map<String,String>params){
        return repository.fetchProfileInfo(params);
    }
    public LiveData<GeneralResponse> uploadPost(MultipartBody body, Boolean isCoverOrPostImage){
        return this.repository.uploadPost(body,isCoverOrPostImage);
    }


}
