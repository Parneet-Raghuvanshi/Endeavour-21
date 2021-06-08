package com.ecell.end_eavour.events.humor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.events.Events;
import com.ecell.end_eavour.services.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Voting extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Voting_Model> options;
    FirebaseRecyclerAdapter<Voting_Model,Voting_Viewholder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voting);

        recyclerView = findViewById(R.id.rv_voting);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("VotingPortal");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Voting_Model>().setQuery(databaseReference,Voting_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Voting_Model, Voting_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Voting_Viewholder holder, int position, @NonNull Voting_Model model) {
                holder.teamName.setText(model.getTeamName());
                Glide.with(getApplicationContext()).load(model.getImage()).into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent viewFull = new Intent(Voting.this,VoteDetail.class);
                        viewFull.putExtra("imageuri",model.getImage());
                        startActivity(viewFull);
                    }
                });
                holder.upVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Voting.this);
                        builder.setTitle("Voting");
                        builder.setIcon(R.mipmap.ic_launcher_round);
                        builder.setMessage("Please Confirm Your Vote\nYou can not change your vote once confirmed")
                                .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("VoteStatus").child(((MyApplication) getApplication()).getUserId());
                                        databaseReference.keepSynced(true);
                                        databaseReference.child("votingStatus").setValue("voted");
                                        databaseReference.child("userId").setValue(((MyApplication) getApplication()).getUserId());
                                        databaseReference.child("voteId").setValue(model.getVoteId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Voting.this,"Vote Updated Successfully",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        int i = model.getTotalVotes() +1;
                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("VotingPortal");
                                        databaseReference1.keepSynced(true);
                                        databaseReference1.child(model.getVoteId()).child("totalVotes").setValue(i);
                                        onBackPressed();
                                    }
                                })
                                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }

            @NonNull
            @Override
            public Voting_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Voting_Viewholder(LayoutInflater.from(Voting.this).inflate(R.layout.card_for_voting,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Voting.this, Events.class);
        startActivity(intent);
        finish();
    }
}