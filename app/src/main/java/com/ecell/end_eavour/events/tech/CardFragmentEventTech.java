package com.ecell.end_eavour.events.tech;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.events.CardAdapterEvent;
import com.ecell.end_eavour.events.detail.Events_Detail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CardFragmentEventTech extends Fragment {

    private CardView cardView;
    CustomSnacks customSnacks;
    String eventId;

    public CardFragmentEventTech() {

    }

    public static Fragment getInstance(int position) {
        CardFragmentEventTech f = new CardFragmentEventTech();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_event_main, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapterEvent.MAX_ELEVATION_FACTOR);

        customSnacks = new CustomSnacks();

        final Bundle bundle = new Bundle();

        TextView eventName = (TextView) view.findViewById(R.id.event_name);
        ImageView eventIcon = (ImageView) view.findViewById(R.id.event_icon);
        TextView eventDesc = (TextView)view.findViewById(R.id.event_desc);
        CardView cardView = (CardView) view.findViewById(R.id.event_card_clicker);

        String position = String.format("%d", getArguments().getInt("position"));

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReference1.keepSynced(true);
        databaseReference1.orderByChild("id").equalTo("TECH"+position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String eventNametemp = dataSnapshot1.child("eventName").getValue().toString();
                    Log.i("String Event Name :: ",eventNametemp);

                    bundle.putString("eventName",dataSnapshot1.child("eventName").getValue().toString());
                    bundle.putString("eventDesc",dataSnapshot1.child("eventDesc").getValue().toString());
                    bundle.putString("eventBenefit",dataSnapshot1.child("eventBenefit").getValue().toString());
                    bundle.putString("eventRules",dataSnapshot1.child("eventRules").getValue().toString());
                    bundle.putString("eventStructure",dataSnapshot1.child("eventStructure").getValue().toString());
                    bundle.putString("eventIcon",dataSnapshot1.child("eventIcon").getValue().toString());
                    bundle.putString("eventId",dataSnapshot1.child("eventId").getValue().toString());
                    bundle.putString("eventImage",dataSnapshot1.child("eventImage").getValue().toString());
                    bundle.putString("isOpen",dataSnapshot1.child("isOpen").getValue().toString());
                    eventId = dataSnapshot1.child("eventId").getValue().toString();
                    bundle.putString("eventType",dataSnapshot1.child("id").getValue().toString());

                    eventName.setText(dataSnapshot1.child("eventName").getValue().toString());
                    eventDesc.setText(dataSnapshot1.child("eventDesc").getValue().toString().replace("--"," "));
                    Glide.with(view).load(dataSnapshot1.child("eventIcon").getValue().toString()).into(eventIcon);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle.getString("isOpen").equals("true")){
                    final FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Events_Detail eventsDetail = new Events_Detail();
                    eventsDetail.setArguments(bundle);
                    fragmentTransaction.add(R.id.event_detail_container,eventsDetail,"event_detail");
                    fragmentTransaction.commit();
                    getActivity().findViewById(R.id.event_detail_container).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.layout_events_catalog).setVisibility(View.GONE);
                }
                else {
                    customSnacks.infoSnack(view,"Registrations not open, Try again after some time!");
                }
            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}
