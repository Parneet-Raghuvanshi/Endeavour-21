package com.ecell.end_eavour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.login.Login;
import com.ecell.end_eavour.login.Profile;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.ecell.end_eavour.services.MyApplication;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    int DELAY_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        verifyUserToken();
    }

    public String readUserToken(Context context){
        String key = "userToken";
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)){
            if (sharedPreferences.getString(key,null).isEmpty()){
                return "";
            }
            else {
                return sharedPreferences.getString(key,null);
            }
        }
        else {
            return "";
        }
    }

    public void verifyUserToken(){
        String token = readUserToken(getApplicationContext());

        if (token.equals("")){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");
                    startActivity(intent);
                    finish();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, DELAY_TIME);
        }
        else {
            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().verifyUserTokenApi(token);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        JSONObject mainjsonObject=null;
                        try {
                            mainjsonObject = new JSONObject(response.body().string());

                            Log.i("PROFILE :: ",mainjsonObject.toString());

                            JSONObject jsonObject = mainjsonObject.optJSONObject("userData");

                            //---( Save User Data )---//
                            ((MyApplication) getApplication()).setUserName(jsonObject.optString("name"));
                            ((MyApplication) getApplication()).setUserEmail(jsonObject.optString("email"));
                            ((MyApplication) getApplication()).setUserProfile(jsonObject.optBoolean("profile"));
                            ((MyApplication) getApplication()).setUserPhoneNumber(jsonObject.optString("phoneNumber"));
                            ((MyApplication) getApplication()).setUserEndvrId(jsonObject.optString("endvrid"));
                            ((MyApplication) getApplication()).setUserId(jsonObject.optString("_id"));
                            ((MyApplication) getApplication()).setLoginExpired(false);
                            ((MyApplication) getApplication()).setUserToken(token);

                            if (jsonObject.optBoolean("profile")){
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                };
                                Handler handler = new Handler();
                                handler.postDelayed(runnable,DELAY_TIME);
                            }
                            else {
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MyApplication) getApplication()).setProfileMissing(true);
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");
                                        Intent intent = new Intent(SplashScreen.this, Profile.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                };
                                Handler handler = new Handler();
                                handler.postDelayed(runnable,DELAY_TIME);
                            }

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
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ((MyApplication) getApplication()).setLoginExpired(true);
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");
                                Intent intent = new Intent(SplashScreen.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable,DELAY_TIME);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("ERROR VERIFYUSERTOKEN "," FAIL "+""+t.getMessage());
                    call.cancel();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            ((MyApplication) getApplication()).setLoginExpired(true);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");
                            Intent intent = new Intent(SplashScreen.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    Handler handler = new Handler();
                    handler.postDelayed(runnable, DELAY_TIME);
                }
            });
        }
    }
}