package com.example.hw4_knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    GovOfficial official;
    TextView location;
    TextView name;
    TextView party;
    TextView office;
    TextView address;
    TextView phone;
    TextView email;
    TextView website;
    ImageView facebook;
    ImageView twitter;
    ImageView googlePlus;
    ImageView youtube;
    ImageView photo;
    ImageView logo;
    ConstraintLayout layout;
    TextView fixedaddress;
    TextView fixedphone;
    TextView fixedwebsite;
    TextView fixedemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setTitle("Know Your Government");
        location = findViewById(R.id.Official_locationTextView);
        name = findViewById(R.id.Official_nameTextView);
        party = findViewById(R.id.Official_partyTextView);
        office = findViewById(R.id.Official_officeTextView);
        address = findViewById(R.id.Official_addressTextView);
        phone = findViewById(R.id.Official_phoneTextView);
        email = findViewById(R.id.Official_emailTextView);
        website = findViewById(R.id.Official_websiteTextView);
        facebook = findViewById(R.id.Official_facebookImageView);
        twitter =  findViewById(R.id.Official_twitterImageView);
        googlePlus = findViewById(R.id.Official_googleplusImageView);
        youtube = findViewById(R.id.Official_youtubeImageView);
        photo = findViewById(R.id.Official_photoImageView);
        fixedaddress = findViewById(R.id.Official_address);
        fixedemail = findViewById(R.id.Official_email);
        fixedphone = findViewById(R.id.Official_phone);
        fixedwebsite = findViewById(R.id.Official_website);
        layout = findViewById(R.id.constraintLayout);
        logo = findViewById(R.id.Official_partyImageView);

        Intent intent = getIntent();
        official = (GovOfficial) intent.getSerializableExtra("official");
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

        if (official.getPhotoUrl()!=null){
            loadImage(official.getPhotoUrl());
        }

        Linkify.addLinks(email,Linkify.ALL);
        Linkify.addLinks(website,Linkify.ALL);
        Linkify.addLinks(phone,Linkify.ALL);
        Linkify.addLinks(address,Linkify.ALL);
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
    private void setView(){
        if (official.getPhones()==null){
            phone.setVisibility(View.GONE);
            fixedphone.setVisibility(View.GONE);
        }
        if (official.getUrls()==null){
            website.setVisibility(View.GONE);
            fixedwebsite.setVisibility(View.GONE);
        }
        if (official.getEmails()==null){
            email.setVisibility(View.GONE);
            fixedemail.setVisibility(View.GONE);
        }
        if(official.getAddress()==null){
            address.setVisibility(View.GONE);
            fixedaddress.setVisibility(View.GONE);
        }
        if (official.getFacebook()==null){
            facebook.setVisibility(View.GONE);
        }
        if (official.getTwitter()==null){
            twitter.setVisibility(View.GONE);
        }
        if (official.getYoutube()==null){
            youtube.setVisibility(View.GONE);
        }
        if (official.getGooglePlus()==null){
            googlePlus.setVisibility(View.GONE);
        }
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

        party.setText("("+official.getParty()+")");
        office.setText(official.getOffice());
        name.setText(official.getName());
        phone.setText(official.getPhones());
        phone.setPaintFlags(phone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        website.setText(official.getUrls());
        website.setPaintFlags(website.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        email.setText(official.getEmails());
        email.setPaintFlags(email.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        address.setText(official.getAddress());
        address.setPaintFlags(address.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
    }

    public void imageClicked(View view) {
        if (official.getPhotoUrl()==null){
            return;
        }
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra("official",official);
        intent.putExtra("location",location.getText().toString());
        startActivity(intent);
    }

    public void youtubeClicked(View view) {
        String name = official.getYoutube();
        Intent youtubeIntent = null;
        try{
            youtubeIntent = new Intent(Intent.ACTION_VIEW);
            youtubeIntent.setPackage("com.google.android.youtube");
            youtubeIntent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(youtubeIntent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void googlePlusClicked(View view) {
        String name = official.getGooglePlus();
        Intent googlePlusIntent = null;
        try {
            googlePlusIntent = new Intent(Intent.ACTION_VIEW);
            googlePlusIntent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            googlePlusIntent.putExtra("customAppUri", name);
            startActivity(googlePlusIntent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void twitterClicked(View view) {
        Intent twitterIntent = null;
        String name =  official.getTwitter();
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitterIntent = new Intent(Intent.ACTION_VIEW);
            twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (PackageManager.NameNotFoundException e) {
            twitterIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/" + name));
        }
        startActivity(twitterIntent);
    }

    public void facebookClicked(View view) {
        String FACEBOOK_URL = "https://www.facebook.com/"+official.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode>=3002850){
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else {
                urlToUse = "fb://page/" + official.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

}
