package com.example.hw4_knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CivicInfoAPIReader extends AsyncTask<String,Void,String> {
    private static final String TAG = "CivicInfoAPIReader";
    private MainActivity mainActivity;
    private String key = "AIzaSyA4Kk1ssYbf5SH6VxGbqV5QzlxlJuNjB1E";
    private String location;

    public CivicInfoAPIReader(MainActivity mainActivity) {  //Constructor
        this.mainActivity=mainActivity;
    }


    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: loaction: "+strings[0]);
        Uri urlBuilder =Uri.parse("https://www.googleapis.com/civicinfo/v2/representatives?key="+key+"&address="+strings[0]);

        String urlToUse =urlBuilder.toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }


        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        mainActivity.setList(s);
    }
}
