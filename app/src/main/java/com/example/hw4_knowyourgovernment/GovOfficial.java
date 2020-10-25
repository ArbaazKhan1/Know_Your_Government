package com.example.hw4_knowyourgovernment;

import android.util.Log;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class GovOfficial implements Serializable {
    private String name;
    private String party;
    private String office;
    private String address;
    private String phones;
    private String urls;
    private String emails;
    private String photoUrl;
    private String googlePlus;
    private String twitter;
    private String facebook;
    private String youtube;
    private static final String TAG="GovOfficial";

    public GovOfficial(String name, String party, String office, JSONArray jsonAddress, String phones, String urls, String emails, String photoUrl, JSONArray jsonChannels) {
        this.name = name;
        this.party = party;
        this.office = office;
        this.photoUrl=photoUrl;
        this.phones=phones;
        this.urls=urls;
        this.emails=emails;
        parseAddress(jsonAddress);
        parseChannels(jsonChannels);
    }

    private void parseChannels(JSONArray jsonChannels) {
        if (jsonChannels==null){
            setYoutube(null);
            setGooglePlus(null);
            setFacebook(null);
            setTwitter(null);
            return;
        }
        try {
            for (int i = 0; i < jsonChannels.length(); i++){
                JSONObject object = (JSONObject) jsonChannels.get(i);

                switch (object.getString("type")){
                    case "GooglePlus":
                        setGooglePlus(object.getString("id"));
                        break;
                    case "Twitter":
                        setTwitter(object.getString("id"));
                        break;
                    case "Facebook":
                        setFacebook(object.getString("id"));
                        break;
                    case "YouTube":
                        setYoutube(object.getString("id"));
                        break;
                    default:
                        break;
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAddress(JSONArray jsonAddress) {
        if (jsonAddress==null){
            setAddress(null);
            return;
        }
        try {
            String s = null;
            for (int i=0;i<jsonAddress.length();i++){
                JSONObject obj = (JSONObject) jsonAddress.get(i);
                String line1 = obj.getString("line1");
                String line2 = obj.getString("line2");
                String city = obj.getString("city");
                String state = obj.getString("state");
                String zip = obj.getString("zip");
                s = line1+"\n"+line2+"\n"+city+", "+state+", "+zip;
                Log.d(TAG, "parseAddress: "+s);

            }
            setAddress(s);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getOffice() {
        return office;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
