package com.example.depark;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity{

    DatabaseHelper db;
    EditText e1, e2, e3, e4, e5;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.txtName);
        e2 = (EditText) findViewById(R.id.txtEmail);
        e3 = (EditText) findViewById(R.id.txtPhonenumber);
        e4 = (EditText) findViewById(R.id.txtPwd);
        e5 = (EditText) findViewById(R.id.txtCPwd);
        b1 = (Button) findViewById(R.id.btnLogin);

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
                String s1 = e1.getText().toString();
                String s4 = e2.getText().toString();
                String s5 = e3.getText().toString();
                String s2 = e4.getText().toString();
                String s3 = e5.getText().toString();

                if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals("")||s5.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if(s2.equals(s3))
                    {
                        Boolean chkusername = db.chkusername(s1);
                        if(chkusername)
                        {
                            Boolean insert = db.insert_USER(s1,s2,s4,s5);
                            if(insert)
                            {
                                Toast.makeText(getApplicationContext(),"Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Registered Failed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}