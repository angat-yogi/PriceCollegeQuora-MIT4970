package com.angat.askmeanything.feature.comment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angat.askmeanything.R;
import com.angat.askmeanything.feature.profile.ProfileViewModel;
import com.angat.askmeanything.model.comment.CommentResponse;
import com.angat.askmeanything.model.comment.CommentsItem;
import com.angat.askmeanything.utils.Util;
import com.angat.askmeanything.utils.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PostCommentReplyBottomSheet extends BottomSheetDialogFragment {
    Context context;
    CommentViewModel viewModel;
    private TextView commentsCounterTxt;
    private RecyclerView recyclerView;
    private EditText commentEditText;
    private LinearLayout commentSendWrapper;
    private String postId, postUserId, commentOn, commentUserId, parentId;
    private Boolean shouldOpenKeyboard = false;
    private int commentCounter = 0, postAdapterPosition = 0;
    private Util.IOnCommentAdded iOnCommentAdded;

    private CommentAdapter commentAdapter;
    private List<CommentsItem> commentsItems= new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //instantiate required ViewModel
        ViewModelFactory vmf = new ViewModelFactory();
        viewModel = vmf.create(CommentViewModel.class);

        postId = getArguments().getString("postId");
        postUserId = getArguments().getString("postUserId");
        commentOn = getArguments().getString("commentOn");
        commentUserId = getArguments().getString("commentUserId");
        parentId = getArguments().getString("parentId");
        commentCounter = getArguments().getInt("commentCounter");
        shouldOpenKeyboard=getArguments().getBoolean("shouldOpenKeyboard");
        postAdapterPosition=getArguments().getInt("postAdapterPosition");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        this.iOnCommentAdded= (Util.IOnCommentAdded) context;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(context, R.layout.bottomsheet_comment,null);
        dialog.setContentView(view);

        commentsCounterTxt=view.findViewById(R.id.commentsCounter);
        recyclerView=view.findViewById(R.id.comment_recyv);
        commentEditText = view.findViewById(R.id.comment_edt_text);
        commentSendWrapper=view.findViewById(R.id.comment_wrapper);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        getPostComments();
        if (commentCounter==0||commentCounter==1){
            commentsCounterTxt.setText(commentCounter +" comment");
        }
        else{commentsCounterTxt.setText(commentCounter +" comments");
        }
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog dialog1 = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = dialog1.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        commentSendWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentTxt = commentEditText.getText().toString();

                if (commentTxt.isEmpty()){
                    Toast.makeText(context,"Please enter your comment", Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                    commentEditText.setText("");
                    viewModel.postComment(new Util.PostComment(
                            commentTxt,
                            FirebaseAuth.getInstance().getUid(),
                            postId,
                            postUserId,
                            commentOn,
                            commentUserId,
                            parentId
                    )).observe(getActivity(), new Observer<CommentResponse>() {
                        @Override
                        public void onChanged(CommentResponse commentResponse) {
                            Toast.makeText(context,commentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            if (commentResponse.getStatus()==200){
                                commentCounter++;
                                if (commentCounter==1){
                                commentsCounterTxt.setText(commentCounter +" comment");
                                }
                                else{commentsCounterTxt.setText(commentCounter +" comments");
                                }
                                iOnCommentAdded.onCommentAdded(postAdapterPosition);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getPostComments() {
        viewModel.getPostComments(postId,postUserId).observe(this, new Observer<CommentResponse>() {
            @Override
            public void onChanged(CommentResponse commentResponse) {
                if (commentResponse.getStatus()==200){
                    commentsItems.clear();
                    commentsItems.addAll(commentResponse.getComments());
                    commentAdapter= new CommentAdapter(context,commentsItems);
                    recyclerView.setAdapter(commentAdapter);
                }
                else{
                    Toast.makeText(context,commentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
