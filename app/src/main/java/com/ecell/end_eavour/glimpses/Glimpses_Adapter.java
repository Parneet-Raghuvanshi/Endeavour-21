package com.ecell.end_eavour.glimpses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

import java.util.List;

public class Glimpses_Adapter extends RecyclerView.Adapter<Glimpses_Adapter.Glimpses_Viewholder> {

    private Context ctx;
    private List<Glimpses> glimpses;

    public Glimpses_Adapter(Context ctx, List<Glimpses> glimpses) {
        this.ctx = ctx;
        this.glimpses = glimpses;
    }

    @NonNull
    @Override
    public Glimpses_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.layout_glimpses_card,null);
        return new Glimpses_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Glimpses_Viewholder holder, final int position) {
        final Glimpses glimpse = glimpses.get(position);

        holder.glimpimg.setImageDrawable(ctx.getResources().getDrawable(glimpse.getImguri()));

        holder.glimpimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ctx,"clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx,GlimpsesDetail.class);
                intent.putExtra("imguri",glimpse.getImguri());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return glimpses.size();
    }

    class Glimpses_Viewholder extends RecyclerView.ViewHolder {

        ImageView glimpimg;

        public Glimpses_Viewholder(@NonNull View itemView) {
            super(itemView);
            glimpimg = itemView.findViewById(R.id.glimp_image);
        }
    }
}
