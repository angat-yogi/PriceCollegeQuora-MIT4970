package com.angat.askmeanything.feature.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angat.askmeanything.R;
import com.angat.askmeanything.data.remote.ApiClient;
import com.angat.askmeanything.feature.fullimage.FullImageActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.model.profile.ProfileResponse;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private ImageView img;
    private String uid = "", profileUrl = "", coverUrl = "";
    private int currentState = 0;
    private Button profileOptionButton;
    private ImageView profileImage, coverImage;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProfileViewModel profileViewModel;
    private Boolean isCoverImage = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileOptionButton = findViewById(R.id.action_button);
        profileImage = findViewById(R.id.profile_image);
        coverImage = findViewById(R.id.profile_cover);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        recyclerView = findViewById(R.id.recycler_view_profile);
        progressBar = findViewById(R.id.progressbar);
        img = findViewById(R.id.test);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_dark);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.super.onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        uid = getIntent().getStringExtra("uid");

        if (uid.equals(FirebaseAuth.getInstance().getUid())) {
            currentState = 5;
            profileOptionButton.setText("Edit Profile");
        } else {
            //find the current state from backend
            profileOptionButton.setText("Loading...");
            profileOptionButton.setEnabled(false);
        }

        //instantiate required ViewModel
        ViewModelFactory vmf = new ViewModelFactory();
        profileViewModel = vmf.create(ProfileViewModel.class);

        fetchProfileInfo();

    }

    private void fetchProfileInfo() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getUid());
        if (currentState == 5) {
            params.put("current_state", currentState + "");
        }
        else{
            params.put("profileId",uid);

        }
        profileViewModel.fetchProfileInfo(params).observe(this, new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                if (profileResponse.getStatus() == 200) {
                    collapsingToolbarLayout.setTitle(profileResponse.getProfile().getName());
                    profileUrl = profileResponse.getProfile().getProfileUrl();
                    coverUrl = profileResponse.getProfile().getCoverUrl();

                    //possible casting?
                    currentState = profileResponse.getProfile().getState();

                    if (!profileUrl.isEmpty()) {
                        Uri profileUri = Uri.parse(profileUrl);
                        if (profileUri.getAuthority() == null) {
                            profileUrl = ApiClient.BASE_URL + profileUrl;
                        }
                        Glide.with(ProfileActivity.this).load(profileUrl).into(profileImage);
                    }
                    if (!coverUrl.isEmpty()) {
                        Uri coverUri = Uri.parse(profileUrl);
                        if (coverUri.getAuthority() == null) {
                            coverUrl = ApiClient.BASE_URL + coverUrl;
                        }
                        Glide.with(ProfileActivity.this).load(coverUrl).into(coverImage);
                    }
                    if (currentState == 0) {
                        profileOptionButton.setText("Loading..");
                        profileOptionButton.setEnabled(false);
                        return;
                    } else if (currentState == 1) {
                        profileOptionButton.setText("You are Friends");
                    } else if (currentState == 2) {
                        profileOptionButton.setText("Cancel Request");
                    } else if (currentState == 3) {
                        profileOptionButton.setText("Accept Request");

                    } else if (currentState == 4) {
                        profileOptionButton.setText("Send Request");

                    } else if (currentState == 5) {
                        profileOptionButton.setText("Edit Profile");

                    }
                    profileOptionButton.setEnabled(true);
                    loadProfileOptionButton();
                }else {
                        Toast.makeText(ProfileActivity.this, profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

            }
        });


    }

    private void loadProfileOptionButton() {

        profileOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileOptionButton.setEnabled(false);
                if (currentState == 5) {
                    CharSequence[] options = new CharSequence[]{"Change Cover Image", "Change Profile Image","View Cover Image","View Profile Image"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                isCoverImage = true;
                                selectImage();
                            } else if (which == 1) {
                                isCoverImage = false;
                                selectImage();
                            }
                            else if(which==2){
                                //user want to view cover image
                                viewFullImage(coverImage,coverUrl);
                            }
                            else if(which ==3){
                                //user want to view profile image
                                viewFullImage(profileImage,profileUrl);

                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnDismissListener(ProfileActivity.this);
                    dialog.show();
                }
            }
        });
    }

    private void viewFullImage(ImageView imageV, String imageUrl) {

        Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
        intent.putExtra("imageUrl",imageUrl);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Pair[] pairs = new Pair[1];
            pairs[0]= new Pair<View, String>(imageV, imageUrl);
          ActivityOptions activityOptions=  ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());

        }else{
            startActivity(intent);
        }
    }

    private void selectImage() {
        ImagePicker.create(this).single().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);
            try {
                File compressedImageFile = new Compressor(this).setQuality(75).
                        compressToFile(new File(selectedImage.getPath()));
                uploadImage(compressedImageFile);
            } catch (IOException e) {
                Toast.makeText(this, "Image Picker Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(File compressedImageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", FirebaseAuth.getInstance().getUid());
        builder.addFormDataPart("isCoverImage", isCoverImage + "");
        builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody
                .create(compressedImageFile, MediaType.parse("multipart/form-data")));

        progressDialog.show();
        profileViewModel.uploadPost(builder.build(),true).observe(this, new Observer<GeneralResponse>() {
            @Override
            public void onChanged(GeneralResponse generalResponse) {
                progressDialog.hide();
                Toast.makeText(ProfileActivity.this, generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if (generalResponse.getStatus()==200){
                    if(isCoverImage){
                        Glide.with(ProfileActivity.this).load(ApiClient.BASE_URL+generalResponse.getExtra()).into(coverImage);
                    }
                    else{
                        Glide.with(ProfileActivity.this).load(ApiClient.BASE_URL+generalResponse.getExtra()).into(profileImage);

                    }
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        profileOptionButton.setEnabled(true);
    }
}