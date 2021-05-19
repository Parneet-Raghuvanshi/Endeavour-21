package com.ecell.end_eavour.speakers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

public class CardFragmentSpeaker extends Fragment {

    private CardView cardView;
    private TextView title,desc;

    public static Fragment getInstance(int position) {
        CardFragmentSpeaker f = new CardFragmentSpeaker();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapterSpeaker.MAX_ELEVATION_FACTOR);

        title = (TextView)view.findViewById(R.id.speaker_title_details);
        desc = (TextView)view.findViewById(R.id.speaker_desc_detail);

        String position = String.format("%d", getArguments().getInt("position"));

        final TextView title_main = (TextView) view.findViewById(R.id.title_speak);
        final TextView description = (TextView)view.findViewById(R.id.desc_speak);
        final de.hdodenhof.circleimageview.CircleImageView image_p = (de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.image_speaker);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("speakers");
        databaseReference1.orderByChild("id").equalTo(position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Speaker_detail speaker_detail = dataSnapshot1.getValue(Speaker_detail.class);

                    title_main.setText(speaker_detail.getName());
                    description.setText(speaker_detail.getDesc());
                    Glide.with(getContext()).load(speaker_detail.getImguri()).into(image_p);

                    title.setText(speaker_detail.getT());
                    desc.setText(speaker_detail.getD());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}
