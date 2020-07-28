package com.example.depark;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
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

    EditText e1, e2, e3, e4, e5;
    Button b1;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e1 = (EditText) findViewById(R.id.txtName);
        e2 = (EditText) findViewById(R.id.txtEmail);
        e3 = (EditText) findViewById(R.id.txtPhonenumber);
        e4 = (EditText) findViewById(R.id.txtPwd);
        e5 = (EditText) findViewById(R.id.txtCPwd);
        b1 = (Button) findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        TextView login = (TextView)findViewById(R.id.lnkRegister);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        b1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String s1 = e1.getText().toString().trim();
                String s2 = e2.getText().toString().trim();
                String s3 = e3.getText().toString().trim();
                String s4 = e4.getText().toString().trim();
                String s5 = e5.getText().toString().trim();

                validate();

                firebaseAuth .createUserWithEmailAndPassword(s1,s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Registered Failed Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }

    private boolean validate(){
        boolean result = false;
        String s1 = e1.getText().toString().trim();
        String s2 = e2.getText().toString().trim();
        String s3 = e3.getText().toString().trim();
        String s4 = e4.getText().toString().trim();
        String s5 = e5.getText().toString().trim();

        if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals("")||s5.equals("")){
            Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }
        return result;
    }
}