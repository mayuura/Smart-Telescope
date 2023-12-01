package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TelescopeMountActivity extends AppCompatActivity {
    TextView instructionTextView;
    public static String mount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telecope_mount);
        instructionTextView = findViewById(R.id.instructionTextView);
        Button raDecButton = findViewById(R.id.raDecButton);
        Button altAzButton = findViewById(R.id.altAzButton);

        raDecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Ra/Dec mount is selected, pass the information to MainActivity
                Intent intent = new Intent(TelescopeMountActivity.this, MainActivity.class);
                intent.putExtra("mountType", "Ra/Dec");
                mount="Ra/Dec";
                startActivity(intent);
                finish();
            }
        });

        altAzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Alt-Az mount is selected, pass the information to MainActivity
                Intent intent = new Intent(TelescopeMountActivity.this, MainActivity.class);
                intent.putExtra("mountType", "Alt-Az");
                mount="Alt-Az";
                startActivity(intent);
                finish();
            }
        });
    }
    public static String getMount(){
        return mount;
    }
}
