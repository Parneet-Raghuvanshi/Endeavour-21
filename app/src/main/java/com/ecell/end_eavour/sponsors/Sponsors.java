package com.ecell.end_eavour.sponsors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragment;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentOne;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentTwo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sponsors extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    ImageView backBtn;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Sponsors_Model> options;
    private FirebaseRecyclerAdapter<Sponsors_Model,Sponsors_Viewholder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sponsors);

        setUpBottomAppBar();

        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_sponsor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("sponsors");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Sponsors_Model>().setQuery(databaseReference,Sponsors_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Sponsors_Model, Sponsors_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Sponsors_Viewholder holder, int position, @NonNull Sponsors_Model model) {
                holder.sponsort.setText(model.getSponsorName());
                holder.sponsorC.setText(model.getSponsorCategory());
                Glide.with(getApplicationContext()).load(model.getImageSponsor()).into(holder.sponsorImage);

                holder.layoutSponsorCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(model.getSponsorLink());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public Sponsors_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Sponsors_Viewholder(LayoutInflater.from(Sponsors.this).inflate(R.layout.layout_sponsors,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     * set up Bottom Bar
     */
    private void setUpBottomAppBar() {
        //find id
        bottomAppBar = findViewById(R.id.bar);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(bottomAppBar);

        //click event over Bottom bar menu item
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_glimpses:
                        BottomSheetDialogFragment bottomSheetDialogFragmentTwo = BottomSheetNavigationFragmentTwo.newInstance();
                        bottomSheetDialogFragmentTwo.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment Two");
                        break;
                    case R.id.menu_about:
                        BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragmentOne.newInstance();
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment One");
                        break;
                }
                return false;
            }
        });
        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });
    }

    //Inflate menu to bottom bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_glimpses:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( Sponsors.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}