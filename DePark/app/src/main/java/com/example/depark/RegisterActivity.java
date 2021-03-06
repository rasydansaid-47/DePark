package com.example.depark;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

public class RegisterActivity extends AppCompatActivity{

    private EditText e1, e2, e3, e4;
    private TextView t1;
    private Button b1;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String s1,s2,s3,s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIView();

        firebaseAuth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(validate()){
                    s1 = e1.getText().toString().trim();
                    s2 = e2.getText().toString().trim();
                    s3 = e3.getText().toString().trim();

                    if (TextUtils.isEmpty(s2)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(s3)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (s3.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.createUserWithEmailAndPassword(s2,s3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registered Failed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                sendEmailVerification(s1,s2);
                                Toast.makeText(RegisterActivity.this, "Registered Successful, Upload completed!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUIView() {
        e1 = findViewById(R.id.etUserName);
        e2 = findViewById(R.id.etEmail);
        e3 = findViewById(R.id.pass);
        e4 = findViewById(R.id.confirmPass);
        t1 = findViewById(R.id.txtLogin);
        b1 = findViewById(R.id.regBtn);
    }

    private Boolean validate(){
        boolean result = false;

        s1 = e1.getText().toString();
        s2 = e2.getText().toString();
        s3 = e3.getText().toString();
        s4 = e4.getText().toString();

        if(s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
        }
        else if(s3.equals(s4) != true){
            Toast.makeText(getApplicationContext(), "Password and Confirm Passsword are not same", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }

    private void sendEmailVerification(final String username, final String useremail){
        final FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //sendUserData(username,useremail);
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        writeNewUser(firebaseUser.getUid(), username, useremail);
                        Toast.makeText(RegisterActivity.this, "Registered Successful, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Verification mail has not been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /*private void sendUserData(final String username, final String useremail){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(username,useremail);
        myRef.setValue(userProfile);
    }*/

    private void writeNewUser(String userId, String username, String email) {
        UserProfile user = new UserProfile(username, email);

        firebaseDatabase.getInstance().getReference().child("users").child(userId).setValue(user);
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}