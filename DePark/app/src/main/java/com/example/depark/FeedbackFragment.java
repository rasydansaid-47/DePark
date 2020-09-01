package com.example.depark;

import android.app.Activity;
import android.os.Bundle;
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

public class FeedbackFragment extends Activity implements AdapterView.OnItemSelectedListener {

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
        e1 = (EditText) findViewById(R.id.etUserName);
        e2 = (EditText) findViewById(R.id.etUserEmail);
        e3 = (EditText) findViewById(R.id.etMessage);
        s1 = (Spinner) findViewById(R.id.typeFeedback);
        b1 = (Button) findViewById(R.id.btnSend);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TypeofFeedback);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserFeedback userFeedback = snapshot.getValue(UserFeedback.class);
                e1.setText(userFeedback.getUserName());
                e2.setText(userFeedback.getUserEmail());
                e3.setText(userFeedback.getUserMessage());
                userFeedback.getTypeFeedback(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = e1.getText().toString();
                String email = e2.getText().toString();
                String message = e3.getText().toString();

                UserFeedback userFeedback = new UserFeedback(email, name, message, type);
                databaseReference.setValue(userFeedback);

                firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email Update", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Email Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
}
