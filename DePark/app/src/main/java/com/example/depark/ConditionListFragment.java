package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConditionListFragment extends AppCompatActivity {

    private static final String TAG = "ValetListFragment";
    private static final String REQUIRED = "Required";

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
        setContentView(R.layout.condition_list_fragment);

        l1 = findViewById(R.id.listview);
        e1 = findViewById(R.id.etLot);
        e2 = findViewById(R.id.etStatus);
        b1 = findViewById(R.id.btnSubmit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.name, arrayList);

        databaseReference.child("Parking-lot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String lot = dataSnapshot.child("name").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    arrayAdapter.add("Parking Lot: " + lot + "\n" + "Status: " + status);
                    l1.setAdapter(arrayAdapter);
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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lot = e1.getText().toString();
                final String status = e2.getText().toString();

                databaseReference.child("Parking-lot").orderByChild("name").equalTo(lot).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange (@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            String lotkey = dataSnapshot.getKey();
                            databaseReference.child("Parking-lot").child(lotkey).child("status").setValue(status);
                            Toast.makeText(ConditionListFragment.this, "The status has been changed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to read user!");
                    }
                });
            }
        });
    }
}