package com.angat.askmeanything.feature.auth.homepage.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.profile.ProfileViewModel;
import com.angat.askmeanything.model.friend.Friend;
import com.angat.askmeanything.model.friend.FriendResponse;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FriendsLoadActivity extends AppCompatActivity {

    private FriendsViewModel friendsViewModel;
    private RecyclerView friendRequestRecv, friendsRecyv;
    private TextView friendTitle,requestTitle,defaultTitle;
    private FriendAdapter friendAdapter;
    private List<Friend> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_load);

         ViewModelFactory vmf = new ViewModelFactory();
        friendsViewModel = vmf.create(FriendsViewModel.class);
        friendRequestRecv = findViewById(R.id.friend_rqst_rcv);
        friendsRecyv = findViewById(R.id.friend_rcv);
        friendTitle=findViewById(R.id.friend_title);
        requestTitle = findViewById(R.id.requestTitle);
        defaultTitle=findViewById(R.id.default_textview);

        friendAdapter = new FriendAdapter(this,friendList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        friendsRecyv.setAdapter(friendAdapter);
        friendsRecyv.setLayoutManager(linearLayoutManager);

        fetchFriends();
    }

    private void fetchFriends() {
        friendsViewModel.loadFriends(FirebaseAuth.getInstance().getUid()).observe(this, new Observer<FriendResponse>() {
            @Override
            public void onChanged(FriendResponse friendResponse) {
                loadData(friendResponse);
            }
        });
    }

    private void loadData(FriendResponse friendResponse) {
        if (friendResponse.getStatus()==200){
            friendList.clear();
            friendList.addAll(friendResponse.getResult().getFriends());
            friendAdapter.notifyDataSetChanged();
            //Toast.makeText(FriendsLoadActivity.this,friendResponse.getMessage(),Toast.LENGTH_SHORT).show();

            if (friendResponse.getResult().getFriends().size()>0){
                friendTitle.setVisibility(View.VISIBLE);
            }else{
                friendTitle.setVisibility(View.GONE);

            }
        }
        else{
            Toast.makeText(FriendsLoadActivity.this,"Error: "+friendResponse.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }
}