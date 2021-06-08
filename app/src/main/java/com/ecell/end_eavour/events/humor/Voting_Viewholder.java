package com.ecell.end_eavour.events.humor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Voting_Viewholder extends RecyclerView.ViewHolder {

    public TextView teamName;
    public TextView upVote;
    public ImageView imageView;

    public Voting_Viewholder(@NonNull View itemView) {
        super(itemView);

        teamName = itemView.findViewById(R.id.team_name_voting);
        upVote = itemView.findViewById(R.id.upvote);
        imageView = itemView.findViewById(R.id.meme_img);
    }
}
