package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ValetListFragment extends AppCompatActivity {

    private static final String TAG = "ValetListFragment";
    private static final String REQUIRED = "Required";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_list_fragment);

        l1 = findViewById(R.id.listview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("valet");

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.name, arrayList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String author = dataSnapshot.child("author").getValue().toString();
                    String carplate = dataSnapshot.child("carplate").getValue().toString();
                    String type = dataSnapshot.child("type").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();
                    arrayAdapter.add("Author: " + author + "\n" + "CarPlate: " + carplate + "\n" + "Type of car: " + type + "\n" + "Time: " + time);
                    l1.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}