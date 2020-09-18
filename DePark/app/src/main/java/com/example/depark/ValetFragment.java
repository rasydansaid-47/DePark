package com.example.depark;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ValetFragment extends Activity {

    private static final String TAG = "FeedbackFragment";
    private static final String REQUIRED = "Required";
    private DatabaseReference databaseReference;
    private DatabaseReference mRef;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    EditText e1, e2, e3;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_valet);

        e1 = (EditText) findViewById(R.id.etTypeCar);
        e2 = (EditText) findViewById(R.id.etCarPlate);
        e3 = (EditText) findViewById(R.id.etTime);
        b1 = (Button) findViewById(R.id.btnSubmit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        mRef = firebaseDatabase.getReference();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitValet();
                Toast.makeText(getApplicationContext(),"Valet Appointment been sended", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitValet(){
        final String typecar = e1.getText().toString();
        final String carplate = e2.getText().toString();
        final String time = e3.getText().toString();

        if (TextUtils.isEmpty(typecar)) {
            e3.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(carplate)) {
            e3.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(time)) {
            e3.setError(REQUIRED);
            return;
        }

        // User data change listener
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);

                if (user == null) {
                    Log.e(TAG, "onDataChange: User data is null!");
                    Toast.makeText(ValetFragment.this, "onDataChange: User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                writeNewMessage(typecar, carplate, time);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read user!");
            }
        });

    }

    private void writeNewMessage(String typecar, String carplate, String time) {
        Valet valet = new Valet(getUsernameFromEmail(firebaseUser.getEmail()), typecar, carplate, time);

        Map<String, Object> valetValues = valet.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = mRef.child("messages").push().getKey();

        childUpdates.put("/valet/" + key, valetValues);

        mRef.updateChildren(childUpdates);
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}