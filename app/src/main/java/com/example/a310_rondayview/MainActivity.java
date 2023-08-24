package com.example.a310_rondayview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a310_rondayview.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String TAG = "FirestoreTest";
    private FirebaseAuth mAuth;

    // TODO ("replaceFragment(new CreateEventFragment());") replace CreateEventFragment with
    // the homepage once the homepage is made
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to login
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish(); // Prevents users from coming back to this activity when back button is pressed
            return;
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());
        ImageButton heartButton = findViewById(R.id.heartButton);
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LikedActivity.class);
                startActivity(intent);
            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.create) {
                replaceFragment(new CreateEventFragment());
            } else if (item.getItemId() == R.id.browse) {
                replaceFragment(new FragmentHome());
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new FragmentAccount());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
