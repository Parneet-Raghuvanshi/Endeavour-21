package com.ecell.end_eavour.events.ragstoriches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

public class Stock_Viewholder extends RecyclerView.ViewHolder {

    TextView currentBid;
    TextView Desc;
    TextView stockOwner;
    ImageView stockImage;

    public Stock_Viewholder(@NonNull View itemView) {
        super(itemView);

        currentBid = itemView.findViewById(R.id.current_bid);
        Desc = itemView.findViewById(R.id.description);
        stockImage = itemView.findViewById(R.id.stock_image);
        stockOwner = itemView.findViewById(R.id.stock_owner);
    }
}
