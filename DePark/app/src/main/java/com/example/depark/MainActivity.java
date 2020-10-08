package com.example.depark;

import android.content.Intent;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private DatabaseReference databaseReference;


    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    TextView t1, t2;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View navHeaderView = navigationView.getHeaderView(0);
        img = navHeaderView.findViewById(R.id.imageView_nav);
        t1 = navHeaderView.findViewById(R.id.tv_nav_Name);
        t2 = navHeaderView.findViewById(R.id.tv_nav_Email);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);

                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    Toast.makeText(MainActivity.this, "User data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                t1.setText(user.getUserName());
                t2.setText(user.getUserEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user!");
            }
        });

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

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_qrcode, R.id.nav_time, R.id.nav_receipt_list,
                R.id.nav_profile, R.id.nav_feedback, R.id.nav_logout)
                .setDrawerLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.nav_home:
                        startActivity(new Intent(MainActivity.this,HomeFragment.class));
                        break;
                    case R.id.nav_qrcode:
                        startActivity(new Intent(MainActivity.this,QrCodeFragment.class));
                        break;
                    case R.id.nav_valet:
                        startActivity(new Intent(MainActivity.this,ValetFragment.class));
                        break;
                    case R.id.nav_time:
                        startActivity(new Intent(MainActivity.this,TimeFragment.class));
                        break;
                    case R.id.nav_receipt_list:
                        startActivity(new Intent(MainActivity.this,ReceiptListFragment.class));
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(MainActivity.this,ProfileFragment.class));
                        break;
                    case R.id.nav_feedback:
                        startActivity(new Intent(MainActivity.this, FeedbackFragment.class));
                        break;
                    case R.id.nav_logout:
                        logout();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout() {
        firebaseAuth.getInstance().signOut();
        Intent toMain = new Intent(getApplicationContext(), LoginActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toMain);
    }



}
