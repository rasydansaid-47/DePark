package com.example.depark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    private TextView e3,e4;
    private Button b1;
    private FirebaseAuth firebaseAuth;
    private int counter = 5;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1 = (EditText)findViewById(R.id.txtUsername);
        e2 = (EditText)findViewById(R.id.txtPwd);
        e3 = (TextView)findViewById(R.id.txtInfo);
        e4 = (TextView)findViewById(R.id.txtRegister);
        b1 = (Button)findViewById(R.id.btnLogin);

        e3.setText("No of attempts remaining: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(e1.getText().toString(),e2.getText().toString());
            }
        });

        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String username, String userpasswrd){
        progressDialog.setMessage("Hello There!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username, userpasswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    counter--;
                    e3.setText("No of Attempts Remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0){
                        b1.setEnabled(false);
                    }
                }
            }
        });
    }
}