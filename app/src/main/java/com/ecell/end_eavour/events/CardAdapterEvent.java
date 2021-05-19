package com.ecell.end_eavour.events;

import androidx.cardview.widget.CardView;

public interface CardAdapterEvent {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
