package com.ecell.end_eavour.events.registration;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTeamEntry extends AppCompatActivity {

    TextInputLayout tilLeaderEndvr,tilMemberOne,tilMemberTwo;
    Button continueBtn,doneBtn;
    String memberCount, eventPrice,teamType;
    String eventId;
    CustomSnacks customSnacks;
    String teamId = "";
    TextView activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_team_entry);

        //---( Razor Pay Preload )---//
        //Checkout.preload(getApplicationContext());

        memberCount = getIntent().getStringExtra("eventMembers");
        eventId = getIntent().getStringExtra("eventId");
        eventPrice = getIntent().getStringExtra("eventPrice");
        teamType = getIntent().getStringExtra("teamType");
        Log.i("TEAM ENTRY FOR :: ",teamType);
        activityType = findViewById(R.id.activity_type);
        doneBtn = findViewById(R.id.done_btn);

        tilLeaderEndvr =  findViewById(R.id.leader_endvr_input);
        tilLeaderEndvr.getEditText().setText(((MyApplication) getApplication()).getUserEndvrId());
        tilLeaderEndvr.setEnabled(false);
        tilMemberOne = findViewById(R.id.member1_endvr_input);
        tilMemberTwo = findViewById(R.id.member2_endvr_input);
        continueBtn = findViewById(R.id.continue_btn);

        customSnacks = new CustomSnacks();

        //---( Register Checkout )---//
        if (teamType.equals("VIEW")){
            tilMemberOne.setEnabled(false);
            tilMemberTwo.setEnabled(false);
            Log.i("UPDATING ACT ",teamType+ " Here updating for  "+eventId );
            getUserDetail();
        }

        if (memberCount.equals("1")){
            tilMemberOne.setVisibility(View.GONE);
            tilMemberTwo.setVisibility(View.GONE);
        }
        else if (memberCount.equals("2")){
            tilMemberTwo.setVisibility(View.GONE);
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValidate()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventTeamEntry.this);
                    builder.setCancelable(true)
                            .setTitle("Are you sure you want to Continue!")
                            .setIcon(R.mipmap.ic_launcher_round)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    registerToEvent(((MyApplication) getApplication()).getUserToken(),eventId,tilMemberOne.getEditText().getText().toString(),tilMemberTwo.getEditText().getText().toString());
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
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateActivityType() {
        tilMemberTwo.setEnabled(false);
        tilMemberOne.setEnabled(false);
        tilMemberOne.setEndIconVisible(false);
        activityType.setText("Your Team");
        continueBtn.setVisibility(View.GONE);
        doneBtn.setVisibility(View.VISIBLE);
    }

    private boolean isDataValidate() {
        View view = findViewById(android.R.id.content);

        if (!tilMemberOne.getEditText().getText().toString().isEmpty() && tilMemberOne.getEditText().getText().toString().length()!=19 && !tilMemberOne.equals(((MyApplication) getApplication()).getUserEndvrId())){
            customSnacks.infoSnack(view,"Member One Detail Incorrect!");
            tilMemberOne.getEditText().requestFocus();
            return false;
        }
        else if (!tilMemberTwo.getEditText().getText().toString().isEmpty() && tilMemberTwo.getEditText().getText().toString().length()!=19 && !tilMemberTwo.equals(((MyApplication) getApplication()).getUserEndvrId())){
            customSnacks.infoSnack(view,"Member Two Detail Incorrect!");
            tilMemberTwo.getEditText().requestFocus();
            return true;
        }
        else {
            return true;
        }
    }

    private void registerToEvent(String userToken,String eventId,String memberOne,String memberTwo) {
        View view = findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("member2",memberOne);
        params.put("member3",memberTwo);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().registerEvent(userToken,params,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        String msg = jsonObject.optString("msg");

                        Log.i("STATUS :: ",jsonObject.toString());
                        customSnacks.successSnack(view,msg);
                        teamType = "VIEW";
                        updateActivityType();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String msg = jsonObject.optString("msg");

                        Log.i("STATUS :: ",jsonObject.toString());
                        customSnacks.failSnack(view,msg);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR :: EVENTREG ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try Again!");
            }
        });
    }

    //---( Get User Api )---//
    public void getUserDetail(){
        View view = findViewById(android.R.id.content);
        String token = ((MyApplication) getApplication()).getUserToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyUserTokenApi(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject mainjsonObject = null;
                    try {
                        mainjsonObject = new JSONObject(response.body().string());

                        JSONObject jsonObject = mainjsonObject.optJSONObject("userData");
                        Log.i("JSON ARRAY :: ", mainjsonObject.toString());
                        Log.i("EVENT ID :: ",eventId);

                        //---( Save User Data )---//
                        JSONArray registeredEvents = jsonObject.optJSONArray("myEvents");
                        for (int i=0;i<registeredEvents.length();i++){
                            JSONObject child = registeredEvents.optJSONObject(i);
                            String eventid = child.optString("eventId");
                            if (eventid.equals(eventId)){
                                JSONArray teamMembers = child.optJSONArray("members");
                                if (teamMembers.optString(1).isEmpty()){
                                    tilMemberOne.setVisibility(View.GONE);
                                }
                                else{
                                    tilMemberOne.setVisibility(View.VISIBLE);
                                }
                                /*if (teamMembers.optString(2).isEmpty()){
                                    tilMemberTwo.setVisibility(View.GONE);
                                }*/
                                tilMemberOne.getEditText().setText(teamMembers.optString(1));
                                /*tilMemberTwo.getEditText().setText(teamMembers.optString(2));
                                tilMemberTwo.setEnabled(false);*/

                                teamType = "VIEW";
                                updateActivityType();
                            }
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        Log.i("STATUS FAIL :: ", jsonObject.toString());
                        customSnacks.failSnack(view,"Please Refresh and Try Again!");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR VERIFYUSERTOKEN ", " FAIL " + "" + t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Please Refresh and Try Again!");
            }
        });
    }

    private void getTeamDetails(String teamId) {
        View view = findViewById(android.R.id.content);
        String token = ((MyApplication) getApplication()).getUserToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getTeamDetail(token,teamId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());

                        //---( Save User Data )---//
                        JSONArray teamMembers = jsonObject.optJSONArray("teamMembers");
                        if (teamMembers.optString(1).isEmpty()){
                            tilMemberOne.setVisibility(View.GONE);
                        }
                        if (teamMembers.optString(2).isEmpty()){
                            tilMemberTwo.setVisibility(View.GONE);
                        }
                        tilMemberOne.getEditText().setText(teamMembers.optString(1));
                        tilMemberOne.setEnabled(false);
                        tilMemberTwo.getEditText().setText(teamMembers.optString(2));
                        tilMemberTwo.setEnabled(false);

                        teamType = "VIEW";
                        updateActivityType();

                        Log.i("JSON OBJECT:: ", jsonObject.toString());

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        Log.i("STATUS FAIL :: ", jsonObject.toString());
                        customSnacks.failSnack(view,"Please Refresh and Try Again!");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR VERIFYUSERTOKEN ", " FAIL " + "" + t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Please Refresh and Try Again!");
            }
        });
    }
}