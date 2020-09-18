package com.example.depark;

import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ParkingActivity extends AppCompatActivity {

    private static final String TAG = "ParkingActivity";
    private static final String REQUIRED = "Required";
    private DatabaseReference databaseReference;
    private DatabaseReference mRef;
    private String lot;

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
                    mRef.child("Parking-lot").child(parking_lot).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            StatusLot lotnum =  snapshot.getValue(StatusLot.class);

                            if (lotnum == null) {
                                Log.e(TAG, "User data is null!");
                                Toast.makeText(ParkingActivity.this, "User data is null!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if (lotnum.equals("Green")) {
                                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                Parking parking = new Parking(getUsernameFromEmail(firebaseUser.getEmail()), parking_lot, time);
                                String status = "Red";

                                Map<String, Object> parkingValues = parking.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();

                                String key = mRef.child("booking").push().getKey();

                                childUpdates.put("/booking/" + key, parkingValues);

                                mRef.updateChildren(childUpdates);
                                mRef.child("Parking-lot").child(parking_lot).setValue(status);
                            }
                            else if (lotnum.equals("Red")){
                                Log.e(TAG, "Parking Lot Booked!");
                                Toast.makeText(ParkingActivity.this, "This Parking Lot already been booking", Toast.LENGTH_SHORT).show();
                                return;
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
