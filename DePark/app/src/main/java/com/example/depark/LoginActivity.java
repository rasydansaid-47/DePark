package com.example.depark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText e1,e2;
    private TextView t1,t2;
    private Button b1;
    private FirebaseAuth firebaseAuth;
    private int counter = 5;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1 = findViewById(R.id.txtUsername);
        e2 = findViewById(R.id.txtPwd);
        t1 = findViewById(R.id.txtRegister);
        t2 = findViewById(R.id.txtForgotPasswrd);
        b1 = findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();

                if (TextUtils.isEmpty(s1)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(s2)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                validate(s1,s2);
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });
    }

    private void validate(String username, String userpasswrd){
        progressDialog.setMessage("Hello There!");
        progressDialog.show();

        if(username.equals("rasydansaidazmirais@gmail.com")) {
            firebaseAuth.signInWithEmailAndPassword(username, userpasswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        checkEmailVerification();
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        counter--;
                        Toast.makeText(getApplicationContext(), "No of attempts remaining: " + counter, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if (counter == 0) {
                            b1.setEnabled(false);
                        }
                    }
                }
            });
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(username, userpasswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        checkEmailVerification();
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        counter--;
                        Toast.makeText(getApplicationContext(), "No of attempts remaining: " + counter, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if (counter == 0) {
                            b1.setEnabled(false);
                        }
                    }
                }
            });
        }
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
           //startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else{
            Toast.makeText(LoginActivity.this,"Verify your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}