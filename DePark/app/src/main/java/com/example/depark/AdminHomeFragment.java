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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AdminHomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ImageView img;
    private Button b1;
    private CardView parkingCard, receiptCard, feeCard, valetCard;

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

        // defining cards
        parkingCard = root.findViewById(R.id.eparking_card);
        receiptCard = root.findViewById(R.id.receipt_card);
        feeCard = root.findViewById(R.id.fee_card);
        valetCard = root.findViewById(R.id.valet_card);

        // add click listener to the cards
        parkingCard.setOnClickListener(this);
        receiptCard.setOnClickListener(this);
        feeCard.setOnClickListener(this);
        valetCard.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.eparking_card : i = new Intent (getActivity(), BookingListFragment.class); startActivity(i); break;
            case R.id.receipt_card : i = new Intent (getActivity(), PaymentListFragment.class); startActivity(i); break;
            case R.id.fee_card : i = new Intent (getActivity(), CustomerListFragment.class); startActivity(i); break;
            case R.id.valet_card : i = new Intent (getActivity(), ValetListFragment.class); startActivity(i); break;
            default:break;
        }

    }
}