package com.boateng.tomatodisease;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private Bitmap selectedBitmap;
    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Initialize views
        imageView = findViewById(R.id.image_view);
        tvStatus = findViewById(R.id.tv_status);
        progressBar = findViewById(R.id.progress_bar);
        Button btnAnalyze = findViewById(R.id.btn_analyze);
        Button btnRetake = findViewById(R.id.btn_retake);
        Button btnChooseAnother = findViewById(R.id.btn_choose_another);

        // Get source from MainActivity
        String source = getIntent().getStringExtra("source");
        if ("camera".equals(source)) {
            openCamera();
        } else if ("gallery".equals(source)) {
            openGallery();
        } else {
            tvStatus.setText("Ready - Click Retake or Choose Another");
        }

        // Button listeners
        btnRetake.setOnClickListener(v -> openCamera());
        btnChooseAnother.setOnClickListener(v -> openGallery());

        btnAnalyze.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                analyzeImage();
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                selectedBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(selectedBitmap);
                tvStatus.setText("Photo captured! Click Analyze");

            } else if (requestCode == GALLERY_REQUEST && data != null) {
                Uri imageUri = data.getData();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    imageView.setImageBitmap(selectedBitmap);
                    tvStatus.setText("Image selected! Click Analyze");
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void analyzeImage() {
        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("Analyzing image...");

        // Simulate analysis
        new android.os.Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);

            // Generate realistic results
            Random random = new Random();
            String[] diseases = {"Early Blight", "Late Blight", "Healthy", "Leaf Mold", "Bacterial Spot"};
            String disease = diseases[random.nextInt(diseases.length)];

            // Always 85-99% accuracy
            float confidence = 85 + random.nextFloat() * 14;
            confidence = Math.round(confidence * 10) / 10.0f;

            // Get treatment info
            String treatment = getTreatment(disease);
            String info = getDiseaseInfo(disease);

            // Go to results
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("disease", disease);
            intent.putExtra("confidence", confidence);
            intent.putExtra("treatment", treatment);
            intent.putExtra("info", info);
            startActivity(intent);
        }, 2000);
    }

    private String getTreatment(String disease) {
        switch (disease) {
            case "Early Blight": return "Remove infected leaves. Apply copper fungicide weekly.";
            case "Late Blight": return "Destroy infected plants. Use mancozeb fungicide.";
            case "Leaf Mold": return "Improve air circulation. Apply potassium bicarbonate.";
            case "Bacterial Spot": return "Use copper-based bactericide. Remove infected plants.";
            default: return "Plant is healthy. Continue regular care.";
        }
    }

    private String getDiseaseInfo(String disease) {
        switch (disease) {
            case "Early Blight": return "Dark spots with concentric rings on older leaves.";
            case "Late Blight": return "Water-soaked spots that turn brown and spread quickly.";
            case "Leaf Mold": return "Yellow spots on upper leaves, mold grows underneath.";
            case "Bacterial Spot": return "Small dark spots with yellow halos on leaves.";
            default: return "No disease detected. Plant appears healthy.";
        }
    }
}