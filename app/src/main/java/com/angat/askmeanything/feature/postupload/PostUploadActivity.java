package com.angat.askmeanything.feature.postupload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

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
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostUploadActivity extends AppCompatActivity {

    TextView post;
    TextInputEditText inputPost;
    private ProgressDialog progressDialog;

    private AppCompatSpinner spinner;
    private int privacy=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        spinner = findViewById(R.id.spinner_privacy);
        post = findViewById(R.id.post_btn);
        inputPost=findViewById(R.id.input_post);
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Uploading Post..");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputPost.getText().toString().trim().length()>0) {
                    progressDialog.show();
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

            }
        });
    }

    private void postDataUsingVolley(int privacyL, String post, String imageStatus, String userId) {


    };
}