package com.ecell.end_eavour.events.humor;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.github.chrisbanes.photoview.PhotoView;

public class VoteDetail extends AppCompatActivity {

    public PhotoView photoView;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vote_detail);

        uri = getIntent().getStringExtra("imageuri");

        photoView = (PhotoView) findViewById(R.id.photo_view);
        Glide.with(VoteDetail.this).load(uri).into(photoView);
    }
}