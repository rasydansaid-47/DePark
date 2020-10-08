package com.example.depark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainAdminActivity extends AppCompatActivity {

    private static final String TAG = "MainAdminActivity";
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
        setContentView(R.layout.activity_main_admin);
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
        databaseReference = firebaseDatabase.getReference("admin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                t1.setText(name);
                t2.setText(email);
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
                R.id.nav_home, R.id.nav_booking_list, R.id.nav_feedback_list, R.id.nav_customer_list,
                R.id.nav_valet_list, R.id.nav_customer_list, R.id.nav_profile,  R.id.nav_logout)
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
                        startActivity(new Intent(MainAdminActivity.this,HomeFragment.class));
                        break;
                    case R.id.nav_customer_list:
                        startActivity(new Intent(MainAdminActivity.this,CustomerListFragment.class));
                        break;
                    case R.id.nav_booking_list:
                        startActivity(new Intent(MainAdminActivity.this,BookingListFragment.class));
                        break;
                    case R.id.nav_valet_list:
                        startActivity(new Intent(MainAdminActivity.this,ValetListFragment.class));
                        break;
                    case R.id.nav_condition_list:
                        startActivity(new Intent(MainAdminActivity.this,ConditionListFragment.class));
                        break;
                    case R.id.nav_feedback_list:
                        startActivity(new Intent(MainAdminActivity.this,FeedbackListFragment.class));
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(MainAdminActivity.this,AdminProfileFragment.class));
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