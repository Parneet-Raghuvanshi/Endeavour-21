package com.ecell.end_eavour.notifications;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Notifications_ViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView description;
    TextView date;
    LinearLayout notificationTrigger;

    public Notifications_ViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.tv_noti_title);
        description = itemView.findViewById(R.id.tv_noti_desc);
        date =itemView.findViewById( R.id.date );
        notificationTrigger = itemView.findViewById(R.id.notification_trigger);
    }
}
