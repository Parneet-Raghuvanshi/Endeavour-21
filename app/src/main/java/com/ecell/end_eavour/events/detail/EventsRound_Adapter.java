package com.ecell.end_eavour.events.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

import java.util.ArrayList;
import java.util.List;

public class EventsRound_Adapter extends RecyclerView.Adapter<EventsRound_Adapter.EventsRound_Viewholder> {

    List<EventsRound_Model> eventsRoundModels = new ArrayList<>();

    public EventsRound_Adapter(List<EventsRound_Model> eventsRoundModels) {
        this.eventsRoundModels = eventsRoundModels;
    }

    @NonNull
    @Override
    public EventsRound_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_rounds,parent,false);
        return new EventsRound_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRound_Viewholder holder, int position) {
        holder.titleText.setText(eventsRoundModels.get(position).getTitle()+" ("+eventsRoundModels.get(position).getHeading()+")");
        holder.contentText.setText("\u25CF "+eventsRoundModels.get(position).getContent().replace("--","\n\u25CF "));
    }

    @Override
    public int getItemCount() {
        return eventsRoundModels.size();
    }

    public class EventsRound_Viewholder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView contentText;

        public EventsRound_Viewholder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.event_rounds_head);
            contentText = itemView.findViewById(R.id.event_rounds);
        }
    }
}
