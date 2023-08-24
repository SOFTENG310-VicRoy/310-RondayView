package com.example.a310_rondayview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a310_rondayview.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String TAG = "FirestoreTest";
    private FirebaseFirestore firestore;
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

        // example
        firestore = FirebaseFirestore.getInstance();

        // Data to be added to the new document
        Map<String, Object> newEventData = new HashMap<>();
        newEventData.put("eventName", "Dummy Test");
        newEventData.put("eventDate", "Test Date");

        addNewEvent(newEventData);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void addNewEvent(Map<String, Object> eventData) {
        firestore.collection("events").add(eventData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "New event added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding new event", e);
                    }
                });
    }
}