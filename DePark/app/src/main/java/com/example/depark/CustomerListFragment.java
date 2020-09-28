package com.example.depark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerListFragment extends AppCompatActivity {

    private static final String TAG = "CustomerListFragment";
    private static final String REQUIRED = "Required";

    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();;
    private ArrayAdapter<String> nameAdapter;

    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list_fragment);

        l1 = findViewById(R.id.listview);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        nameAdapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.name, arrayList);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    UserProfile users = dataSnapshot.getValue(UserProfile.class);
                    arrayList.add("Username: " + users.getUserName() + "\n" + "Useremail: " + users.getUserEmail());
                    l1.setAdapter(nameAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Failed to read user!");
            }
        });
    }
}