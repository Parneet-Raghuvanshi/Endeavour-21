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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;

import java.util.List;

public class SponsorsMain_Adapter extends RecyclerView.Adapter<SponsorsMain_Adapter.SponsorsMain_Viewholder> {

    private Context context;
    private List<SponsorsMain_Model> sponsorsMainModels;

    public SponsorsMain_Adapter(Context context, List<SponsorsMain_Model> sponsorsMainModels) {
        this.context = context;
        this.sponsorsMainModels = sponsorsMainModels;
    }

    @NonNull
    @Override
    public SponsorsMain_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SponsorsMain_Viewholder(LayoutInflater.from(context).inflate(R.layout.layout_sponsors_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorsMain_Viewholder holder, int position) {

        holder.sponsorsCategory.setText(sponsorsMainModels.get(position).getName());

        if (sponsorsMainModels.get(position).getSponsorsModels().size()==1){
            holder.sponsorsRecyclerview.setVisibility(View.GONE);
            holder.singleCard.setVisibility(View.VISIBLE);
            holder.sponsorName.setText(sponsorsMainModels.get(position).getSponsorsModels().get(0).getSponsorName());
            Glide.with(context).load(sponsorsMainModels.get(position).getSponsorsModels().get(0).getImageSponsor()).into(holder.sponsorImage);
            holder.singleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(sponsorsMainModels.get(position).getSponsorsModels().get(0).getSponsorLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.singleCard.setVisibility(View.GONE);
            holder.sponsorsRecyclerview.setVisibility(View.VISIBLE);
            Sponsors_Adapter sponsors_adapter = new Sponsors_Adapter(context,sponsorsMainModels.get(position).getSponsorsModels());
            holder.sponsorsRecyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
            holder.sponsorsRecyclerview.setHasFixedSize(true);
            holder.sponsorsRecyclerview.setAdapter(sponsors_adapter);
        }
    }

    @Override
    public int getItemCount() {
        return sponsorsMainModels.size();
    }

    public static final class SponsorsMain_Viewholder extends RecyclerView.ViewHolder {

        TextView sponsorsCategory;
        RecyclerView sponsorsRecyclerview;
        RelativeLayout singleCard;
        ImageView sponsorImage;
        TextView sponsorName;

        public SponsorsMain_Viewholder(@NonNull View itemView) {
            super(itemView);

            sponsorsCategory = itemView.findViewById(R.id.sponsor_category_text);
            sponsorsRecyclerview = itemView.findViewById(R.id.recycler_view_sponsor);
            singleCard = itemView.findViewById(R.id.rv_single_card);
            sponsorImage = itemView.findViewById(R.id.sponsor_image_view);
            sponsorName = itemView.findViewById(R.id.sponsor_name_text);
        }
    }
}
