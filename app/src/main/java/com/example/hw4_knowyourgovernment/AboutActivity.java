package com.example.hw4_knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //The following is to allow for the about page to have text with a link
        TextView url = findViewById(R.id.url);
        Spanned text = Html.fromHtml("<a href='https://developers.google.com/civic-information/'>Google Civic Information API</a>");
        url.setMovementMethod(LinkMovementMethod.getInstance());
        url.setText(text);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
