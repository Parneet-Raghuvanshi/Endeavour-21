package com.ecell.end_eavour.speakers;

import androidx.cardview.widget.CardView;

public interface CardAdapterSpeaker {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
