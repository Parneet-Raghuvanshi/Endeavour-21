package com.ecell.end_eavour.events.faq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;

import java.util.ArrayList;
import java.util.List;

public class EventsFaq_Adapter extends RecyclerView.Adapter<EventsFaq_Adapter.EventsFaq_Viewholder> {

    List<EventsFaq_Model> eventsFaqModels = new ArrayList<>();

    public EventsFaq_Adapter(List<EventsFaq_Model> eventsFaqModels) {
        this.eventsFaqModels = eventsFaqModels;
    }

    @NonNull
    @Override
    public EventsFaq_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_faq, parent, false);

        return new EventsFaq_Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsFaq_Viewholder holder, int position) {
        holder.faqQuestion.setText(eventsFaqModels.get(position).getQuestion());
        holder.faqAnswer.setText(eventsFaqModels.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return eventsFaqModels.size();
    }

    public class EventsFaq_Viewholder extends RecyclerView.ViewHolder {
        TextView faqQuestion;
        TextView faqAnswer;

    public EventsFaq_Viewholder(@NonNull View itemView) {
            super(itemView);
            faqQuestion = itemView.findViewById(R.id.faq_ques);
            faqAnswer = itemView.findViewById(R.id.faq_ans);
        }
    }

}
