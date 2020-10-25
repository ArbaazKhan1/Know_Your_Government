package com.example.hw4_knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailActivity";
    private GovOfficial official;
    private TextView location;
    private TextView name;
    private TextView office;
    private ImageView photo;
    private ImageView logo;
    private ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        setTitle("Know Your Government");
        location = findViewById(R.id.Photo_locationTextView);
        name = findViewById(R.id.Photo_nameTextView);
        office = findViewById(R.id.Photo_officeTextView);
        photo = findViewById(R.id.Photo_photoImageView);
        logo = findViewById(R.id.Photo_logoImageView);
        layout = findViewById(R.id.Photo_layout);

        Intent intent = getIntent();
        if(intent.hasExtra("location")){
            String s = intent.getStringExtra("location");
            location.setText(s);
            Log.d(TAG, "onCreate: Reading Location "+s);
        }
        if(intent.hasExtra("official")){
            official = (GovOfficial) intent.getSerializableExtra("official");
            Log.d(TAG, "onCreate: Reading Official: "+official.getName()+" info");
        }
        setView();
        loadImage(official.getPhotoUrl());
    }

    private void loadImage(final String imageURL) {
        // Needs gradle  implementation 'com.squareup.picasso:picasso:2.71828'

        Log.d(TAG, "loadImage: " + imageURL);

        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(photo);
    }

    private void setView() {
        name.setText(official.getName());
        office.setText(official.getOffice());
        switch (official.getParty()){
            case "Republican Party":
                layout.setBackgroundColor(Color.RED);
                logo.setImageResource(R.drawable.rep_logo);
                break;
            case "Democratic Party":
                layout.setBackgroundColor(Color.BLUE);
                logo.setImageResource(R.drawable.dem_logo);
                break;
            default:
                layout.setBackgroundColor(Color.BLACK);
                logo.setVisibility(View.GONE);
                break;
        }
    }
}
