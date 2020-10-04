package com.example.depark;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static final String TAG = "HomeFragment";
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, mRef;
    private FirebaseAuth firebaseAuth;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        mRef =  firebaseDatabase.getReference();
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);

                if (user == null) {
                    mRef.child("admin").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String admin = snapshot.child("name").getValue().toString();
                            mText.setValue("Welcome, " + admin);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    return;
                }
                mText.setValue("Welcome, " + user.getUserName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user!");
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }
}