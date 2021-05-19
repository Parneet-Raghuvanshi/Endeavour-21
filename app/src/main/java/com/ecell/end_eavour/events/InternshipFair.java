package com.ecell.end_eavour.events;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ecell.end_eavour.R;
import com.ecell.end_eavour.customised.CustomSnacks;
import com.ecell.end_eavour.services.MyApplication;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class InternshipFair extends AppCompatActivity implements PaymentResultWithDataListener {

    Button payInternshipBtn,doneBtn;
    CustomSnacks customSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_internship_fair);

        Checkout.preload(getApplicationContext());

        payInternshipBtn = findViewById(R.id.payinternship_btn);
        doneBtn = findViewById(R.id.done_pass_btn);
        doneBtn.setVisibility(View.GONE);
        customSnacks = new CustomSnacks();

        payInternshipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInternshipPayment();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InternshipFair.this, Events.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //---( Razor Pay Payment Process )---//
    private void startInternshipPayment() {
        String TAG = "RAZOR PAY :: ";

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_bW2H9hmho7861f");
        checkout.setImage(R.mipmap.ic_launcher_round);

        String userPhoneNumber = ((MyApplication) getApplication()).getUserPhoneNumber();
        String userEmail = ((MyApplication) getApplication()).getUserEmail();

        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", ((MyApplication) getApplication()).getUserName());
            options.put("description", ((MyApplication)getApplication()).getUserEndvrId());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#a13941");
            options.put("currency", "INR");
            options.put("amount", "5000");//pass amount in currency subunits ( 15000 /100 )

            JSONObject preFill = new JSONObject();
            preFill.put("email",userEmail);
            options.put("send_sms_hash", true);
            preFill.put("contact","+91"+userPhoneNumber);
            options.put("prefill",preFill);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.i("SUCCESS PAY :: ",s+" Payment Id ");
        View view = findViewById(android.R.id.content);
        customSnacks.successSnack(view,"Payment Successful!");
        payInternshipBtn.setVisibility(View.GONE);
        doneBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.i("FAIL PAY :: ",s+ " Payment Id ");
        View view = findViewById(android.R.id.content);
        customSnacks.failSnack(view,"Payment Unsuccessful, Try Again!");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InternshipFair.this);
        builder.setCancelable(true)
                .setTitle("Do you want to go Back!")
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(InternshipFair.this, Events.class);
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
}