package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingListFragment extends AppCompatActivity {

    private static final String TAG = "BookingListFragment";
    private static final String REQUIRED = "Required";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    private ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_list_fragment);

        l1 = (ListView) findViewById(R.id.listview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("booking");
        arrayAdapter = new ArrayAdapter<String>(BookingListFragment.this, R.layout.list_row, R.id.name, arrayList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String author = dataSnapshot.child("author").getValue().toString();
                    String lot = dataSnapshot.child("lot").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();
                    arrayAdapter.add("Author: " + author + "\n" + "Lot: " + lot + "\n" + "Time: " + time);
                    l1.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}