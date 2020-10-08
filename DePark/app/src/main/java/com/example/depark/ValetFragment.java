package com.example.depark;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ValetFragment extends Activity implements AdapterView.OnItemSelectedListener  {

    private static final String TAG = "FeedbackFragment";
    private static final String REQUIRED = "Required";
    private DatabaseReference databaseReference;
    private DatabaseReference mRef;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TimePickerDialog picker;
    EditText e1, e2, e3;
    Button b1;
    Spinner s1;
    String[] TypeofCar = { "Type of Car", "Nissan", "Mazda", "Tesla", "Rolls Royce", "BMW"};
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_valet);

        s1 = findViewById(R.id.etTypeCar);
        e2 = findViewById(R.id.etCarPlate);
        e3 = findViewById(R.id.etTime);
        b1 = findViewById(R.id.btnSubmit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        mRef = firebaseDatabase.getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TypeofCar);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(ValetFragment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int sHour, int sMinute) {
                                e3.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitValet();
                Toast.makeText(getApplicationContext(),"Valet Appointment has been sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitValet(){
        final String carplate = e2.getText().toString();
        final String time = e3.getText().toString();

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
                writeNewMessage(type, carplate, time);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = TypeofCar[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Invalid Input",Toast.LENGTH_SHORT).show();
    }
}