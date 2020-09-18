package com.example.depark;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
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

@SuppressWarnings("unused")
public class ProfileFragment extends Activity {

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
    ImageButton b1, b2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        img = findViewById(R.id.imgProfile);
        t1 = findViewById(R.id.tvName);
        t2 = findViewById(R.id.tvEmail);
        b1 = findViewById(R.id.btnUpdate);
        b2 = findViewById(R.id.btnChange);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");

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

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                t1.setText("Name: " + userProfile.getUserName());
                t2.setText("Email: " + userProfile.getUserEmail());
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener()
    {
        public void onClick (View view){
            startActivity(new Intent(ProfileFragment.this, UpdateProfileActivity.class));
        }
        });

        b2.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view){
            startActivity(new Intent(ProfileFragment.this, UpdatePassword.class));
        }
        });
    }
}