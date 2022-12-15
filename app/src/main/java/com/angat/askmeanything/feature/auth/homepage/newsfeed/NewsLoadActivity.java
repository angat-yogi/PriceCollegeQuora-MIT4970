package com.angat.askmeanything.feature.auth.homepage.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.auth.homepage.friends.FriendsViewModel;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.model.post.PostResponse;
import com.angat.askmeanything.model.post.PostsItem;
import com.angat.askmeanything.utils.Util;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.angat.askmeanything.utils.adapter.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsLoadActivity extends AppCompatActivity implements Util.IOnCommentAdded {
    private NewsFeedViewModel newsFeedViewModel;
    private PostAdapter postAdapter;
    private List<PostsItem> posts = new ArrayList<>();
    private RecyclerView newsFeedRecyclerView;
    private boolean isFirstLoading=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_load);
        newsFeedRecyclerView= findViewById(R.id.recycler_view_newsfeed);


        //recycler_view_newsfeed
        postAdapter = new PostAdapter(this, posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        newsFeedRecyclerView.setAdapter(postAdapter);
        newsFeedRecyclerView.setLayoutManager(linearLayoutManager);
        ViewModelFactory vmf = new ViewModelFactory();
        newsFeedViewModel = vmf.create(NewsFeedViewModel.class);

        fetchPosts();
    }

    private void fetchPosts() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", FirebaseAuth.getInstance().getUid());
        params.put("limit","2");
        params.put("offset","0");
        //  ((MainActivity)getActivity()).showProgressBar();

        newsFeedViewModel.getNewsFeed(params).observe(this, new Observer<PostResponse>() {
            @Override
            public void onChanged(PostResponse postResponse) {
                //   ((MainActivity)getActivity()).hideProgressBar();
                if (postResponse.getStatus()==200){
                    posts.addAll(postResponse.getPosts());
                    if (isFirstLoading){
                        postAdapter = new PostAdapter(NewsLoadActivity.this, posts);
                        newsFeedRecyclerView.setAdapter(postAdapter);
                    }else{
                        postAdapter.notifyItemRangeInserted(posts.size(),postResponse.getPosts().size());
                    }
                    isFirstLoading=false;
                }
                else{
                    Toast.makeText(NewsLoadActivity.this, postResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        posts.clear();
        isFirstLoading=true;
    }

    @Override
    public void onCommentAdded(int adapterPosition) {
        postAdapter.increasePostCommentCount(adapterPosition);
    }

}