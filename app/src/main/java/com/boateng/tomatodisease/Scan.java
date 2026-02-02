package com.boateng.tomatodisease;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Scan {
    private int id;
    private String disease;
    private float confidence;
    private String treatment;
    private String info;
    private String timestamp;

    public Scan() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
    public float getConfidence() { return confidence; }
    public void setConfidence(float confidence) { this.confidence = confidence; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getFormattedTimestamp() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return timestamp;
        }
    }

    public String getConfidenceString() {
        return String.format("%.1f%%", confidence);
    }

    public Bitmap getImage() {
        return null;
    }
}