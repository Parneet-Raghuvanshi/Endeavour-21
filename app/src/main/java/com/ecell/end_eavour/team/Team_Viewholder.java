package com.ecell.end_eavour.team;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Team_Viewholder extends RecyclerView.ViewHolder {

    TextView name;
    TextView domain;
    TextView desig;
    de.hdodenhof.circleimageview.CircleImageView imguri;
    boolean expand;
    public LinearLayout expandableLayout;
    public RelativeLayout relativeLayout;
    public LinearLayout googleProfile;
    public LinearLayout linkedinProfile;
    public LinearLayout facebookProfile;

    public Team_Viewholder(@NonNull View itemView) {
        super(itemView);

        expand = false;
        name = itemView.findViewById(R.id.tv_team_name);
        domain = itemView.findViewById(R.id.tv_team_domain);
        desig = itemView.findViewById(R.id.tv_team_desig);
        imguri = itemView.findViewById(R.id.image_team);
        expandableLayout = itemView.findViewById(R.id.expandable_layout);
        relativeLayout = itemView.findViewById(R.id.layout_team_card);
        googleProfile = itemView.findViewById(R.id.google_profile);
        linkedinProfile = itemView.findViewById(R.id.linkedin_profile);
        facebookProfile = itemView.findViewById(R.id.facebook_profile);
    }
}
