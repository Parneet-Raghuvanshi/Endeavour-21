package com.ecell.end_eavour.customised;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.serverfiles.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    TextInputLayout tilEmail;
    CustomSnacks customSnacks;
    ProgressBar progressBar;

    public ForgotPasswordDialog(Activity myActivity) {
        customSnacks = new CustomSnacks();
        activity = myActivity;
    }

    public void requestForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_forgotpass_dialog,null,false));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        Button continueBtn = alertDialog.findViewById(R.id.continue_btn);
        tilEmail = alertDialog.findViewById(R.id.email_input);
        progressBar = alertDialog.findViewById(R.id.progressbar);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData(view)){
                    continueBtn.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    sendForgotPassMail(tilEmail.getEditText().getText().toString().trim());
                }
            }
        });
    }

    private boolean validateData(View view) {
        String email = tilEmail.getEditText().getText().toString().trim();

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
        else return true;
    }

    private void sendForgotPassMail(String mail) {
        View view = activity.findViewById(android.R.id.content);
        Map<String,Object> params = new ArrayMap<>();
        params.put("email",mail);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sendForgotPassMail(params);

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
                        alertDialog.dismiss();
                        customSnacks.successSnack(view,msg);

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
                        alertDialog.dismiss();
                        customSnacks.failSnack(view,msg);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR :: FORGOT PASS ::"," FAIL "+""+t.getMessage());
                call.cancel();
                alertDialog.dismiss();
                customSnacks.failSnack(view,"Some Error Occurred, Try Again!");
            }
        });
    }
}
