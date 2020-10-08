package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReceiptListFragement extends AppCompatActivity {

    private static final String TAG = "ValetListFragment";
    private static final String REQUIRED = "Required";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    ListView l1;
    EditText e1,e2;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_list_fragement);

        l1 = findViewById(R.id.listview);
        e1 = findViewById(R.id.etLot);
        e2 = findViewById(R.id.etdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.name, arrayList);

        String a = getUsernameFromEmail(firebaseUser.getEmail());

        databaseReference.child("receipt").orderByChild("author").equalTo(a).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if(dataSnapshot != null) {
                        final String ref = dataSnapshot.child("ref").getValue().toString();
                        final String author = dataSnapshot.child("author").getValue().toString();
                        final String lot = dataSnapshot.child("lot").getValue().toString();
                        final String start = dataSnapshot.child("start").getValue().toString();
                        final String end = dataSnapshot.child("end").getValue().toString();
                        final String state = dataSnapshot.child("paymentState").getValue().toString();
                        final String total = dataSnapshot.child("total").getValue().toString();
                        arrayAdapter.add("Parking Lot: " + lot + "\n" + "Date: " + start);
                        l1.setAdapter(arrayAdapter);
                        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReceiptListFragement.this);
                                builder.setTitle("Booking Date " + start + " to " + end);
                                builder.setMessage("No.ref: " + ref + "\n" + "Name: " + author + "\n" + "Parking Lot: " + lot + "\n" +
                                        "Payment State: " + state + "\n" + "Total Price: RM" + total);
                                AlertDialog alert1 = builder.create();
                                alert1.show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
                arrayAdapter.notifyDataSetChanged();
                l1.invalidateViews();
                l1.refreshDrawableState();
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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