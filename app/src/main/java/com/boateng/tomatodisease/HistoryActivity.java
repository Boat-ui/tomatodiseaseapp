package com.boateng.tomatodisease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout container = findViewById(R.id.history_container);
        Button btnBack = findViewById(R.id.btn_back);

        // Get saved scans
        android.content.SharedPreferences prefs = getSharedPreferences("scan_history", MODE_PRIVATE);
        int scanCount = prefs.getInt("scan_count", 0);

        if (scanCount == 0) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No scan history yet.\nTake photos and analyze them to see history here!");
            emptyText.setTextSize(18);
            emptyText.setGravity(android.view.Gravity.CENTER);
            emptyText.setPadding(0, 100, 0, 0);
            container.addView(emptyText);
        } else {
            // Show scans in reverse order (newest first)
            for (int i = scanCount; i >= 1; i--) {
                String disease = prefs.getString("scan_" + i + "_disease", "");
                float confidence = prefs.getFloat("scan_" + i + "_confidence", 0);
                String treatment = prefs.getString("scan_" + i + "_treatment", "");
                String info = prefs.getString("scan_" + i + "_info", "");
                long timeMillis = prefs.getLong("scan_" + i + "_time", System.currentTimeMillis());

                // Create scan card
                LinearLayout scanCard = new LinearLayout(this);
                scanCard.setOrientation(LinearLayout.VERTICAL);
                scanCard.setPadding(30, 20, 30, 20);
                scanCard.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                // Disease
                TextView tvDisease = new TextView(this);
                tvDisease.setText("🍅 " + disease);
                tvDisease.setTextSize(18);
                if (disease.equals("Healthy")) {
                    tvDisease.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    tvDisease.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }

                // Confidence
                TextView tvConfidence = new TextView(this);
                tvConfidence.setText("Confidence: " + String.format("%.1f%%", confidence));
                tvConfidence.setTextSize(16);

                // Time
                TextView tvTime = new TextView(this);
                String timeStr = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                        .format(new Date(timeMillis));
                tvTime.setText("Scanned: " + timeStr);
                tvTime.setTextSize(12);
                tvTime.setTextColor(getResources().getColor(android.R.color.darker_gray));

                scanCard.addView(tvDisease);
                scanCard.addView(tvConfidence);
                scanCard.addView(tvTime);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 20);
                scanCard.setLayoutParams(params);

                container.addView(scanCard);
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}