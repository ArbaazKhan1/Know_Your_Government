package com.example.hw4_knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";
    private List<GovOfficial> govOfficialList = new ArrayList<>();
    private GovOfficialAdapter adapter;
    private RecyclerView recyclerView;
    private ConnectivityManager connectivityManager;
    private LocationManager locationManager;
    private Criteria criteria;
    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private String location;
    private EditText locationDialog;
    private TextView currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Know Your Government");
        currentLocation = findViewById(R.id.currentLocationTextView);
        recyclerView = findViewById(R.id.recycler);
        adapter = new GovOfficialAdapter(govOfficialList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!netCheck()){    //Checks for Network Connectivity
            noConnectionAlert();
            Log.d(TAG, "onCreate: Cannot Connect to Network");
        }
        else {
            Log.d(TAG, "onCreate: Network Connected");
            //The following is all work setup for location Permissions
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_LOCATION_REQUEST_CODE_ID);
            } else {

                setLocation();
                new CivicInfoAPIReader(this).execute(location); //Async Task for API Info
                adapter.notifyDataSetChanged();
            }

        }

    }


    /////////////////////////////////////////   Location Services (ask permision and set location)   ///////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
                new CivicInfoAPIReader(this).execute(location); //Async Task for API Info
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void setLocation() {
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
            return;
        }
        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            location = String.format(Locale.getDefault(),"%.4f, %.4f", currentLocation.getLatitude(), currentLocation.getLongitude());
            Log.d(TAG, "setLocation: "+location);
        } else {

            Log.d(TAG, "setLocation: Location Unavailable");
        }
    }


    ////////////////////////////   NetWork Connections   ///////////////////////////////////////

    private boolean netCheck(){
        if (connectivityManager==null){
            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager==null){
                Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            Toast.makeText(this, "Connected to Network", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            Toast.makeText(this, "Cannot Connect to Network", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //////////////////  Set up Array List of officials     ////////////////////////////////

    public void setList(String s) {  //this will take in the api call and set all the info into my govOfficials list
        try {
            JSONObject object = new JSONObject(s);
            JSONObject locationInfo = (JSONObject) object.get("normalizedInput");
            JSONArray officesArray = (JSONArray) object.get("offices");
            JSONArray officialsArray = (JSONArray) object.get("officials");
            String city = locationInfo.get("city").toString();
            String state = locationInfo.get("state").toString();
            String zip = locationInfo.get("zip").toString();
            currentLocation.setText(String.format(Locale.getDefault(), "%s, %s %s", city, state, zip));

            for(int i = 0; i < officesArray.length(); i++) {    //Will parse thru JSON Arrays and and read in relevant data for the GovOfficials objects
                JSONObject thisOffice = (JSONObject)officesArray.get(i);
                JSONArray indices = (JSONArray)thisOffice.get("officialIndices");
                for(int j = 0; j < indices.length(); j++) {
                    JSONObject thisCandidate = (JSONObject) officialsArray.get(indices.getInt(j));
                    JSONArray address = null;
                    String phones = null;
                    String urls = null;
                    String emails = null;
                    String photoUrl = null;
                    JSONArray channels = null;
                    if (thisCandidate.has("address")){
                        address = (JSONArray) thisCandidate.get("address");
                    }
                    if (thisCandidate.has("phones")){
                        JSONArray a = (JSONArray) thisCandidate.get("phones");
                        phones = a.getString(0);
                        Log.d(TAG, "setList: PHONES: "+phones+" Index"+j);
                    }
                    if (thisCandidate.has("urls")){
                        JSONArray u = (JSONArray) thisCandidate.get("urls");
                        urls =u.getString(0);
                        Log.d(TAG, "setList: URLS: "+urls+" Index: "+j);
                    }
                    if (thisCandidate.has("emails")){
                        JSONArray c = (JSONArray) thisCandidate.get("emails");
                        emails = c.getString(0);
                        Log.d(TAG, "setList: emails: "+emails+" Index"+j);
                    }
                    if (thisCandidate.has("photoUrl")){
                        photoUrl = thisCandidate.getString("photoUrl");
                        Log.d(TAG, "setList: Photo: "+photoUrl+" Index"+j);
                    }
                    if (thisCandidate.has("channels")){
                        channels = (JSONArray) thisCandidate.get("channels");
                    }

                    String name = thisCandidate.get("name").toString();
                    String party = thisCandidate.get("party").toString();
                    String office = thisOffice.get("name").toString();
                    GovOfficial newOfficial = new GovOfficial(name,party,office,address,phones,urls,emails,photoUrl,channels);
                    govOfficialList.add(newOfficial);
                }
                adapter.notifyDataSetChanged();
                System.out.println();
            }

        } catch(JSONException e) {
            System.out.println("Error retrieving data");
            e.printStackTrace();

        }
        Log.d(TAG, "setList: ");
    }

    //////////////////     Options MENU     ///////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.AboutItem){
            Log.d(TAG, "onOptionsItemSelected: Selected About Item");
            Intent intent =  new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
  
        if (item.getItemId()==R.id.SearchItem){
            Log.d(TAG, "onOptionsItemSelected: Selected Search Item ");
            if (!netCheck()){
                noConnectionAlert();
                return true;
            }
            locationDialog();
        }
        return true;
    }

///////////////////////////////    ON CLICK METHOD (WILL START SECOND ACTIVITY)   ////////////////////////
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        GovOfficial gov = govOfficialList.get(pos);
        Intent i = new Intent(this,OfficialActivity.class);
        i.putExtra("official",gov);
        i.putExtra("location",currentLocation.getText().toString());
        startActivity(i);
        Log.d(TAG, "onClick: Started new activity with Official:"+gov.getName());
    }

    ////////////////////////   Dialogs ////////////////////////////////////

    private void locationDialog() {
        Log.d(TAG, "locationDialog: ");
        setLocation();
        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.location_dialog,null);
        locationDialog = v.findViewById(R.id.locationDialogEditText);
        locationDialog.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                location = locationDialog.getText().toString().toUpperCase();
                if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onClick: Location permissions NOT granted");
                    return;
                }
                else {
                    govOfficialList.clear();
                    new CivicInfoAPIReader(MainActivity.this).execute(location);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: LocationDialog: Cancel");
                dialog.cancel();
            }
        });

        builder.setTitle("Enter a City, State, or a Zip Code");
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void noConnectionAlert() {
        Log.d(TAG, "noConnectionAlert: NO Internet Connection!");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NO Network Connection!");
        builder.setMessage("Unable to load Stock data.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
