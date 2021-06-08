package com.ecell.end_eavour.events.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.events.InternshipFair;
import com.ecell.end_eavour.events.faq.EventsFaq;
import com.ecell.end_eavour.events.registration.EventTeamEntry;
import com.ecell.end_eavour.events.registration.GetEventPass;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Events_Detail extends Fragment {

    //---( \u20B9 )-----( for rupees icon )----//

    ImageView eventImage;
    String eventId;
    Button eventFaq;
    TextView eventName,eventDesc,eventBenefits,eventStructure,eventRules,downloadPortalBtn;
    RecyclerView recyclerView;
    EventsRound_Adapter adapter;
    Button registerBtn,getPassBtn,redirectBtn,paidBtn,ragsBtn;
    CustomSnacks customSnacks;
    ProgressBar progressBar;
    String teamType = "REG";
    String eventType = "";

    public Events_Detail() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(eventType.equals("TECH0") || eventType.equals("TECH1"))){
            if (!((MyApplication) getActivity().getApplication()).isGetPassTrigger()){
                checkRegisteredStatus(((MyApplication) getActivity().getApplication()).getUserToken(),((MyApplication) getActivity().getApplication()).getTempEventId());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_detail, container, false);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        registerBtn = view.findViewById(R.id.register_btn);
        getPassBtn = view.findViewById(R.id.get_pass_btn);
        redirectBtn = view.findViewById(R.id.redirect_btn);
        paidBtn = view.findViewById(R.id.paid_btn);
        paidBtn.setVisibility(View.GONE);
        redirectBtn.setVisibility(View.GONE);
        getPassBtn.setVisibility(View.GONE);
        registerBtn.setVisibility(View.GONE);
        ragsBtn = view.findViewById(R.id.rags_btn);
        ragsBtn.setVisibility(View.GONE);
        downloadPortalBtn = view.findViewById(R.id.download_portal_btn);

        final Bundle bundle = getArguments();
        eventType = bundle.getString("eventType");

        ((MyApplication) getActivity().getApplication()).setTempEventId(bundle.getString("eventId"));

        if (!(eventType.equals("TECH0") || eventType.equals("TECH1"))){
            if (((MyApplication) getActivity().getApplication()).isGetPassTrigger()){
                getPassBtn.setVisibility(View.VISIBLE);
                registerBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            else {
                checkRegisteredStatus(((MyApplication) getActivity().getApplication()).getUserToken(),bundle.getString("eventId"));
                registerBtn.setVisibility(View.VISIBLE);
                getPassBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }
        else{
            Checkout.preload(getActivity().getApplicationContext());
            registerBtn.setVisibility(View.GONE);
            getPassBtn.setVisibility(View.GONE);

            if (eventType.equals("TECH0")){
                redirectBtn.setVisibility(View.VISIBLE);
                redirectBtn.setText("Register");
                progressBar.setVisibility(View.GONE);
            }
            if (eventType.equals("TECH1")){
                downloadPortalBtn.setVisibility(View.VISIBLE);
                checkInternShipStatus();
            }
        }

        redirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventType.equals("TECH0")){

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("hackathon");
                    databaseReference.keepSynced(true);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null){
                                for (DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                    String value = dataSnapshot2.getValue(String.class);
                                    Uri uri = Uri.parse(value);
                                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //customSnacks.successSnack(view,"Hackathon");
                    //---( hackathon link )---//
                }
                if (eventType.equals("TECH1")){
                    //customSnacks.successSnack(view,"Internship Fair");
                    //internship fair 50 rupees;
                    Intent intent = new Intent(getActivity(),InternshipFair.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        recyclerView = view.findViewById(R.id.rv_event_detail_rounds);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        customSnacks = new CustomSnacks();

        eventImage = view.findViewById(R.id.event_main_img);
        eventName = view.findViewById(R.id.event_name);
        eventDesc = view.findViewById(R.id.event_desc);
        eventFaq = view.findViewById(R.id.faq_btn);
        eventBenefits = view.findViewById(R.id.event_benefits);
        eventStructure = view.findViewById(R.id.event_structure);
        eventRules = view.findViewById(R.id.event_rules);
        eventId = bundle.getString("eventId");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("eventsMain");
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("eventId").equalTo(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                    String eventNametemp = dataSnapshot1.child("eventName").getValue().toString();
                    Log.i("String Event Name :: ",eventNametemp);

                    List<EventsRound_Model> eventsRoundModels = new ArrayList<>();
                    for (DataSnapshot dataSnapshotRound : dataSnapshot1.child("eventRounds").getChildren()){
                        EventsRound_Model eventsRoundModel = dataSnapshotRound.getValue(EventsRound_Model.class);
                        eventsRoundModels.add(eventsRoundModel);
                    }
                    adapter = new EventsRound_Adapter(eventsRoundModels);

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(view).load(bundle.getString("eventImage")).into(eventImage);
        eventName.setText(bundle.getString("eventName"));//-----( \u2022  for small bullets )-----( \u25A0 for square box )----( \u25CF for bol bullets )----//
        eventDesc.setText(bundle.getString("eventDesc").replace("--","\n"));
        eventBenefits.setText("\u25CF "+bundle.getString("eventBenefit").replace("--","\n\u25CF "));
        eventStructure.setText("\u25CF "+bundle.getString("eventStructure").replace("--","\n\u25Cf "));
        eventRules.setText("\u25CF "+bundle.getString("eventRules").replace("--","\n\u25CF "));

        eventFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                EventsFaq eventsFaq = new EventsFaq();
                Bundle faqIdBundle = new Bundle();
                faqIdBundle.putString("eventId",bundle.getString("eventId"));
                eventsFaq.setArguments(faqIdBundle);
                fragmentTransaction.replace(R.id.event_detail_container,eventsFaq,"event_faq");
                fragmentTransaction.addToBackStack("faq");
                fragmentTransaction.commit();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEventDetail(((MyApplication) getActivity().getApplication()).getUserToken(),eventId,teamType);
            }
        });

        getPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //---( Get Pass Btn )---//
                Intent intent = new Intent(getActivity(), GetEventPass.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        downloadPortalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.ecell.internshipfairendeavour");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        /*ragsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StartAuction.class);
                startActivity(intent);
            }
        });*/

        return view;
    }

    private void checkInternShipStatus() {
        View view = getActivity().findViewById(android.R.id.content);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyUserTokenApi(((MyApplication) getActivity().getApplication()).getUserToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.body().string());

                        JSONObject jsonObject1 = jsonObject.optJSONObject("userData");

                        boolean internshipStatus = jsonObject1.optBoolean("internship");

                        if (internshipStatus){
                            redirectBtn.setVisibility(View.GONE);
                            paidBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            redirectBtn.setVisibility(View.VISIBLE);
                            redirectBtn.setText("Pay \u20B9 50");
                        }

                        Log.i("STATUS :: ",jsonObject.toString());

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                        Log.i("STATUS :: ",jsonObject.toString());
                        customSnacks.failSnack(view,"Some Error Occurred, Try to Refresh!");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR :: INTERNSHIP ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try to Refresh!");
            }
        });
        redirectBtn.setVisibility(View.VISIBLE);
        redirectBtn.setText("Pay \u20B9 50");
    }

    private void getEventDetail(String userToken,String eventId,String type) {
        View view = getActivity().findViewById(android.R.id.content);
        Log.i("ReRegister Clicked"," true + geteventdetail");
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getEventApi(userToken,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        String msg = jsonObject.optString("msg");

                        JSONObject content = jsonObject.optJSONObject("content");
                        String membersCount = content.optString("membersCount");
                        String price = content.optString("price");

                        Intent intent = new Intent(getActivity(), EventTeamEntry.class);
                        intent.putExtra("eventPrice",price);
                        intent.putExtra("eventMembers",membersCount);
                        intent.putExtra("eventId", eventId);
                        intent.putExtra("teamType",type);
                        startActivity(intent);

                        Log.i("STATUS :: ",jsonObject.toString());

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
                Log.e("ERROR :: CONTACTUS ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try to Refresh!");
            }
        });
    }

    private void checkRegisteredStatus(String userToken, String eventId) {
        View view = getActivity().findViewById(android.R.id.content);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getRegisteredStatusApi(userToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject mainjsonObject=null;
                    try {
                        mainjsonObject = new JSONObject(response.body().string());
                        Log.i("REGISTER STATUS :: ",mainjsonObject.toString());

                        JSONObject jsonObject = mainjsonObject.optJSONObject("userData");

                        boolean eventFound = false;
                        JSONArray registeredEvents = jsonObject.optJSONArray("myEvents");
                        for (int i=0;i<registeredEvents.length();i++){
                            JSONObject child = registeredEvents.optJSONObject(i);
                            String eventid = child.optString("eventId");
                            if (eventid.equals(eventId)){
                                eventFound = true;
                            }
                        }
                        if (eventFound){
                            Log.i("VIEW TEAM FOR :: ",eventId);
                            registerBtn.setText("View Team");
                            registerBtn.setVisibility(View.VISIBLE);
                            teamType = "VIEW";
                        }
                        else{
                            Log.i("REG TEAM FOR :: ",eventId);
                            registerBtn.setVisibility(View.VISIBLE);
                            teamType = "REG";
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String msg = jsonObject.optString("msg");

                        Log.i("STATUS FALSE :: ",jsonObject.toString());
                        teamType = "REG";

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR :: Event ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try to Reopen Event!");
            }
        });
    }

    //---( Razor Pay Payment Process )---//
    public void startInternshipFairPayment() {
        String TAG = "RAZOR PAY :: ";

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_sbRY0oc744nz57");
        checkout.setImage(R.mipmap.ic_launcher_round);

        String userPhoneNumber = ((MyApplication) getActivity().getApplication()).getUserPhoneNumber();
        String userEmail = ((MyApplication) getActivity().getApplication()).getUserEmail();

        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "e-Cell KIET");
            options.put("description", ((MyApplication) getActivity().getApplication()).getUserEndvrId());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#a13941");
            options.put("currency", "INR");
            options.put("amount", "5000");//pass amount in currency subunits ( 15000 /100 )
            options.put("send_sms_hash", true);

            JSONObject preFill = new JSONObject();
            preFill.put("email",userEmail);
            preFill.put("contact","+91"+userPhoneNumber);
            options.put("prefill",preFill);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
}