package com.ecell.end_eavour.fab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.glimpses.Glimpses;
import com.ecell.end_eavour.glimpses.Glimpses_Adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetNavigationFragmentTwo extends BottomSheetDialogFragment {

    public static BottomSheetNavigationFragmentTwo newInstance() {
        Bundle args = new Bundle();
        BottomSheetNavigationFragmentTwo fragmentTwo = new BottomSheetNavigationFragmentTwo();
        fragmentTwo.setArguments(args);
        return fragmentTwo;
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
    private RecyclerView recyclerView;
    private Glimpses_Adapter glimpses_adapter;
    private List<Glimpses> glimpses;

    //TextView one;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        final View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer_two, null);
        dialog.setContentView(contentView);

        closeButton1 = contentView.findViewById(R.id.close_image_view);
        recyclerView = contentView.findViewById(R.id.rv_glimp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(contentView.getContext(),2));
        glimpses = new ArrayList<>();

        glimpses.add(new Glimpses(R.drawable.a00001));
        glimpses.add(new Glimpses(R.drawable.a0002));
        glimpses.add(new Glimpses(R.drawable.a0003));
        glimpses.add(new Glimpses(R.drawable.a0004));
        glimpses.add(new Glimpses(R.drawable.a0005));
        glimpses.add(new Glimpses(R.drawable.a0006));
        glimpses.add(new Glimpses(R.drawable.a0007));
        glimpses.add(new Glimpses(R.drawable.a0009));
        glimpses.add(new Glimpses(R.drawable.a0010));
        glimpses.add(new Glimpses(R.drawable.a0011));
        glimpses.add(new Glimpses(R.drawable.a0012));
        glimpses.add(new Glimpses(R.drawable.a0013));
        glimpses.add(new Glimpses(R.drawable.a0014));
        glimpses.add(new Glimpses(R.drawable.a0015));
        glimpses.add(new Glimpses(R.drawable.a0016));
        glimpses.add(new Glimpses(R.drawable.a0017));
        glimpses.add(new Glimpses(R.drawable.a0018));
        glimpses.add(new Glimpses(R.drawable.a0019));
        glimpses.add(new Glimpses(R.drawable.a0020));
        glimpses.add(new Glimpses(R.drawable.a0021));
        glimpses.add(new Glimpses(R.drawable.a0022));

        glimpses_adapter = new Glimpses_Adapter(contentView.getContext(),glimpses);

        recyclerView.setAdapter(glimpses_adapter);

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