package com.ecell.end_eavour.contactus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomLoadingDialog;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragment;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentOne;
import com.ecell.end_eavour.fab.BottomSheetNavigationFragmentTwo;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contactus extends AppCompatActivity {

    private BottomAppBar bottomAppBar;

    CustomLoadingDialog customLoadingDialog;
    CustomSnacks customSnacks;

    TextInputLayout tilEmailSubject,tilEmailBody,tilEmail;

    ImageView backBtn;
    Button submitBtn,visitWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_contactus);
        customSnacks = new CustomSnacks();
        customLoadingDialog = new CustomLoadingDialog(Contactus.this);
        setUpBottomAppBar();

        backBtn = findViewById(R.id.back);
        submitBtn = findViewById(R.id.submit_btn);
        tilEmail = findViewById(R.id.mail_input);
        tilEmail.getEditText().setText(((MyApplication) getApplication()).getUserEmail());
        tilEmail.setEnabled(false);
        tilEmailBody = findViewById(R.id.mailbody_input);
        tilEmailSubject = findViewById(R.id.mailsub_input);
        visitWebsite = findViewById(R.id.viewweb_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    customLoadingDialog.startCustomDialog();
                    contactUsAPI(((MyApplication) getApplication()).getUserName(),((MyApplication) getApplication()).getUserEmail(),
                            tilEmailBody.getEditText().getText().toString(),
                            tilEmailSubject.getEditText().getText().toString().trim());
                }
            }
        });

        visitWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://endeavour-kiet.in/");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void contactUsAPI(String name,String email,String body,String subject) {
        View view = findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("contactUserName",name);
        params.put("contactEmail",email);
        params.put("contactContent",body);
        params.put("contactSubject",subject);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().contactUsApi(params);

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
                        customSnacks.successSnack(view,msg);
                        customLoadingDialog.dismissCustomDialog();

                        tilEmailSubject.getEditText().setText("");
                        tilEmailSubject.getEditText().clearFocus();
                        tilEmailBody.getEditText().clearFocus();
                        tilEmailBody.getEditText().setText("");

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
                Log.e("ERROR :: CONTACTUS ::"," FAIL "+""+t.getMessage());
                call.cancel();
                customSnacks.failSnack(view,"Some Error Occurred, Try Again!");
                customLoadingDialog.dismissCustomDialog();
            }
        });
    }

    //---( To check data entered )---//
    private boolean validateData() {
        final String emailSubject,emailBody;
        View view = findViewById(android.R.id.content);

        emailSubject = tilEmailSubject.getEditText().getText().toString().trim();
        emailBody = tilEmailBody.getEditText().getText().toString().trim();

        if (emailSubject.isEmpty()){
            customSnacks.warnSnack(view,"Subject can't be Empty!");
            tilEmailSubject.getEditText().requestFocus();
            return false;
        }
        else if (emailBody.isEmpty()){
            customSnacks.warnSnack(view,"Body can't be Empty!");
            tilEmailBody.getEditText().requestFocus();
            return false;
        }
        else if (emailBody.length()<20){
            customSnacks.warnSnack(view,"Body should be 20 character long!");
            tilEmailBody.getEditText().requestFocus();
            return false;
        }
        else return true;
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
        Intent intent = new Intent( Contactus.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}