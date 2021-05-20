package com.ecell.end_eavour.events.faq;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsFaq extends Fragment {

    RecyclerView recyclerView;
    EventsFaq_Adapter adapter;
    List<EventsFaq_Model> eventsFaqModels;

    public EventsFaq() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_faq, container, false);

        Bundle bundle = getArguments();
        String faqId = bundle.getString( "eventId" );

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("eventId").equalTo(faqId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                    String eventNametemp = dataSnapshot1.child("eventName").getValue().toString();
                    Log.i("String Event Name :: ",eventNametemp);

                    eventsFaqModels = new ArrayList<>();
                    for (DataSnapshot dataSnapshotFaq : dataSnapshot1.child("eventFaq").getChildren()){
                        EventsFaq_Model eventsFaqModel = dataSnapshotFaq.getValue(EventsFaq_Model.class);
                        eventsFaqModels.add(eventsFaqModel);
                    }
                    adapter = new EventsFaq_Adapter(eventsFaqModels);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = view.findViewById(R.id.rv_faq_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return view;
    }
}