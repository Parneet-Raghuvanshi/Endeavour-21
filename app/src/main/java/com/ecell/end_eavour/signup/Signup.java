package com.ecell.end_eavour.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomLoadingDialog;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.login.Login;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    TextInputLayout tilEmail,tilPassword,tilPhoneNumber,tilFullName;
    Button signupBtn;
    TextView loginToggle;
    CustomSnacks customSnacks;
    CustomLoadingDialog customLoadingDialog;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup);
        customSnacks = new CustomSnacks();
        customLoadingDialog = new CustomLoadingDialog(Signup.this);

        signupBtn = findViewById(R.id.signup_btn);
        loginToggle = findViewById(R.id.login_user_toggle);

        tilEmail = findViewById(R.id.mail_input);
        tilFullName = findViewById(R.id.name_input);
        tilPassword = findViewById(R.id.password_input);
        tilPhoneNumber = findViewById(R.id.phonenumber_input);

        loginToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateDate()){
                    customLoadingDialog.startCustomDialog();
                    signUpApi(tilEmail.getEditText().getText().toString().trim(),
                            tilPassword.getEditText().getText().toString().trim(),
                            tilFullName.getEditText().getText().toString().trim(),
                            tilPhoneNumber.getEditText().getText().toString().trim());
                }
            }
        });
    }

    private boolean validateDate() {
        final String email,password,phoneNumber,name;
        View view = findViewById(android.R.id.content);
        name = tilFullName.getEditText().getText().toString().trim();
        email = tilEmail.getEditText().getText().toString().trim();
        password = tilPassword.getEditText().getText().toString().trim();
        phoneNumber = tilPhoneNumber.getEditText().getText().toString().trim();

        if (email.isEmpty()){
            customSnacks.warnSnack(view,"Email can't be Empty!");
            tilEmail.getEditText().requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            customSnacks.warnSnack(view,"Provide a Valid Email!");
            tilEmail.getEditText().requestFocus();
            return false;
        }
        else if (password.isEmpty()){
            customSnacks.warnSnack(view,"Password can't be Empty!");
            tilPassword.getEditText().requestFocus();
            return false;
        }
        else if (password.length()<5){
            customSnacks.warnSnack(view,"Provide a Valid Password!");
            tilPassword.getEditText().requestFocus();
            return false;
        }
        else if (name.isEmpty()){
            customSnacks.warnSnack(view,"Name can't be Empty!");
            tilFullName.getEditText().requestFocus();
            return false;
        }
        else if(phoneNumber.isEmpty()){
            customSnacks.warnSnack(view,"Phone Number can't be Empty!");
            tilPhoneNumber.getEditText().requestFocus();
            return false;
        }
        else if (phoneNumber.length() != 10) {
            customSnacks.warnSnack(view,"Invalid Phone Number!");
            tilPhoneNumber.getEditText().requestFocus();
            return false;
        }
        return true;
    }

    private void signUpApi(String email,String pass,String name,String phno) {
        View view = findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("name",name);
        params.put("email",email);
        params.put("plainPassword",pass);
        params.put("phoneNumber",phno);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().signUpApi(params);

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
                        customLoadingDialog.dismissCustomDialog();
                        ((MyApplication) getApplication()).setSignUpSuccess(true);

                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                        finish();

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
                Log.e("ERROR :: SIGNUP ::"," FAIL "+""+t.getMessage());
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