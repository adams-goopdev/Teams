package edu.ags.teams;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamMapActivity extends AppCompatActivity {
    public static final String TAG = "myDebug";
    LocationManager locationManager;
    LocationListener locationListener;
    final int PERMISSION_REQUEST_LOCATION = 1001;
    Team team;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_map);

        initListButton();
        initMapButton();
        initSettingsButton();
        initLookUpButton();
        initLookupFromLocation();
        initFindButton();
        initSaveButton();

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            initTeam(extras.getInt("teamId"));

        }

        this.setTitle("MAP");
    }

    private void initTeam(int teamId) {

        try {
            Log.d(TAG, "initTeam: "+ MainActivity.VEHICLETRACKERAPI + teamId);
            RestClient.executeGetOneRequest(MainActivity.VEHICLETRACKERAPI  + teamId, this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {
                            team = result.get(0);
                            RebindTeams();
                        }
                    });
        }
        catch (Exception e)
        {
            Log.d(TAG, "initTeam: " + e.getMessage());
        }

    }

    private void RebindTeams() {

        TextView editName = findViewById(R.id.textView_name);
        TextView textLat = findViewById(R.id.textView_latitude);
        TextView textLong = findViewById(R.id.textView_longitude);
        EditText editCity = findViewById(R.id.editText_city);

        editName.setText(team.getName());
        editCity.setText(team.getCity());

        textLat.setText(String.valueOf(team.getLatitude()));
        textLong.setText(String.valueOf(team.getLongitude()));

    }

    private void initSaveButton() {
        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(team !=null)
                {
                    TextView textViewLat = findViewById(R.id.textView_latitude);
                    TextView textViewLong = findViewById(R.id.textView_longitude);

                    team.setLatitude(Double.parseDouble(textViewLat.getText().toString()));
                    team.setLongitude(Double.parseDouble(textViewLong.getText().toString()));

                    try {
                        saveToAPI();
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onClick: " + e.getMessage());
                    }
                }
            }


        });
    }

    private void saveToAPI() {

        try {

                RestClient.executePutRequest(team,
                        MainActivity.VEHICLETRACKERAPI + team.getId(),
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Team> result) {
                                Log.d(TAG, "onSuccess: Put" + result);
                            }
                        });

        }
        catch (Exception e)
        {
            Log.d(TAG, "saveToAPI: " + e.getMessage());
        }


    }

    private void initFindButton() {
        Button button = findViewById(R.id.button_find);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(TeamMapActivity.this);
                List<Address> addressList = null;

                EditText editTextAddress = findViewById(R.id.editText_streetAddress);
                EditText editTextCity = findViewById(R.id.editText_city);
                EditText editTextState = findViewById(R.id.editText_state);
                EditText editTextZip = findViewById(R.id.editText_zip);


                String address = editTextAddress.getText().toString() + ", " +
                        editTextCity.getText().toString() + ", " +
                        editTextState.getText().toString() + " " +
                        editTextZip.getText().toString();

                try {
                    addressList = geocoder.getFromLocationName(address, 1);
                    TextView textViewLatitude = findViewById(R.id.textView_latitude);
                    TextView textViewLongitude = findViewById(R.id.textView_longitude);
                    textViewLatitude.setText(String.valueOf(addressList.get(0).getLatitude()));
                    textViewLongitude.setText(String.valueOf(addressList.get(0).getLongitude()));


                }
                catch (IOException e)
                {
                    Log.d(TAG, "GeoCodeError " + e.getMessage());
                }

            }
        });
    }

    private void initLookupFromLocation() {
        Button button = findViewById(R.id.button_fromlocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(View.INVISIBLE);

                try {

                    if(Build.VERSION.SDK_INT >= 23)
                    {
                        //Check for the manifest permission
                        if(ContextCompat.checkSelfPermission(TeamMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
                        {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(TeamMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                                Snackbar.make(findViewById(R.id.activity_main), "Teams requires this permission to place a call from the app.",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(TeamMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                    }
                                }).show();
                            }
                            else
                            {
                                ActivityCompat.requestPermissions(TeamMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                startLocationUpdates();
                            }
                        }
                        else
                        {
                            //Permission was previously granted
                            startLocationUpdates();
                        }
                    }
                    else{
                        startLocationUpdates();
                    }
                }
                catch (Exception e)
                {

                }

            }
        });
    }

    private void startLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PERMISSION_GRANTED )
        {
            Log.d(TAG, "startLocationUpdates: Permissions Problem");
            return;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d(TAG, "onLocationChanged: " + location.getLatitude() + " : " + location.getLongitude());

                    TextView textViewLatitude = findViewById(R.id.textView_latitude);
                    TextView textViewLongitude = findViewById(R.id.textView_longitude);
                    textViewLatitude.setText(String.valueOf((Math.round(location.getLatitude()*100000.0)/100000.0)));
                    textViewLongitude.setText(String.valueOf((Math.round(location.getLongitude()*100000.0)/100000.0)));

                }

            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
        }
        catch (Exception e)
        {
            Log.d(TAG, "startLocationUpdates: " + e.getMessage());
        }
    }
    private void stopLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PERMISSION_GRANTED )
        {
            Log.d(TAG, "startLocationUpdates: Permissions Problem");
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private void initLookUpButton() {
        Button button = findViewById(R.id.button_lookup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(View.VISIBLE);
            }
        });
    }

    private void changeVisibility(int visible) {
        EditText editText = findViewById(R.id.editText_streetAddress);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_city);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_state);
        editText.setVisibility(visible);

        editText = findViewById(R.id.editText_zip);
        editText.setVisibility(visible);

        Button button = findViewById(R.id.button_find);
        button.setVisibility(visible);
    }

    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamMapActivity.this, TeamSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamMapActivity.this, TeamMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamMapActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}