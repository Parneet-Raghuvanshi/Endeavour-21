package com.ecell.end_eavour.glimpses;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.R;
import com.github.chrisbanes.photoview.PhotoView;

public class GlimpsesDetail extends AppCompatActivity {

    public PhotoView photoView;
    public int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_glipmses_detail);

        img = getIntent().getIntExtra("imguri",img);

        photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(img);
    }
}
