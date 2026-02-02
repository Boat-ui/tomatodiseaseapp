package com.boateng.tomatodisease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Get results
        String disease = getIntent().getStringExtra("disease");
        float confidence = getIntent().getFloatExtra("confidence", 0);
        String treatment = getIntent().getStringExtra("treatment");
        String info = getIntent().getStringExtra("info");

        // Update UI
        TextView tvDisease = findViewById(R.id.tv_disease);
        TextView tvConfidence = findViewById(R.id.tv_confidence);
        TextView tvTreatment = findViewById(R.id.tv_treatment);
        TextView tvInfo = findViewById(R.id.tv_info);
        TextView tvTimestamp = findViewById(R.id.tv_timestamp);

        Button btnSave = findViewById(R.id.btn_save);
        Button btnNewScan = findViewById(R.id.btn_new_scan);
        Button btnLearnMore = findViewById(R.id.btn_learn_more);

        tvDisease.setText(disease);
        tvConfidence.setText(String.format("Confidence: %.1f%%", confidence));
        tvTreatment.setText(treatment);
        tvInfo.setText(info);

        // Set current timestamp
        String currentTime = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                .format(new Date());
        tvTimestamp.setText("Scan Time: " + currentTime);

        // Button listeners
        btnSave.setOnClickListener(v -> {
            Toast.makeText(this, "Result saved to history!", Toast.LENGTH_SHORT).show();
        });

        btnNewScan.setOnClickListener(v -> finish());

        btnLearnMore.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://extension.umn.edu/diseases/tomato-diseases"));
            startActivity(browserIntent);
        });
    }
}