package com.ecell.end_eavour.events.ragstoriches;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.BucketRecyclerView;
import com.ecell.end_eavour.services.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuctionHandler extends AppCompatActivity {

    TextView stockName;
    BucketRecyclerView recyclerView;
    FirebaseRecyclerOptions<Stock_Model> options;
    FirebaseRecyclerAdapter<Stock_Model,Stock_Viewholder> adapter;
    DatabaseReference databaseReference;
    View progressbar;
    Button bidBtn;
    String highestBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auction_handler);

        stockName = findViewById(R.id.stock_name);
        progressbar = findViewById(R.id.stock_progress);
        bidBtn = findViewById(R.id.bid_btn);
        bidBtn.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.rv_stocks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.showIfEmpty(progressbar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("RagPortal");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Stock_Model>().setQuery(databaseReference,Stock_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Stock_Model, Stock_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Stock_Viewholder holder, int position,Stock_Model model) {
                holder.Desc.setText(model.getStockDesc());
                stockName.setText(model.getStockName());
                holder.currentBid.setText(model.getHighestBid());
                holder.stockOwner.setText(model.getStockHolder());
                Glide.with(AuctionHandler.this).load(model.getImage()).into(holder.stockImage);
                highestBid = model.getHighestBid();

                bidBtn.setText("Bid + "+model.getBidrate());
                bidBtn.setVisibility(View.VISIBLE);
            }

            @NonNull
            @Override
            public Stock_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Stock_Viewholder(LayoutInflater.from(AuctionHandler.this).inflate(R.layout.rags_card,parent,false));
            }
        };

        bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbref_update = FirebaseDatabase.getInstance().getReference().child("RagProfile");
                dbref_update.keepSynced(true);
                dbref_update.orderByChild("userId").equalTo(((MyApplication) getApplication()).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            RagProfile model = dataSnapshot.getValue(RagProfile.class);

                            String credit = model.getCredit();
                            int total = Integer.parseInt(highestBid)+50;
                            if (Integer.parseInt(credit)>=total){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RegPortal").child("0");
                                databaseReference.keepSynced(true);
                                databaseReference.child("highestBid").setValue(String.valueOf(total));
                                databaseReference.child("stockHolder").setValue(((MyApplication) getApplication()).getUserName());
                                databaseReference.child("stockHolderId").setValue(((MyApplication) getApplication()).getUserId());
                            }
                            else {
                                Toast.makeText(AuctionHandler.this,"Insufficient Credits!",Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}