package com.example.depark;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText e1,e2;
    TextView e3,e4;
    Button b1;
    FirebaseAuth firebaseAuth;
    int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button)findViewById(R.id.btnLogin);
        e1 = (EditText)findViewById(R.id.txtUsername);
        e2 = (EditText)findViewById(R.id.txtPwd);
        e3 = (TextView)findViewById(R.id.txtInfo);
        e4 = (TextView)findViewById(R.id.txtRegister);

        e3.setText("No of attempts remaining: 5");

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = e1.getText().toString();
                String password = e2.getText().toString();
                validate(username,password);
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
        if((username.equals("Admin")) && (userpasswrd.equals("1234"))){
            Toast.makeText(getApplicationContext(),"Successfully Login", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent2);
        }
        else{
            counter--;

            e3.setText("No of attempts remaining: " + String.valueOf(counter));

            if(counter == 0){
                b1.setEnabled(false);
            }
        }
    }
}