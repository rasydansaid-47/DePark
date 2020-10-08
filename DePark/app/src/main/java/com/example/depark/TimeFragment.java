package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TimeFragment extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 9999;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, mRef;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    private Chronometer meter;
    private long pauseOffset;
    private boolean running;
    final Context context = this;
    double pay;
    long hours;
    String g,e;
    DigitalClock c1;
    TextView t1;
    Button b1;

    //Paypal Payment
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(com.example.depark.config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_time);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mRef = firebaseDatabase.getReference("");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final long time = sharedPreferences.getLong("time", 0);

        c1 = findViewById(R.id.textClock);
        meter = findViewById(R.id.chronometer);
        t1 = findViewById(R.id.textview1);
        b1 = findViewById(R.id.btnPay);
        hours = SystemClock.elapsedRealtime() - (meter.getBase() + time);

        startChronometer();
        meter.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long myElapsedMillis = SystemClock.elapsedRealtime() - (meter.getBase() + time);
                double seconds = myElapsedMillis * 0.001;
                if (seconds <= 11700) {
                    pay = 2.0;
                    t1.setText("RM" + pay);
                } else if (seconds > 11700) {
                    pay = 2.0 + ((seconds % 11700));
                    t1.setText("RM" + pay);

                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endChronometer();
                getPayment();
            }
        });
    }

    private void getPayment(){
        String formatAmount = Double.toString(pay);

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatAmount),
                "MYR",
                "De`ParkApp",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }
    

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int req = requestCode;
        final int res = resultCode;
        final Intent intent = data;
        final String author = getUsernameFromEmail(firebaseUser.getEmail());
        final String endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        final String formatAmount = Double.toString(pay);
        databaseReference.child("booking").orderByChild("author").equalTo(author).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    final String ref = dataSnapshot.child("ref").getValue().toString();
                    databaseReference.child("request").orderByChild("ref").equalTo(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                String lockey = dataSnapshot.getKey();
                                if (req == PAYPAL_REQUEST_CODE) {
                                    if (res == Activity.RESULT_OK) {
                                        final PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                                        if (confirmation != null) {
                                            try {
                                                String paymentDetail = confirmation.toJSONObject().toString();
                                                JSONObject jsonObject = new JSONObject(paymentDetail);

                                                String state = jsonObject.getJSONObject("response").getString("state");

                                                databaseReference.child("request").child(lockey).child("time").setValue(endtime);
                                                databaseReference.child("request").child(lockey).child("total").setValue(formatAmount);
                                                databaseReference.child("request").child(lockey).child("paymentState").setValue(state);
                                                databaseReference.child("booking").orderByChild("author").equalTo(author).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            String lot = dataSnapshot.child("lot").getValue().toString();
                                                            databaseReference.child("Parking-lot").orderByChild("name").equalTo(lot).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                        String lotkey = dataSnapshot.getKey();
                                                                        String status = "Green";
                                                                        databaseReference.child("Parking-lot").child(lotkey).child("status").setValue(status);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                Intent intent = new Intent(TimeFragment.this, ReceiptActivity.class);
                                                startActivity(intent);

                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    } else if (resultCode == Activity.RESULT_CANCELED) {
                                        Toast.makeText(getApplicationContext(), "Payment cancelled", Toast.LENGTH_SHORT).show();
                                    } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                                        Toast.makeText(getApplicationContext(), "Invalid payment", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void startChronometer(){
        if(!running){
            meter.setBase(SystemClock.elapsedRealtime());
            meter.start();
            running = true;
        }
    }

    public void pauseChronometer(){
        if(running){
            meter.stop();
            pauseOffset = SystemClock.elapsedRealtime() - meter.getBase();
            running = false;
        }
    }

    public void endChronometer(){
        meter.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time",meter.getBase()).apply();
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time",meter.getBase()).apply();
        editor.commit();
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}