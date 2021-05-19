package com.ecell.end_eavour.fab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.ecell.end_eavour.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetNavigationFragmentOne extends BottomSheetDialogFragment {

    public static BottomSheetNavigationFragmentOne newInstance() {
        Bundle args = new Bundle();
        BottomSheetNavigationFragmentOne fragmentOne = new BottomSheetNavigationFragmentOne();
        fragmentOne.setArguments(args);
        return fragmentOne;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //check the slide offset and change the visibility of close button
            if (slideOffset > 0.5) {
                closeButton1.setVisibility(View.VISIBLE);
            } else {
                closeButton1.setVisibility(View.GONE);
            }
        }
    };

    private ImageView closeButton1;
    //TextView one;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer_one, null);
        dialog.setContentView(contentView);

        ImageView image = (ImageView) contentView.findViewById(R.id.linked_in);
        ImageView image1 = (ImageView) contentView.findViewById(R.id.gmail);
        ImageView image2 = (ImageView) contentView.findViewById(R.id.fb);
        ImageView image3 = (ImageView) contentView.findViewById(R.id.insta);
        ImageView image4 = (ImageView) contentView.findViewById(R.id.you_tube);

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.linkedin.com/company/e-cell-kiet/"));
                startActivity(viewIntent);
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String to = "ecell@kiet.edu";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email Service :"));
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.facebook.com/ecellkiet/"));
                startActivity(viewIntent);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.instagram.com/kietecell/?igshid=lbrt09aprj0l"));
                startActivity(viewIntent);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.youtube.com/channel/UCtnkeQnhcAGS_AWKgYUicEA"));
                startActivity(viewIntent);
            }
        });
        closeButton1 = contentView.findViewById(R.id.close_image_view);
        closeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss bottom sheet
                dismiss();
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}