package com.ecell.end_eavour.customised;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.ecell.end_eavour.R;

public class CustomLoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public CustomLoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog,null,false));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public void dismissCustomDialog() {
        alertDialog.dismiss();
    }
}
