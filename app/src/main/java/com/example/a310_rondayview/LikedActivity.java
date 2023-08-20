package com.example.a310_rondayview;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LikedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        // Set the toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up (Back) button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false); // Hide the title from the action bar
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24); // Set your custom back arrow icon
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // This method is called when the up button is pressed
        return true;
    }
}
