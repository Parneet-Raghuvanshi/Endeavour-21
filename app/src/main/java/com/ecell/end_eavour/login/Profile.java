package com.ecell.end_eavour.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomLoadingDialog;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    TextInputLayout tilCollegeName, tilLibraryId,tilBranch, tilSemester,tilDiscord;
    Button continueBtn;
    CustomLoadingDialog customLoadingDialog;
    CustomSnacks customSnacks;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);
        customLoadingDialog = new CustomLoadingDialog(Profile.this);
        customSnacks = new CustomSnacks();

        if (((MyApplication) getApplication()).isToProfile()){
            View view = findViewById(android.R.id.content);
            customSnacks.successSnack(view,"Logged In Successfully, Complete your Profile to Continue!");
            ((MyApplication) getApplication()).setToProfile(false);
        }
        if (((MyApplication) getApplication()).isProfileMissing()){
            View view = findViewById(android.R.id.content);
            customSnacks.warnSnack(view,"Complete your Profile to Continue!");
            ((MyApplication) getApplication()).setProfileMissing(false);
        }

        continueBtn = findViewById(R.id.continue_btn);
        tilCollegeName = findViewById(R.id.college_input);
        tilLibraryId = findViewById(R.id.library_input);
        tilBranch = findViewById(R.id.branch_input);
        tilSemester = findViewById(R.id.semester_input);
        tilDiscord = findViewById(R.id.discordid_input);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    customLoadingDialog.startCustomDialog();
                    completeProfile(((MyApplication) getApplication()).getUserToken(),
                            tilCollegeName.getEditText().getText().toString().trim(),
                            tilLibraryId.getEditText().getText().toString().trim(),
                            tilBranch.getEditText().getText().toString().trim(),
                            Integer.parseInt(tilSemester.getEditText().getText().toString().trim()),
                            tilDiscord.getEditText().getText().toString().trim()
                    );
                }
            }
        });
    }

    private boolean validateData() {
        final String collegeName,libraryId,branch,semester,discordId;
        View view = findViewById(android.R.id.content);
        Pattern pattern = Pattern.compile(".+#\\d{4}");

        collegeName = tilCollegeName.getEditText().getText().toString().trim();
        libraryId = tilLibraryId.getEditText().getText().toString().trim();
        branch = tilBranch.getEditText().getText().toString().trim();
        semester = tilSemester.getEditText().getText().toString().trim();
        discordId = tilDiscord.getEditText().getText().toString().trim();

        if (collegeName.isEmpty()){
            customSnacks.warnSnack(view,"College Name can't be Empty!");
            tilCollegeName.getEditText().requestFocus();
            return false;
        }
        else if (libraryId.isEmpty()) {
            customSnacks.warnSnack(view,"University RollNo can't be Empty!");
            tilLibraryId.getEditText().requestFocus();
            return false;
        }
        else if (branch.isEmpty()) {
            customSnacks.warnSnack(view,"Branch can't be Empty!");
            tilBranch.getEditText().requestFocus();
            return false;
        }
        else if (semester.isEmpty()){
            customSnacks.warnSnack(view,"Semester can't be Empty!");
            tilSemester.getEditText().requestFocus();
            return false;
        }
        else if (semester.length()!=1 && Integer.parseInt(semester)>8 && Integer.parseInt(semester)<1) {
            customSnacks.warnSnack(view,"Semester should lie between (1-8)!");
            tilSemester.getEditText().requestFocus();
            return false;
        }
        else if (discordId.isEmpty()) {
            customSnacks.warnSnack(view,"Discord can't be Empty!");
            tilDiscord.getEditText().requestFocus();
            return false;
        }
        else if (!pattern.matcher(discordId).matches()){
            customSnacks.warnSnack(view,"Discord ID Format Incorrect!");
            tilDiscord.getEditText().requestFocus();
            return false;
        }
        else return true;
    }

    private void completeProfile(String userToken, String clgname,String libId,String branch,int semester,String discord) {
        View view = findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("branch",branch);
        params.put("semester",semester);
        params.put("college",clgname);
        params.put("libId",libId);
        params.put("discord",discord);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().setProfileApi(userToken,params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.optString("status");
                        String msg = jsonObject.optString("msg");

                        Log.i("STATUS :: ",status);

                        ((MyApplication) getApplication()).setProfileSuccess(true);

                        DatabaseReference databaseReferencenotup = FirebaseDatabase.getInstance().getReference().child("notificationdots").child(((MyApplication) getApplication()).getUserId());
                        databaseReferencenotup.child("dotStatus").setValue("no");
                        databaseReferencenotup.keepSynced(true);

                        Intent intent = new Intent(Profile.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                        customLoadingDialog.dismissCustomDialog();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        String status = jsonObject.optString("status");
                        String msg = jsonObject.optString("msg");

                        Log.i("STATUS :: ",status);
                        customSnacks.failSnack(view,msg);
                        customLoadingDialog.dismissCustomDialog();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR :: LOGIN ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try Again!");
                customLoadingDialog.dismissCustomDialog();
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