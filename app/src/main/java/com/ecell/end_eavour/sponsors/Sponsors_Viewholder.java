package com.ecell.end_eavour.sponsors;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Sponsors_Viewholder extends RecyclerView.ViewHolder {

    TextView sponsort;
    TextView sponsorC;
    ImageView sponsorImage;
    RelativeLayout layoutSponsorCard;

    public Sponsors_Viewholder(@NonNull View itemView) {
        super(itemView);

        sponsort = itemView.findViewById(R.id.tv_sponsor_name);
        sponsorC = itemView.findViewById(R.id.tv_sponsor_domain);
        sponsorImage = itemView.findViewById(R.id.image_sponsor);
        layoutSponsorCard = itemView.findViewById(R.id.layout_sponsor_card);
    }
}