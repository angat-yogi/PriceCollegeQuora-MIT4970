package com.angat.askmeanything.feature.auth.homepage.newsfeed;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.auth.homepage.MainViewModel;
import com.angat.askmeanything.feature.postupload.post.PostsItem;

import java.util.List;

public class NewsFeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainViewModel mainViewModel;
    private Context context;
//    private PostAdapter postAdapter;
    private List<PostsItem> postsItems;
    private Boolean isFirstLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

}