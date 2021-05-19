package com.ecell.end_eavour.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.events.corp.CardFragmentPagerAdapterEventEventCorp;
import com.ecell.end_eavour.events.fun.CardFragmentPagerAdapterEventEventFun;
import com.ecell.end_eavour.events.registration.GetEventPass;
import com.ecell.end_eavour.events.tech.CardFragmentPagerAdapterEventEventTech;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragment;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentOne;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentTwo;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Events extends AppCompatActivity implements PaymentResultWithDataListener {

    private BottomAppBar bottomAppBar;
    ImageView backBtn;
    FrameLayout eventDetailContainer;
    NestedScrollView eventCatalog;
    CustomSnacks customSnacks;
    CardView getPassCard,passActivated,discordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_events);

        setUpBottomAppBar();

        backBtn = findViewById(R.id.back);
        eventCatalog = findViewById(R.id.layout_events_catalog);
        eventDetailContainer = findViewById(R.id.event_detail_container);
        getPassCard = findViewById(R.id.get_pass_card);
        getPassCard.setVisibility(View.GONE);
        passActivated = findViewById(R.id.pass_activated);
        passActivated.setVisibility(View.GONE);
        discordBtn = findViewById(R.id.discord_btn);
        customSnacks = new CustomSnacks();

        checkPassAvaliability();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getPassCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Events.this, GetEventPass.class);
                startActivity(intent);
                finish();
            }
        });

        discordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://discord.com/invite/KwSKQb62Hv");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        //---(  CORP EVENT )---//
        final DatabaseReference databaseReferenceCorp = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReferenceCorp.keepSynced(true);
        databaseReferenceCorp.orderByChild("isLaunched").equalTo("CORP#true").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){
                    long j = dataSnapshot.getChildrenCount();
                    int i = (int)j;
                    Log.i("CORP :: ", String.valueOf(i));
                    final ViewPager viewPagerCorp = (ViewPager) findViewById(R.id.viewPager_corp);
                    StartProcessCorp(viewPagerCorp,i);
                }
                else {
                    findViewById(R.id.viewPager_corp).setVisibility(View.INVISIBLE);
                    findViewById(R.id.comingsoon_layout_corp).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //---(  FUN EVENT )---//
        final DatabaseReference databaseReferenceFun = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReferenceFun.keepSynced(true);
        databaseReferenceFun.orderByChild("isLaunched").equalTo("FUN#true").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){
                    long j = dataSnapshot.getChildrenCount();
                    int i = (int)j;
                    Log.i("FUN :: ", String.valueOf(i));
                    final ViewPager viewPagerFun = (ViewPager) findViewById(R.id.viewPager_fun);
                    StartProcessFun(viewPagerFun,i);
                }
                else {
                    findViewById(R.id.viewPager_fun).setVisibility(View.INVISIBLE);
                    findViewById(R.id.comingsoon_layout_fun).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //---(  TECH EVENT )---//
        final DatabaseReference databaseReferenceTech = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReferenceTech.keepSynced(true);
        databaseReferenceTech.orderByChild("isLaunched").equalTo("TECH#true").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){
                    long j = dataSnapshot.getChildrenCount();
                    int i = (int)j;
                    Log.i("TECH :: ", String.valueOf(i));
                    final ViewPager viewPagerTech = (ViewPager) findViewById(R.id.viewPager_tech);
                    StartProcessTech(viewPagerTech,i);
                }
                else {
                    findViewById(R.id.viewPager_tech).setVisibility(View.INVISIBLE);
                    findViewById(R.id.comingsoon_layout_tech).setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //---( Event Pass Avaliability )---//
    private void checkPassAvaliability() {
        View view = findViewById(android.R.id.content);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyUserTokenApi(((MyApplication) getApplication()).getUserToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject mainjsonObject=null;
                    try {
                        mainjsonObject = new JSONObject(response.body().string());

                        JSONObject jsonObject = mainjsonObject.optJSONObject("userData");

                        Log.i("STATUS FAIL :: ",jsonObject.toString());
                        //---( To toogle get pass button )---//
                        boolean eventPass = jsonObject.optBoolean("eventPass");
                        if (eventPass){
                            ((MyApplication) getApplication()).setGetPassTrigger(false);
                            getPassCard.setVisibility(View.GONE);
                            passActivated.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ((MyApplication) getApplication()).setGetPassTrigger(true);
                            getPassCard.setVisibility(View.VISIBLE);
                            passActivated.setVisibility(View.GONE);
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        Log.i("STATUS FAIL :: ",jsonObject.toString());
                        customSnacks.failSnack(view,"Something went wrong, Please Refresh!");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR VERIFYUSERTOKEN "," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Something went wrong, Please Refresh!");
            }
        });
    }

    //----( FOR CORP EVENTS )---///
    public void StartProcessCorp(ViewPager viewPagerCorp,int i){
        CardFragmentPagerAdapterEventEventCorp pagerAdapterCorp = new CardFragmentPagerAdapterEventEventCorp(getSupportFragmentManager(), dpToPixels(2, this),i);
        ShadowTransformerEvent shadowTransformerEventCorp = new ShadowTransformerEvent(viewPagerCorp, pagerAdapterCorp);
        shadowTransformerEventCorp.enableScaling(true);

        viewPagerCorp.setAdapter(pagerAdapterCorp);
        viewPagerCorp.setPageTransformer(false, shadowTransformerEventCorp);
        viewPagerCorp.setOffscreenPageLimit(3);
    }

    //----( FOR FUN EVENTS )---///
    public void StartProcessFun(ViewPager viewPagerFun,int i){
        CardFragmentPagerAdapterEventEventFun pagerAdapterFun = new CardFragmentPagerAdapterEventEventFun(getSupportFragmentManager(), dpToPixels(2, this),i);
        ShadowTransformerEvent shadowTransformerEventFun = new ShadowTransformerEvent(viewPagerFun, pagerAdapterFun);
        shadowTransformerEventFun.enableScaling(true);

        viewPagerFun.setAdapter(pagerAdapterFun);
        viewPagerFun.setPageTransformer(false, shadowTransformerEventFun);
        viewPagerFun.setOffscreenPageLimit(3);
    }

    //----( FOR TECH EVENTS )---///
    public void StartProcessTech(ViewPager viewPagerTech,int i){
        CardFragmentPagerAdapterEventEventTech pagerAdapterTech = new CardFragmentPagerAdapterEventEventTech(getSupportFragmentManager(), dpToPixels(2, this),i);
        ShadowTransformerEvent shadowTransformerEventTech = new ShadowTransformerEvent(viewPagerTech, pagerAdapterTech);
        shadowTransformerEventTech.enableScaling(true);

        viewPagerTech.setAdapter(pagerAdapterTech);
        viewPagerTech.setPageTransformer(false, shadowTransformerEventTech);
        viewPagerTech.setOffscreenPageLimit(3);
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment faqFragment = getSupportFragmentManager().findFragmentByTag("event_faq");
        Fragment detailFragment = getSupportFragmentManager().findFragmentByTag("event_detail");

        if (faqFragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
        else{
            if (detailFragment!=null){
                transaction.remove(detailFragment);
                transaction.commit();
                eventDetailContainer.setVisibility(View.GONE);
                eventCatalog.setVisibility(View.VISIBLE);
            }
            else {
                Intent intent = new Intent( Events.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        onBackPressed();
        Log.i("SUCCESS PAY :: ",s+" Payment Id ");
        View view = findViewById(android.R.id.content);
        customSnacks.successSnack(view,"Successfully Registered in Internship Fair, Check your email!");
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        onBackPressed();
        Log.i("FAIL PAY :: ",s+ " Payment Id ");
        View view = findViewById(android.R.id.content);
        customSnacks.failSnack(view,"Payment Unsuccessful, Try Again!");
    }
}