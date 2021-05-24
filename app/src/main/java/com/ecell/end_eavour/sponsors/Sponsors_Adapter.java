package com.ecell.end_eavour.sponsors;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;

import java.util.List;

public class Sponsors_Adapter extends RecyclerView.Adapter<Sponsors_Adapter.Sponsors_Viewholder> {

    private Context context;
    private List<Sponsors_Model> sponsorsModels;

    public Sponsors_Adapter(Context context, List<Sponsors_Model> sponsorsModels) {
        this.context = context;
        this.sponsorsModels = sponsorsModels;
    }

    @NonNull
    @Override
    public Sponsors_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Sponsors_Viewholder(LayoutInflater.from(context).inflate(R.layout.layout_sponsors,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Sponsors_Viewholder holder, int position) {

        holder.sponsorName.setText(sponsorsModels.get(position).getSponsorName());
        Glide.with(context).load(sponsorsModels.get(position).getImageSponsor()).into(holder.sponsorImage);

        holder.sponsorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(sponsorsModels.get(position).getSponsorLink());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sponsorsModels.size();
    }

    public static final class Sponsors_Viewholder extends RecyclerView.ViewHolder {

        TextView sponsorName;
        ImageView sponsorImage;
        RelativeLayout sponsorCard;

        public Sponsors_Viewholder(@NonNull View itemView) {
            super(itemView);

            sponsorName = itemView.findViewById(R.id.sponsor_name_text);
            sponsorImage = itemView.findViewById(R.id.sponsor_image_view);
            sponsorCard = itemView.findViewById(R.id.main_card_context);
        }
    }
}
