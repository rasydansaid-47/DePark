package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "ReceiptActivity";
    private static final String REQUIRED = "Required";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    TextView t1, t2, t3, t4, t5, t6;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        t1 = findViewById(R.id.tvAuthor);
        t2 = findViewById(R.id.tvLot);
        t3 = findViewById(R.id.tvStart);
        t4 = findViewById(R.id.tvEnd);
        t5 = findViewById(R.id.tvPayment);
        t6 = findViewById(R.id.tvTotal);
        b1 = findViewById(R.id.btnOK);

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

                                t1.setText(author);
                                t2.setText(lot);
                                t3.setText(start);
                                t4.setText(end);
                                t5.setText(state);
                                t6.setText(total);

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
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}