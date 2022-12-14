package com.angat.askmeanything.feature.auth.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.auth.homepage.friends.FriendsFragment;
import com.angat.askmeanything.feature.auth.homepage.friends.FriendsLoadActivity;
import com.angat.askmeanything.feature.auth.homepage.newsfeed.NewsFeedFragment;
import com.angat.askmeanything.feature.auth.homepage.newsfeed.NewsLoadActivity;
import com.angat.askmeanything.feature.postupload.PostUploadActivity;
import com.angat.askmeanything.feature.profile.ProfileActivity;
import com.angat.askmeanything.feature.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FriendsFragment friendsFragment;
    private NewsFeedFragment newsFeedFragment;
    private FloatingActionButton fab;
    private Toolbar toolbarSearch;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); bottomNavigationView = findViewById(R.id.navigation);
        fab=findViewById(R.id.fab);
        toolbarSearch=findViewById(R.id.toolbar_search);
        friendsFragment = new FriendsFragment();
        newsFeedFragment = new NewsFeedFragment();
        progressBar = findViewById(R.id.progressbar);
        setFragment(newsFeedFragment);
        setBottomNavigationView();

        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostUploadActivity.class));
            }
        });
    }
    private void setBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.newsFeedFragment:
                        startActivity(new Intent(MainActivity.this, NewsLoadActivity.class).putExtra("uid", FirebaseAuth.getInstance().getUid()));
                        return true;
                    case R.id.friendFragment:
                        startActivity(new Intent(MainActivity.this, FriendsLoadActivity.class).putExtra("uid", FirebaseAuth.getInstance().getUid()));
                        return true;
                    case R.id.profileActivity:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("uid", FirebaseAuth.getInstance().getUid()));
                        return false;

                }
                return true;
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();
    }

    public void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgressbar(){
        progressBar.setVisibility(View.GONE);
    }
}