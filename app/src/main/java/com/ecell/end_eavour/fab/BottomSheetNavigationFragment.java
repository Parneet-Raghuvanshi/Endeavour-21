package com.ecell.end_eavour.fab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.contactus.Contactus;
import com.ecell.end_eavour.events.Events;
import com.ecell.end_eavour.schedule.Schedule;
import com.ecell.end_eavour.services.MyApplication;
import com.ecell.end_eavour.speakers.Speakers;
import com.ecell.end_eavour.sponsors.Sponsors;
import com.ecell.end_eavour.team.Team;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {

    public static BottomSheetNavigationFragment newInstance() {
        Bundle args = new Bundle();
        BottomSheetNavigationFragment fragment = new BottomSheetNavigationFragment();
        fragment.setArguments(args);
        return fragment;
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
                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }
        }
    };

    private ImageView closeButton;
    //TextView one;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);
        dialog.setContentView(contentView);

        final TextView name  = (TextView)contentView.findViewById(R.id.tv_name_models);
        final TextView phno = (TextView)contentView.findViewById(R.id.user_phno);
        final TextView referid = (TextView)contentView.findViewById(R.id.user_refer_id);

        NavigationView navigationView = contentView.findViewById(R.id.navigation_view);

        closeButton = (ImageView) contentView.findViewById(R.id.close_image_view);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //------------( Database Part )------//

        name.setText(((MyApplication) getActivity().getApplication()).getUserName());
        phno.setText(((MyApplication) getActivity().getApplication()).getUserPhoneNumber());
        referid.setText(((MyApplication) getActivity().getApplication()).getUserEndvrId());

        ///-----------------------------------------------------------------//////

        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav01:
                        Intent intent=new Intent(getActivity(), Events.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent );
                        break;
                    case R.id.nav02:
                        Intent intent2=new Intent(getActivity(), Schedule.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent2 );

                        break;
                    case R.id.nav03:
                        Intent intent3=new Intent(getActivity(), Speakers.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent3 );

                        break;
                    case R.id.nav04:
                        Intent intent4=new Intent(getActivity(), Sponsors.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent4 );

                        break;

                    case R.id.nav05:
                        Intent intent5=new Intent(getActivity(), Team.class);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent5 );

                        break;
                    case R.id.nav06:
                        Intent intent6=new Intent(getActivity(), Contactus.class);
                        intent6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dismiss();
                        startActivity( intent6 );
                        break;
                }
                return false;
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