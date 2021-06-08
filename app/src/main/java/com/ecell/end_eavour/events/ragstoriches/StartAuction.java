package com.ecell.end_eavour.events.ragstoriches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.services.MyApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartAuction extends AppCompatActivity {

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_auction);

        continueBtn = findViewById(R.id.continue_btn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //---( Making his Auction Profile )---//
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("RagProfile").child(((MyApplication) getApplication()).getUserId());
                databaseReference.keepSynced(true);
                databaseReference.child("creddit").setValue("500");
                databaseReference.child("userId").setValue(((MyApplication) getApplication()).getUserId());
                databaseReference.child("userName").setValue(((MyApplication) getApplication()).getUserName());
                //---( After Verification Start Auction )---//
                Intent intent = new Intent(StartAuction.this,AuctionHandler.class);
                startActivity(intent);
                finish();
            }
        });
    }
}