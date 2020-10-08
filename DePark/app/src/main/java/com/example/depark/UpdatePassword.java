package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    private Button b1;
    private ImageView b2;
    private EditText e1;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        e1 = findViewById(R.id.etNewPassword);
        b1 = findViewById(R.id.btnUpdatePassword);
        b2 = findViewById(R.id.backBtn2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPasswordNew = e1.getText().toString();
                firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdatePassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdatePassword.this, ProfileFragment.class));
                            finish();
                        }else{
                            Toast.makeText(UpdatePassword.this, "Password Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = getUsernameFromEmail(firebaseUser.getEmail());
                if(a == "admin"){
                    startActivity(new Intent(UpdatePassword.this, AdminProfileFragment.class));
                    finish();
                } else {
                    startActivity(new Intent(UpdatePassword.this, ProfileFragment.class));
                    finish();
                }
            }
        });
    }

    private String getUsernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}