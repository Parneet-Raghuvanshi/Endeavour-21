package com.ecell.end_eavour.schedule;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Schedule_Viewholder extends RecyclerView.ViewHolder {

    TextView eventTitle;
    TextView eventDesc;
    TextView eventVenue;
    TextView eventTime;
    ImageView eventIcon;
    boolean expand;
    RelativeLayout expanderLayout;
    RelativeLayout expandableLayout;
    TextView gotoEvent;
    CardView cardView;

    public Schedule_Viewholder(@NonNull View itemView) {
        super(itemView);

        expand = false;
        eventTitle = itemView.findViewById(R.id.sh_titleEvent);
        eventDesc = itemView.findViewById(R.id.sh_descEvent);
        eventVenue = itemView.findViewById(R.id.sh_venueEvent);
        eventTime = itemView.findViewById(R.id.sh_timeEvent);
        eventIcon = itemView.findViewById(R.id.sh_iconEvent);
        expanderLayout = itemView.findViewById(R.id.relativelayout_expander);
        expandableLayout = itemView.findViewById(R.id.layout_expandable);
        gotoEvent = itemView.findViewById(R.id.sh_gotoEvent);
        cardView = itemView.findViewById(R.id.cardlayout);
    }
}
