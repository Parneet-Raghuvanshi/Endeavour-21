package com.ecell.end_eavour.events.faq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.services.MyApplication;

public class EventsFaq extends Fragment {

    RecyclerView recyclerView;
    EventsFaq_Adapter adapter;

    public EventsFaq() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_faq, container, false);

        Bundle bundle = getArguments();
        String faqId = bundle.getString( "eventId" );

        recyclerView = view.findViewById(R.id.rv_faq_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        adapter = new EventsFaq_Adapter(((MyApplication) getActivity().getApplication()).getEventsFaqModels());

        recyclerView.setAdapter(adapter);

        return view;
    }
}