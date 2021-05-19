package com.ecell.end_eavour.login;

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

import com.ecell.end_eavour.Dashboard;
import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomLoadingDialog;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.customised.ForgotPasswordDialog;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.ecell.end_eavour.signup.Signup;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    TextInputLayout tilEmail,tilPassword;
    Button loginBtn;
    TextView signupToggle,forgotPassBtn;
    CustomLoadingDialog customLoadingDialog;
    ForgotPasswordDialog forgotPasswordDialog;
    CustomSnacks customSnacks;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---( TO REMOVE STATUS BAR )---//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        customLoadingDialog = new CustomLoadingDialog(Login.this);
        forgotPasswordDialog = new ForgotPasswordDialog(Login.this);
        customSnacks = new CustomSnacks();

        if (((MyApplication) getApplication()).isLoginExpired()){
            View view = findViewById(android.R.id.content);
            customSnacks.failSnack(view,"Login Expired!");
            ((MyApplication) getApplication()).setLoginExpired(false);
        }
        if (((MyApplication) getApplication()).isSignUpSuccess()){
            View view = findViewById(android.R.id.content);
            customSnacks.successSnack(view,"Account Created, Please Verify Your Email!");
            ((MyApplication) getApplication()).setSignUpSuccess(false);
        }
        if (((MyApplication) getApplication()).isLogoutSuccess()){
            View view = findViewById(android.R.id.content);
            customSnacks.successSnack(view,"Logout Successfully, Please Login!");
            ((MyApplication) getApplication()).setLogoutSuccess(false);
        }

        loginBtn = findViewById(R.id.login_btn);
        signupToggle = findViewById(R.id.signup_user_toggle);
        forgotPassBtn = findViewById(R.id.forgot_pass);

        tilEmail = findViewById(R.id.mail_input);
        tilPassword = findViewById(R.id.password_input);

        signupToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    customLoadingDialog.startCustomDialog();
                    loginUser(tilEmail.getEditText().getText().toString().trim(),tilPassword.getEditText().getText().toString().trim());
                }
            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordDialog.requestForgotPasswordDialog();
            }
        });
    }

    private boolean validateData() {
        final String email,password;
        View view = findViewById(android.R.id.content);

        email = tilEmail.getEditText().getText().toString().trim();
        password = tilPassword.getEditText().getText().toString().trim();

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
        else return true;
    }

    private void loginUser(String email,String pass) {
        View view = findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("email",email);
        params.put("plainPassword",pass);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().loginApi(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject jsonObject=null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject userObject = jsonObject.getJSONObject("user");

                        //---( Save to Shared Preferences Also )---//
                        String userToken = jsonObject.optString("token");
                        ((MyApplication) getApplication()).setUserToken(userToken);
                        ((MyApplication) getApplication()).setUserUserIdForNotification(getApplicationContext(),userObject.optString("_id"));
                        ((MyApplication) getApplication()).saveUserToken(getApplicationContext(),userToken);
                        ((MyApplication) getApplication()).setUserProfile(userObject.optBoolean("profile"));
                        ((MyApplication) getApplication()).setUserId(userObject.optString("_id"));

                        if (userObject.optBoolean("profile")){
                            ((MyApplication) getApplication()).setLoginSuccess(true);

                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                            customLoadingDialog.dismissCustomDialog();
                        }
                        else {
                            ((MyApplication) getApplication()).setToProfile(true);

                            Intent intent = new Intent(Login.this, Profile.class);
                            startActivity(intent);
                            finish();
                            customLoadingDialog.dismissCustomDialog();
                        }

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