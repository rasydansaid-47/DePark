package com.example.depark;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity{

    private EditText e1, e2, e3, e4;
    private TextView e5;
    private Button b1;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIView();

        firebaseAuth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(validate()){
                    String s1 = e2.getText().toString().trim();
                    String s2 = e3.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(s1,s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUIView() {
        e1 = (EditText) findViewById(R.id.txtName);
        e2 = (EditText) findViewById(R.id.txtEmail);
        e3 = (EditText) findViewById(R.id.txtPwd);
        e4 = (EditText) findViewById(R.id.txtCPwd);
        e5 = (TextView) findViewById(R.id.txtLogin);
        b1 = (Button) findViewById(R.id.btnRegister);
    }

    private Boolean validate(){
        boolean result = false;

        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();
        String s3 = e3.getText().toString();
        String s4 = e4.getText().toString();

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
}