package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "ReceiptActivity";
    private static final String REQUIRED = "Required";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    TextView t1;
    Button b1, b2;
    ImageView I1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        I1 = findViewById(R.id.qrcode);
        t1 = findViewById(R.id.tvAuthor);
        b1 = findViewById(R.id.btnOK);
        b2 = findViewById(R.id.btnQrcode);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        String a = getUsernameFromEmail(firebaseUser.getEmail());

        databaseReference.child("request").orderByChild("author").equalTo(a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    final String ref = dataSnapshot.child("ref").getValue().toString();
                    final String end = dataSnapshot.child("time").getValue().toString();
                    final String state = dataSnapshot.child("paymentState").getValue().toString();
                    final String total = dataSnapshot.child("total").getValue().toString();

                    databaseReference.child("receipt").orderByChild("ref").equalTo(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                String lockey = dataSnapshot.getKey();
                                String author = dataSnapshot.child("author").getValue().toString();
                                String lot = dataSnapshot.child("lot").getValue().toString();
                                String start = dataSnapshot.child("start").getValue().toString();

                                t1.setText("No.ref: " + ref + "\n" + "Name: " + author + "\n" + "Parking Lot: " + lot + "\n" +
                                        "Payment State: " + state + "\n" + "Total Price: RM" + total);

                                databaseReference.child("receipt").child(lockey).child("end").setValue(end);
                                databaseReference.child("receipt").child(lockey).child("total").setValue(total);
                                databaseReference.child("receipt").child(lockey).child("paymentState").setValue(state);

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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = getUsernameFromEmail(firebaseUser.getEmail());

                databaseReference.child("request").orderByChild("author").equalTo(a).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            final String ref = dataSnapshot.child("ref").getValue().toString();
                            final String state = dataSnapshot.child("paymentState").getValue().toString();
                            final String total = dataSnapshot.child("total").getValue().toString();

                            databaseReference.child("receipt").orderByChild("ref").equalTo(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String author = dataSnapshot.child("author").getValue().toString();
                                        String lot = dataSnapshot.child("lot").getValue().toString();

                                        String data = "Name: " + author + "\n" + "Lot: " + lot + "\n" + "Total: RM" + total + "\n" +
                                                "Payment State: " + state;
                                        if (data.isEmpty()) {
                                            Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_SHORT).show();
                                        } else {
                                            QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
                                            try {
                                                Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                                                I1.setImageBitmap(qrBits);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
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