package com.angat.askmeanything.feature.postupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.Observer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.auth.homepage.MainActivity;
import com.angat.askmeanything.model.GeneralResponse;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostUploadActivity extends AppCompatActivity {

    TextView post;
    TextInputEditText inputPost;
    private ProgressDialog progressDialog;
    private PostUploadViewModel pvm;
    private Boolean isImageSelected=false;
    ImageView addImage,previewImage;
    private File compressedImageFile=null;

    private AppCompatSpinner spinner;
    private int privacy=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        //Instantiating ViewModel
        ViewModelFactory vmf = new ViewModelFactory();
        pvm= vmf.create(PostUploadViewModel.class);
        spinner = findViewById(R.id.spinner_privacy);
        post = findViewById(R.id.post_btn);
        inputPost=findViewById(R.id.input_post);
        addImage = findViewById(R.id.add_image);
        previewImage=findViewById(R.id.image_preview);
        addImage.setOnClickListener(v-> selectImage());
        previewImage.setOnClickListener(v-> selectImage());
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Uploading Post... Please Wait!");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = inputPost.getText().toString();
                String userId = FirebaseAuth.getInstance().getUid();


                if (status.trim().length()>0) {
                    progressDialog.show();
                    //perform network operation
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM);
                    builder.addFormDataPart("post",status);
                    builder.addFormDataPart("postUserId",userId);
                    builder.addFormDataPart("privacy",privacy+"");

                    if (isImageSelected){
                        builder.addFormDataPart("file",compressedImageFile.getName(),
                                                RequestBody.create(compressedImageFile, MediaType.parse("multipart/form-data")));
                    }
                    MultipartBody multipartBody = builder.build();
                    pvm.uploadPost(multipartBody,false).observe(PostUploadActivity.this, new Observer<GeneralResponse>() {
                        @Override
                        public void onChanged(GeneralResponse generalResponse) {
                            progressDialog.hide();
                            Toast.makeText(PostUploadActivity.this,generalResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            if (generalResponse.getStatus()==200){
                                Intent intent = new Intent(PostUploadActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(PostUploadActivity.this,"Please Write Your Concern",Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.hide();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedTextView = (TextView) view;
                if (selectedTextView!=null){
                    selectedTextView.setTextColor(Color.WHITE);
                    selectedTextView.setTypeface(null, Typeface.BOLD);
                }
                privacy=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            privacy =0;
            }
        });
    }

    private void selectImage() {
        ImagePicker.create(this).single().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);
            try {
                compressedImageFile = new Compressor(this).setQuality(75).compressToFile(new File(selectedImage.getPath()));
                isImageSelected=true;
                addImage.setVisibility(View.GONE);
                previewImage.setVisibility(View.VISIBLE);

                Glide.with(PostUploadActivity.this).load(selectedImage.getPath()).
                        error(R.drawable.cover_picture_placeholder)
                        .placeholder(R.drawable.cover_picture_placeholder)
                .into(previewImage);
                Toast.makeText(this,"Image Selected Successfully", Toast.LENGTH_SHORT).show();

            }catch (IOException e){
                previewImage.setVisibility(View.GONE);
                addImage.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Image Picker Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}