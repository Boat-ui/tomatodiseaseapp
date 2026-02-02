package com.boateng.tomatodisease;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScanDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_detail);

        int scanId = getIntent().getIntExtra("scanId", -1);

        if (scanId == -1) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Scan> allScans = dbHelper.getAllScans();

        Scan selectedScan = null;
        for (Scan scan : allScans) {
            if (scan.getId() == scanId) {
                selectedScan = scan;
                break;
            }
        }

        if (selectedScan == null) {
            finish();
            return;
        }

        // Update UI with scan details
        TextView tvDisease = findViewById(R.id.tv_disease);
        TextView tvConfidence = findViewById(R.id.tv_confidence);
        TextView tvTreatment = findViewById(R.id.tv_treatment);
        TextView tvInfo = findViewById(R.id.tv_info);
        TextView tvTimestamp = findViewById(R.id.tv_timestamp);
        ImageView ivImage = findViewById(R.id.iv_image);
        Button btnBack = findViewById(R.id.btn_back);

        tvDisease.setText(selectedScan.getDisease());
        tvConfidence.setText(String.format("Confidence: %.1f%%", selectedScan.getConfidence()));
        tvTreatment.setText(selectedScan.getTreatment());
        tvInfo.setText(selectedScan.getInfo());
        tvTimestamp.setText("Scanned on: " + selectedScan.getFormattedTimestamp());

        // Set image
        Bitmap image = selectedScan.getImage();
        if (image != null) {
            ivImage.setImageBitmap(image);
        } else {
            // ivImage.setImageResource(R.drawable.ic_tomato);
        }

        // Set disease-specific color
        int colorRes;
        if (selectedScan.getDisease().equals("Healthy")) {
            colorRes = R.color.healthy;
        } else {
            colorRes = R.color.diseased;
        }
        tvDisease.setBackgroundColor(getResources().getColor(colorRes));

        btnBack.setOnClickListener(v -> finish());
    }
}