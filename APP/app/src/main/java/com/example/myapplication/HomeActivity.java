package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        //.ImageView logoImageView = findViewById(R.id.logoImageView);
        TextView appNameTextView = findViewById(R.id.appNameTextView);
        TextView appDescriptionTextView = findViewById(R.id.appDescriptionTextView);
        Button getStartedButton = findViewById(R.id.getStartedButton);

        // Set onClickListener for Get Started Button
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the next activity or perform the desired action
                // For example, you can start the activity where the user chooses the telescope mount
                startActivity(new Intent(HomeActivity.this, TelescopeMountActivity.class));
            }
        });
    }
}

