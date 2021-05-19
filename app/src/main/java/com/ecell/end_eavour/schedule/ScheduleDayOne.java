package com.ecell.end_eavour.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.BucketRecyclerView;
import com.ecell.end_eavour.events.Events;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleDayOne extends Fragment {

    LinearLayout switchToggle,comingSoon;
    TextView dateDayOne,dateDayTwo;

    BucketRecyclerView recyclerView;
    FirebaseRecyclerOptions<Schedule_Model> options;
    FirebaseRecyclerAdapter<Schedule_Model,Schedule_Viewholder> adapter;
    DatabaseReference databaseReference;

    public ScheduleDayOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_day_one, container, false);

        switchToggle = view.findViewById(R.id.switch_toggle);
        comingSoon = view.findViewById(R.id.coming_soon);

        dateDayOne = view.findViewById(R.id.sh_dayOneDate);
        dateDayTwo = view.findViewById(R.id.sh_dayTwoDate);

        setDates();

        switchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_shedule,new ScheduleDayTwo());
                fragmentTransaction.commit();
            }
        });

        recyclerView = view.findViewById(R.id.rv_schedule_dayone);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.showIfEmpty(comingSoon);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("schedule").child("dayone");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Schedule_Model>().setQuery(databaseReference,Schedule_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Schedule_Model, Schedule_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Schedule_Viewholder holder, int position, @NonNull Schedule_Model model) {
                Glide.with(view).load(model.getEventIcon()).into(holder.eventIcon);
                holder.eventTitle.setText(model.getEventTitle());
                holder.eventDesc.setText(model.getEventDesc());
                holder.eventVenue.setText(model.getEventVenue());
                holder.eventTime.setText(model.getEventTime());

                holder.gotoEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Events.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                holder.expanderLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.expand == false){
                            holder.expandableLayout.setVisibility(View.VISIBLE);
                            holder.expand = true;
                        }
                        else {
                            holder.expandableLayout.setVisibility(View.GONE);
                            holder.expand = false;
                        }
                    }
                });
            }

            @NonNull
            @Override
            public Schedule_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Schedule_Viewholder(LayoutInflater.from(view.getContext()).inflate(R.layout.layout_schedule,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        //---( Swipe Gesture for Main Fragment Layout )---//
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                            {
                                //open right side
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.container_shedule,new ScheduleDayTwo());
                                fragmentTransaction.commit();

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                            {
                                //open left side

                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return view;
    }

    private void setDates() {
        DatabaseReference databaseReferencenot = FirebaseDatabase.getInstance().getReference().child("shDate");
        databaseReferencenot.keepSynced(true);
        databaseReferencenot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Schedule_CheckDate schedule_checkDate = dataSnapshot.getValue(Schedule_CheckDate.class);
                if (schedule_checkDate != null)
                {
                    dateDayOne.setText(schedule_checkDate.getDayOneDate());
                    dateDayTwo.setText(schedule_checkDate.getDayTwoDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}