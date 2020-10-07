package com.example.depark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AdminHomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ImageView img;
    private Button b1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        final TextView textView = root.findViewById(R.id.textView2);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        img = root.findViewById(R.id.imageView2);
        b1 = root.findViewById(R.id.btnCheck);

        storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).

                child("Images/Profile Pic").

                getDownloadUrl().

                addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess (Uri uri){
                        Picasso.get().load(uri).fit().centerCrop().into(img);
                    }
                });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminScanActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}