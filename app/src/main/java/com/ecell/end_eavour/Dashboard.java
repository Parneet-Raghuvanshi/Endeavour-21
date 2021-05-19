package com.ecell.end_eavour;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.contactus.Contactus;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.events.Events;
import com.ecell.end_eavour.login.Login;
import com.ecell.end_eavour.notifications.Notification_Dot;
import com.ecell.end_eavour.notifications.Notifications;
import com.ecell.end_eavour.schedule.Schedule;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.ecell.end_eavour.speakers.Speakers;
import com.ecell.end_eavour.sponsors.Sponsors;
import com.ecell.end_eavour.team.Team;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    //---( TILES LAYOUT VARIABLES )---//
    LinearLayout layoutTeam;
    LinearLayout layoutEvents;
    LinearLayout layoutSchedule;
    LinearLayout layoutSpeakers;
    LinearLayout layoutSponsors;
    LinearLayout layoutContactus;

    //---( BACK PRESS TIME )---//
    CustomSnacks customSnacks;
    private long backPressedTime;

    ImageView notificationBtn;
    ImageView badgeNotifications;
    ImageView mainLogo;

    //----( Session Variables )---//
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //---( SETTING VIEW )---//
        setContentView(R.layout.activity_dashboard);
        customSnacks = new CustomSnacks();
        myApplication = ((MyApplication) getApplication());

        //---( Geeting Data From MyApplication )---//
        if (((MyApplication) getApplication()).isLoginSuccess()){
            fetchUserData();
            View view = findViewById(android.R.id.content);
            customSnacks.successSnack(view,"Logged In Successfully!");
            ((MyApplication) getApplication()).setLoginSuccess(false);
        }

        if (((MyApplication) getApplication()).isProfileSuccess()){
            fetchUserData();
            View view = findViewById(android.R.id.content);
            customSnacks.successSnack(view,"Profile Updated Successfully!");
            ((MyApplication) getApplication()).setProfileSuccess(false);
        }

        if (!myApplication.haveNetworkConnection()){
            View view = findViewById(android.R.id.content);
            customSnacks.warnSnack(view,"No Internet Connection!");
        }

        if (((MyApplication) getApplication()).isUserProfile()){
            FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        }

        //---( Notification Dots )----//
        notificationBtn = findViewById(R.id.iv_notification_btn);
        badgeNotifications = findViewById(R.id.notification_badge);
        badgeNotifications.setVisibility(View.GONE);
        checkNotifications();

        //---( Logout Options )---//
        mainLogo = findViewById(R.id.main_logo);
        mainLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setCancelable(true)
                        .setTitle("Do you want to Logout!")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((MyApplication) getApplication()).removeUserToken(getApplicationContext());
                                ((MyApplication) getApplication()).removeUserIdForNotification(getApplicationContext());
                                ((MyApplication) getApplication()).setLogoutSuccess(true);
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");

                                Intent intent = new Intent(Dashboard.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //---( Notification Intent )----//
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReferencenotup = FirebaseDatabase.getInstance().getReference().child("notificationdots")
                        .child(((MyApplication) getApplication()).getUserId());
                databaseReferencenotup.child("dotStatus").setValue("no");
                databaseReferencenotup.keepSynced(true);
                Intent intentTeam = new Intent(Dashboard.this, Notifications.class);
                startActivity(intentTeam);
                finish();
            }
        });

        //---( FINDING TILES LAYOUT )---//
        layoutTeam = findViewById(R.id.layout_ourteam);
        layoutContactus = findViewById(R.id.layout_contact);
        layoutEvents = findViewById(R.id.layout_events);
        layoutSchedule = findViewById(R.id.layout_shedule);
        layoutSpeakers = findViewById(R.id.layout_speakers);
        layoutSponsors = findViewById(R.id.layout_sponsors);

        //---( TEAM ACTIVITY INTENT )--//
        layoutTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Team.class);
                startActivity(intentTeam);
                finish();
            }
        });
        //---( EVENT ACTIVITY INTENT )--//
        layoutEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Events.class);
                startActivity(intentTeam);
                finish();
            }
        });
        //---( SCHEDULE ACTIVITY INTENT )--//
        layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Schedule.class);
                startActivity(intentTeam);
                finish();
            }
        });
        //---( SPONSORS ACTIVITY INTENT )--//
        layoutSponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Sponsors.class);
                startActivity(intentTeam);
                finish();
            }
        });
        //---( SPEAKERS ACTIVITY INTENT )--//
        layoutSpeakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Speakers.class);
                startActivity(intentTeam);
                finish();
            }
        });
        //---( CONTACTUS ACTIVITY INTENT )--//
        layoutContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTeam = new Intent(Dashboard.this, Contactus.class);
                startActivity(intentTeam);
                finish();
            }
        });

        //---( Tap Target View )---//
        if(isFirstTime()){
            new TapTargetSequence(this).targets(
                    TapTarget.forView(findViewById(R.id.main_logo), "Endeavour Logo", "Use this to Logout from your account \n (Tap on Logo to Cancel)")
                            .tintTarget(false)
                            .targetRadius(125)
                            .targetCircleColor(R.color.main_grey)
                            .cancelable(false)
                            .id(1),
                    TapTarget.forView(findViewById(R.id.iv_notification_btn), "Notification Button", "Tap here to view notifications\n (Tap on button to Cancel)")
                            .tintTarget(false)
                            .cancelable(false)
                            .targetCircleColor(R.color.main_grey)
                            .id(2)
            ).listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            }).start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifications();
    }

    //---( Fetch Data )---//
    public void fetchUserData() {
        String token = ((MyApplication) getApplication()).getUserToken();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyUserTokenApi(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject mainjsonObject=null;
                    try {
                        mainjsonObject = new JSONObject(response.body().string());

                        JSONObject jsonObject = mainjsonObject.optJSONObject("userData");

                        //---( Save User Data )---//
                        ((MyApplication) getApplication()).setUserName(jsonObject.optString("name"));
                        ((MyApplication) getApplication()).setUserEmail(jsonObject.optString("email"));
                        ((MyApplication) getApplication()).setUserProfile(jsonObject.optBoolean("profile"));
                        ((MyApplication) getApplication()).setUserPhoneNumber(jsonObject.optString("phoneNumber"));
                        ((MyApplication) getApplication()).setUserEndvrId(jsonObject.optString("endvrid"));
                        ((MyApplication) getApplication()).setUserId(jsonObject.optString("_id"));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        Log.i("STATUS FAIL :: ",jsonObject.toString());

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR VERIFYUSERTOKEN "," FAIL "+""+t.getMessage());
                call.cancel();
            }
        });
    }

    //---( Ran Before )----//
    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("ranBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ranBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    //----( Notification Dots )----//
    private void checkNotifications() {
        DatabaseReference databaseReferencenot = FirebaseDatabase.getInstance().getReference().child("notificationdots")
                .child(((MyApplication) getApplication()).getUserId());
        databaseReferencenot.keepSynced(true);
        databaseReferencenot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notification_Dot notifications_dots = dataSnapshot.getValue(Notification_Dot.class);
                if (notifications_dots != null)
                {
                    if (notifications_dots.getDotStatus().equals("yes")){
                        badgeNotifications.setVisibility(View.VISIBLE);
                        View view = findViewById(android.R.id.content);
                        customSnacks.infoSnack(view,"Checkout New Notifications!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //---( CUSTOM ON BACK PRESSED )---//
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            View view = findViewById(android.R.id.content);
            customSnacks.warnSnack(view,"Press back again to Exit!");
        }
        backPressedTime = System.currentTimeMillis();
    }
}