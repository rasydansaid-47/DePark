package com.example.depark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AdminProfileFragment extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //Firebase storage
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    String name, address, phone, image;
    ImageView img;
    TextView t1, t2;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_fragment);

        img = findViewById(R.id.imgProfile);
        t1 = findViewById(R.id.tvName);
        t2 = findViewById(R.id.tvEmail);
        b1 = findViewById(R.id.btnPassword);
        b2 = findViewById(R.id.btnProfile);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("admin");

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).
                child("Images/Profile Pic").
                getDownloadUrl().
                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess (Uri uri){
                        Picasso.get().load(uri).fit().centerCrop().into(img);
                    }
                });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                t1.setText("Name: " + name);
                t2.setText("Email: " + email);
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick (View view){
                startActivity(new Intent(AdminProfileFragment.this, UpdatePassword.class));
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                startActivity(new Intent(AdminProfileFragment.this, AdminUpdateProfile.class));
            }
        });
    }
}