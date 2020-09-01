package com.example.depark;

import android.app.Activity;
import android.os.Bundle;
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

public class ValetFragment extends Activity {

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
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserValet userFeedback = snapshot.getValue(UserValet.class);
                e1.setText(userFeedback.getTypeCar());
                e2.setText(userFeedback.getUserCarPlate());
                e3.setText(userFeedback.getTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String typecar = e1.getText().toString();
                String carplate = e2.getText().toString();
                String time = e3.getText().toString();

                UserValet userValet = new UserValet(typecar, carplate, time);
                databaseReference.setValue(userValet);

                Toast.makeText(getApplicationContext(),"Valet Appointment been sended", Toast.LENGTH_SHORT).show();
            }
        });
    }
}