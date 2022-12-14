package com.angat.askmeanything.feature.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.angat.askmeanything.R;
import com.angat.askmeanything.model.search.SearchResponse;
import com.angat.askmeanything.model.search.User;
import com.angat.askmeanything.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView defaultText;
    private SearchViewModel searchViewModel;
    private SearchAdapter searchAdapter;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ViewModelFactory vmf = new ViewModelFactory();
        searchViewModel= vmf.create(SearchViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search);
        recyclerView = findViewById(R.id.search_recycler_view);
        defaultText = findViewById(R.id.default_text);
        userList= new ArrayList<>();
        searchAdapter = new SearchAdapter(this,userList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchAdapter);
        searchView.setQueryHint("Search People");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDb(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().length()>3){
                    searchDb(newText);
                }
                else{
                    defaultText.setVisibility(View.VISIBLE);
                    userList.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    private void searchDb(String query) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword",query);
        searchViewModel.search(params).observe(SearchActivity.this, new Observer<SearchResponse>() {
            @Override
            public void onChanged(SearchResponse searchResponse) {
                if (searchResponse.getStatus()==200){
                    defaultText.setVisibility(View.GONE);
                    userList.clear();
                    userList.addAll(searchResponse.getUser());
                    searchAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(SearchActivity.this,searchResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    defaultText.setVisibility(View.VISIBLE);
                }

            }
        });

    }
}