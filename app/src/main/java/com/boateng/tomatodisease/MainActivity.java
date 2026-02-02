package com.boateng.tomatodisease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.btn_camera);
        Button btnGallery = findViewById(R.id.btn_gallery);
        Button btnHistory = findViewById(R.id.btn_history);

        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra("source", "camera");  // Tell CameraActivity to open camera
            startActivity(intent);
        });

        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra("source", "gallery");  // Tell CameraActivity to open gallery
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            // For now, just go to Results with dummy data
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("disease", "No History");
            intent.putExtra("confidence", 0.0f);
            intent.putExtra("treatment", "History feature coming soon");
            intent.putExtra("info", "Scan some images first");
            startActivity(intent);
        });
    }
}