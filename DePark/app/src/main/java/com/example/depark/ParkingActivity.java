package com.example.depark;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParkingActivity extends AppCompatActivity {

    private static final String TAG = "ParkingActivity";
    private static final String REQUIRED = "Required";
    private DatabaseReference databaseReference;
    private DatabaseReference mRef;
    private String lot, lot_ava;

    ArrayList<String> parkinglot = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_list);

        b1 = findViewById(R.id.btn101);
        b2 = findViewById(R.id.btn102);
        b3 = findViewById(R.id.btn103);
        b4 = findViewById(R.id.btn104);
        b5 = findViewById(R.id.btn105);
        b6 = findViewById(R.id.btn106);
        b7 = findViewById(R.id.btn107);
        b8 = findViewById(R.id.btn108);
        b9 = findViewById(R.id.btn109);
        b10 = findViewById(R.id.btn110);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        mRef = firebaseDatabase.getReference();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot101";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot102";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot103";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot104";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot105";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot106";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot107";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot108";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot109";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });

        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lot = "Lot110";
                submitBooking(lot);
                Toast.makeText(ParkingActivity.this, "Your Booking been submitted.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitBooking(String lot){
        final String parking_lot = lot;
        final int min = 10;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final String ref = Integer.toString(random);
        final String formatAmount = "test";
        final String state = "test";
        final String date = "end";

        // User data change listener
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);

                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    Toast.makeText(ParkingActivity.this, "User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    mRef.child("Parking-lot").orderByChild("name").equalTo(parking_lot).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                String lot_key = childSnapshot.getKey();
                                String status = childSnapshot.child("status").getValue().toString();

                                if (status == null) {
                                    Log.e(TAG, "User data is null!");
                                    Toast.makeText(ParkingActivity.this, "User data is null!", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (status.equals("Green")) {
                                    String author = getUsernameFromEmail(firebaseUser.getEmail());
                                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                    String newstatus = "Red";
                                    Parking parking = new Parking(ref, getUsernameFromEmail(firebaseUser.getEmail()), parking_lot, time);
                                    Request request = new Request(ref, author, time, formatAmount, state);
                                    Receipt receipt = new Receipt(ref, author, parking_lot, time, date, formatAmount, state);

                                    Map<String, Object> parkingValues = parking.toMap();
                                    Map<String, Object> childUpdatesA = new HashMap<>();

                                    Map<String, Object> requestValues = request.toMap();
                                    Map<String, Object> childUpdatesB = new HashMap<>();

                                    Map<String, Object> receiptValues = receipt.toMap();
                                    Map<String, Object> childUpdatesC = new HashMap<>();

                                    String keyA = mRef.child("booking").push().getKey();
                                    childUpdatesA.put("/booking/" + keyA, parkingValues);

                                    String keyB = mRef.child("request").push().getKey();
                                    childUpdatesB.put("/request/" + keyB, requestValues);

                                    String keyC = mRef.child("receipt").push().getKey();
                                    childUpdatesC.put("/receipt/" + keyC, receiptValues);

                                    mRef.updateChildren(childUpdatesA);
                                    mRef.updateChildren(childUpdatesB);
                                    mRef.updateChildren(childUpdatesC);
                                    mRef.child("Parking-lot").child(lot_key).child("status").setValue(newstatus);

                                    Intent intent = new Intent(ParkingActivity.this, TimeFragment.class);
                                    startActivity(intent);
                                } else if (status.equals("Red")) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ParkingActivity.this);
                                    builder.setTitle("Sorry Sir/Madam,");
                                    builder.setMessage("Parking Lot No: " + parking_lot + " already been booked.\nPlease process with another parking lot number." );
                                    AlertDialog alert1 = builder.create();
                                    alert1.show();
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Failed to read user!");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user!");
            }
        });

    }
    
    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
