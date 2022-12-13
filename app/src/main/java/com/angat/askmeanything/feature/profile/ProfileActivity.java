package com.angat.askmeanything.feature.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angat.askmeanything.R;
import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.feature.auth.LoginViewModel;
import com.angat.askmeanything.model.profile.ProfileResponse;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ImageView img;
    private String uid="",profileUrl="",coverUrl="";
    private  int currentState =0;
    private Button profileOptionButton;
    private ImageView profileImage, coverImage;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileOptionButton=findViewById(R.id.action_button);
        profileImage =findViewById(R.id.profile_image);
        coverImage=findViewById(R.id.profile_cover);
        toolbar=findViewById(R.id.toolBar);
        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        recyclerView=findViewById(R.id.recycler_view_profile);
        progressBar=findViewById(R.id.progressbar);
        img = findViewById(R.id.test);

        uid=getIntent().getStringExtra("uid");

        if(uid.equals(FirebaseAuth.getInstance().getUid())){
            currentState=5;
            profileOptionButton.setText("Edit Profile");
        }else{
            //find the current state from backend
            profileOptionButton.setText("Loading...");
            profileOptionButton.setEnabled(false);
        }

        //instantiate required ViewModel
        ViewModelFactory vmf = new ViewModelFactory();
        profileViewModel= vmf.create(ProfileViewModel.class);

        fetchProfileInfo();

    }

    private void fetchProfileInfo() {

        Map<String,String> params=new HashMap<>();
        params.put("userId",FirebaseAuth.getInstance().getUid());
        if (currentState==5){
            params.put("current_state",currentState+"");
        }
        profileViewModel.fetchProfileInfo(params).observe(this, new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                if (profileResponse.getStatus()==200){
                    collapsingToolbarLayout.setTitle(profileResponse.getProfile().getName());
                    profileUrl=profileResponse.getProfile().getProfileUrl();
                    coverUrl=profileResponse.getProfile().getCoverUrl();

                    //possible casting?
                    currentState = profileResponse.getProfile().getState();

                    if (!profileUrl.isEmpty()) {
                        Uri profileUri = Uri.parse(profileUrl);
                        if (profileUri.getAuthority() == null) {
                            profileUrl = ApiClient.BASE_URL + profileUrl;
                        }
                        Glide.with(ProfileActivity.this).load(profileUrl).into(profileImage);
                    }
                        if (!coverUrl.isEmpty()){
                            Uri coverUri = Uri.parse(profileUrl);
                            if (coverUri.getAuthority()==null){
                                coverUrl= ApiClient.BASE_URL+coverUrl;
                            }
                        Glide.with(ProfileActivity.this).load(coverUrl).into(coverImage);

                            switch (currentState){
                                case 0:
                                    profileOptionButton.setText("Loading..");
                                    profileOptionButton.setEnabled(false);
                                    return;
                                case 1:
                                    profileOptionButton.setText("You are Friends");
                                    break;
                                case 2:
                                    profileOptionButton.setText("Cancel Request");
                                    break;
                                case 3:
                                    profileOptionButton.setText("Accept Request");
                                    break;
                                case 4:
                                    profileOptionButton.setText("Send Request");
                                    break;
                                case 5:
                                    profileOptionButton.setText("Edit Profile");
                                    break;
                            }
                            profileOptionButton.setEnabled(true);

                        }
                        else{
                            Toast.makeText(ProfileActivity.this, profileResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });


    }
}