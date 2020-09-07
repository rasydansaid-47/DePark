package com.example.depark;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FeedbackFragment extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "FeedbackFragment";
    private static final String REQUIRED = "Required";
    private DatabaseReference databaseReference;

    EditText e1, e2, e3;
    Button b1;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Spinner s1;
    String[] TypeofFeedback = { "Type of Feedback", "Suggestion", "Bugs", "Report"};
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);
        e3 = (EditText) findViewById(R.id.etMessage);
        s1 = (Spinner) findViewById(R.id.typeFeedback);
        b1 = (Button) findViewById(R.id.btnSend);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TypeofFeedback);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = TypeofFeedback[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Invalid Input",Toast.LENGTH_SHORT).show();
    }

    private void submitFeedback(){
        final String body = e3.getText().toString();

        if (TextUtils.isEmpty(body)) {
            e3.setError(REQUIRED);
            return;
        }

        // User data change listener
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);

                if (user == null) {
                    Log.e(TAG, "onDataChange: User data is null!");
                    Toast.makeText(FeedbackFragment.this, "onDataChange: User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                writeNewMessage(body,type);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read user!");
            }
        });

    }

    private void writeNewMessage(String body, String type) {
        Feedback feedback = new Feedback(getUsernameFromEmail(firebaseUser.getEmail()), body, type);

        Map<String, Object> feedbackValues = feedback.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = databaseReference.child("messages").push().getKey();

        childUpdates.put("/feedback/" + key, feedbackValues);

        databaseReference.updateChildren(childUpdates);
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
