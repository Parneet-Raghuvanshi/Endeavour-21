package com.ecell.end_eavour.team;

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
import com.ecell.end_eavour.fab.FABAnimation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Team extends AppCompatActivity {

    //---( BOTTOM BAR VARIABLE )---//
    private BottomAppBar bottomAppBar;
    FloatingActionButton fabToggler,fabDeveloper,fabEndvr;
    boolean isFabOpen = false;

    //---( MAIN VARIABLES )---//
    ImageView backBtn;
    RecyclerView recyclerView;

    //---( FIREBASE VARIABLES )---//
    FirebaseRecyclerOptions<Team_Model> options;
    FirebaseRecyclerAdapter<Team_Model,Team_Viewholder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //---( SETTING VIEW )---//
        setContentView(R.layout.activity_team);

        //---( SETTING BOTTOM BAR )---//
        setUpBottomAppBar();

        //---( SETTING FAB BUTTONS ALL )---//
        fabToggler = findViewById(R.id.fab_toogler);
        fabDeveloper = findViewById(R.id.fab_developer);
        fabEndvr = findViewById(R.id.fab_endvr);

        FABAnimation.init( fabDeveloper );
        FABAnimation.init( fabEndvr );

        fabToggler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
                animateFAB();
            }
        } );
        fabDeveloper.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Team.this, Developer.class);
                startActivity(intent);
            }
        } );
        fabEndvr.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Team.this, AboutEndvr.class);
                startActivity(intent);
            }
        } );

        //---( MAIN TEAM WORK ( FIREBASE ))---//
        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_teammain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("team");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Team_Model>().setQuery(databaseReference,Team_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Team_Model, Team_Viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Team_Viewholder holder, int position, @NonNull Team_Model model) {
                holder.name.setText(model.getName());
                holder.desig.setText(model.getDesig());
                holder.domain.setText(model.getDomain());
                Glide.with(getApplicationContext()).load(model.imguri).into(holder.imguri);

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                holder.googleProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String to = model.getGoogleProfile();
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});

                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email Service :"));
                    }
                });

                holder.linkedinProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(model.getLinkedinProfile());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });

                holder.facebookProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(model.getFacebookProfile());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public Team_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Team_Viewholder(LayoutInflater.from(Team.this).inflate(R.layout.layout_team,parent,false));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    //---( FAB ANIMATION )---//
    private void animateFAB(){
        isFabOpen = FABAnimation.rotateFab( fabToggler,!isFabOpen );
        if(isFabOpen){
            FABAnimation.fabopen( fabDeveloper );
            FABAnimation.fabopen( fabEndvr );
        }
        else{
            FABAnimation.fabclose( fabDeveloper );
            FABAnimation.fabclose( fabEndvr );
        }
    }

    //---( SETTING BOTTOM BAR FUNCTION )---//
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
    /**
     * method to toggle fab mode
     *
     * @param view
     */
    public void toggleFabMode(View view) {
        //check the fab alignment mode and toggle accordingly
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        } else {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent( Team.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}