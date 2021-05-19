package com.ecell.end_eavour.notifications;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.BucketRecyclerView;
import com.ecell.end_eavour.services.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Notifications extends AppCompatActivity {

    private BucketRecyclerView recyclerView;
    private FirebaseRecyclerOptions<Notifications_Model> options;
    private FirebaseRecyclerAdapter<Notifications_Model,Notifications_ViewHolder> adapter;
    private DatabaseReference databaseReference;
    private View noNewNotifications;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notifications);

        noNewNotifications = findViewById(R.id.no_new_notifications);
        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.noti_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.showIfEmpty(noNewNotifications);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(((MyApplication) getApplication()).getUserId());
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Notifications_Model>().setQuery(databaseReference,Notifications_Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Notifications_Model, Notifications_ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Notifications_ViewHolder holder, int position, @NonNull Notifications_Model model) {

                holder.title.setText(model.getTitle());
                holder.description.setText(model.getBody());
                holder.date.setText(model.getDate());
            }

            @NonNull
            @Override
            public Notifications_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Notifications_ViewHolder(LayoutInflater.from(Notifications.this).inflate(R.layout.layout_notification,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Toast.makeText(Notifications.this,"swiped",Toast.LENGTH_SHORT).show();
                int id = viewHolder.getAdapterPosition();
                String notiid = adapter.getItem(id).getId();

                FirebaseDatabase.getInstance().getReference().child("notification").child(((MyApplication) getApplication()).getUserId()).child(notiid).removeValue();
                //Toast.makeText(Notifications.this,"Sucess "+notiid,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(Notifications.this,c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.ic_white_delete_forever_24)
                        .addSwipeRightBackgroundColor(getResources().getColor(R.color.main_color))
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Notifications.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}