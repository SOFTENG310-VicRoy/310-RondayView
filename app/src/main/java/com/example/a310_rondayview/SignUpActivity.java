// Represents the activity for user sign-up
package com.example.a310_rondayview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Get references to UI elements
        EditText editTextEmailSignUp = findViewById(R.id.editTextEmailSignUp);
        EditText editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvSignIn = findViewById(R.id.tvSignIn);

        // Handle sign-up button click
        btnSignUp.setOnClickListener(v -> {
            // Extract email and password from input fields
            String email = editTextEmailSignUp.getText().toString().trim();
            String password = editTextPasswordSignUp.getText().toString().trim();

            // Attempt to sign up with provided credentials
            signUp(email, password);
        });

        // Handle sign-in text click for navigation
        tvSignIn.setOnClickListener(v -> {
            // Navigate to SignInActivity
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    // Method to sign up using Firebase authentication
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up was successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();

                        // Store user details in Firestore
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("email", email);
                        // Add any other details, for example:
                        // userMap.put("name", name);

                        db.collection("users").document(user.getUid())
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    // Navigate to main activity after successful data storage
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Remove SignUpActivity from back stack
                                })
                                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Error saving user details.", Toast.LENGTH_SHORT).show());

                    } else {
                        // Registration failed, display an error message
                        Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}