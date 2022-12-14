package com.angat.askmeanything.feature.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.angat.askmeanything.data.remote.Repository;
import com.angat.askmeanything.model.search.SearchResponse;

import java.util.Map;

public class SearchViewModel extends ViewModel {

    private Repository repository;

    public SearchViewModel(Repository repository) {
        this.repository = repository;
    }
    public LiveData<SearchResponse> search(Map<String,String> params){
        return repository.search(params);
    }
}
