package edu.ags.teams;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.ags.teams.FileIO;
import edu.ags.teams.R;
import edu.ags.teams.RaterDialog;
import edu.ags.teams.Team;
import edu.ags.teams.TeamDataSource;
import edu.ags.teams.TeamListActivity;
import edu.ags.teams.TeamMapActivity;
import edu.ags.teams.TeamSettingsActivity;

public class MainActivity extends AppCompatActivity  implements  RaterDialog.SaveRatingListener, OnMapReadyCallback {
    public static final String TAG = "myDebug";
    private static final int PERMISSION_REQUEST_PHONE = 102;
    public static final String VEHICLETRACKERAPI = "https://vehicletrackerapi.azurewebsites.net/api/Team/";
    private static final int PERMISSION_REQUEST_CAMERA = 103;
    private static final int CAMERA_REQUEST = 1888;
    Team team;
    ArrayList<Team> teams;

    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initMapButton();
        initSettingsButton();
        initRatingButton();
        setForEditing(false);
        initToggleButton();
        initTextChangedEvents();
        initSaveButton();
        initCallFunction();
        initImageButton();


        Bundle extras = getIntent().getExtras();

        //ReadFromTextFile();

        if(extras != null) {
            // Edit an existing team
            Log.d(TAG, "onCreate: " + extras.getInt("teamId"));
            initTeam(extras.getInt("teamId"));
        }
        else {
            // Make a new one.
            team = new Team();
            Log.d(TAG, "onCreate: New Team");
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.setTitle("MainActivity");
    }



    private void initImageButton() {
        ImageButton ib = findViewById(R.id.imgPhoto);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= 23)
                {
                    //Check for the manifest permission
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PERMISSION_GRANTED)
                    {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){
                            Snackbar.make(findViewById(R.id.activity_main), "Teams requires this permission to place a take a picture from the app.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                                }
                            }).show();
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                            takePhoto();
                        }
                    }
                    else
                    {
                        //Permission was previously granted
                        takePhoto();
                    }
                }
                else{
                    takePhoto();
                }

            }
        });
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST)
            if(resultCode == RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144,144,true);
                ImageButton imageButton = findViewById(R.id.imgPhoto);
                imageButton.setImageBitmap(scaledPhoto);
                team.setPhoto(scaledPhoto);

            }
    }

    private void initCallFunction() {
        EditText editCell = findViewById(R.id.editCell);

        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                checkPhonePermission(team.getCellNumber());
                return false;
            }
        });

    }

    private void checkPhonePermission(String cellNumber) {
        //Check the API version to decide if more permissions are necessary
        if(Build.VERSION.SDK_INT >= 23)
        {
            //Check for the manifest permission
            if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)){
                    Snackbar.make(findViewById(R.id.activity_main), "Teams requires this permission to place a call from the app.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_PHONE);
                        }
                    }).show();
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_PHONE);
                    callTeam(cellNumber);
                }
            }
            else
            {
                //Permission was previously granted
                callTeam(cellNumber);
            }
        }
        else{
            callTeam(cellNumber);
        }

    }

    private void callTeam(String cellNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + cellNumber));
        startActivity(intent);
    }

    private void WriteToTextFile() {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String[teams.size()];
        for (Team t : teams) data[counter++] = t.toString();
        fileIO.writeFile(this, data);
    }

    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TeamDataSource ds = new TeamDataSource(MainActivity.this);

                if(team.getId() == -1){
                    try{
                        //ds.open();
                        //boolean result = ds.insert(team);
                        saveToAPI(true);
                        Log.d(TAG, "SaveToDatabase: Saved: " + team.toString());
                    }
                    catch(Exception ex)
                    {
                        Log.d(TAG, "SaveToDatabase: " + ex.getMessage());
                    }

                }
                else {
                    try{
                        //ds.open();
                        //boolean result = ds.update(team);
                        saveToAPI(false);
                        Log.d(TAG, "SaveToDatabase: Saved: " + team.toString());
                    }
                    catch(Exception ex)
                    {
                        Log.d(TAG, "SaveToDatabase: " + ex.getMessage());
                    }
                    Log.d(TAG, "onClick: Update Team: " + team.toString());
                   // teams.set(team.getId() - 1, team);
                }


                //WriteToTextFile();

                Log.d(TAG, "onClick: Wrote the file.");

                Intent intent = new Intent(MainActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void saveToAPI(boolean post) {

        try {
            if(post)
            {
                RestClient.executePostRequest(team,
                        MainActivity.VEHICLETRACKERAPI,
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(ArrayList<Team> result) {
                                Log.d(TAG, "onSuccess: Post" + result);
                            }
                        });
            }
            else
            {
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
        }
        catch (Exception e)
        {
            Log.d(TAG, "saveToAPI: " + e.getMessage());
        }


    }

    private void initTextChangedEvents() {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellNumber = findViewById(R.id.editCell);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG, "onTextChanged: Name");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.d(TAG, "afterTextChanged: Name");
                team.setName(editName.getText().toString());
            }
        });

        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                team.setCity(editCity.getText().toString());
            }
        });
        editCellNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                team.setCellNumber(editCellNumber.getText().toString());
            }
        });
    }

    private void initTeam(int teamId) {



        try {
            Log.d(TAG, "initTeam: "+ VEHICLETRACKERAPI + teamId);
            RestClient.executeGetOneRequest(VEHICLETRACKERAPI + teamId, this,
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

        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellNumber = findViewById(R.id.editCell);
        TextView rating = findViewById(R.id.txtRating);
        ImageButton imageButtonPhoto = findViewById(R.id.imgPhoto);

        editName.setText(team.getName());
        editCity.setText(team.getCity());
        editCellNumber.setText(team.getCellNumber());
        rating.setText(String.valueOf(team.getRating()));
        imageButtonPhoto.setImageBitmap(team.getPhoto());

        mapFragment.getMapAsync(this);

    }


    private void ReadFromTextFile() {
        //  Write out the data to a flat file.
        FileIO fileIO = new FileIO();

        // Read the data out of the flat file.
        ArrayList<String> strData = fileIO.readFile(this);
        teams = new ArrayList<Team>();

        for(String s : strData)
        {
            // Remember to include \\
            String[] data = s.split("\\|");
            teams.add(new Team(Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    Integer.parseInt(data[3]),
                    Float.parseFloat(data[4]),
                    data[5],
                    Boolean.parseBoolean(data[6])));
        }
    }


    private void initToggleButton() {
        ToggleButton toggleButtonEdit = findViewById(R.id.toggleButtonEdit);

        toggleButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(toggleButtonEdit.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText etName = findViewById(R.id.etName);
        EditText etCity = findViewById(R.id.etCity);
        Button btnRating = findViewById(R.id.btnRating);
        EditText editCell = findViewById(R.id.editCell);

        etName.setEnabled(enabled);
        etCity.setEnabled(enabled);
        btnRating.setEnabled(enabled);
        editCell.setEnabled(enabled);

        if(enabled) {
            etName.requestFocus();
            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        else
        {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            editCell.setInputType(InputType.TYPE_NULL);
        }

    }

    private void initRatingButton() {
        Button btnRating = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RaterDialog raterDialog = new RaterDialog();
                Log.d(TAG, "onClick: Before Show");
                raterDialog.show(fragmentManager, "Rate Team");
                Log.d(TAG, "onClick: After Show");
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the List Activity
                Intent intent = new Intent(MainActivity.this, TeamSettingsActivity.class);
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
                // Show the List Activity



                Intent intent = new Intent(MainActivity.this, TeamMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("teamId",team.getId());
                startActivity(intent);
            }
        });
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the List Activity
                Intent intent = new Intent(MainActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void didFinishRaterDialog(float rating) {
        TextView txtRating = findViewById(R.id.txtRating);
        txtRating.setText(String.valueOf(rating));
        team.setRating(rating);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            Log.d(TAG, "onMapReady: Start of Map");

            gMap = googleMap;
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            Point size = new Point();
            WindowManager windowManager = getWindowManager();
            windowManager.getDefaultDisplay().getSize(size);
            int measuredWidth = size.x;
            int measuredHeight = size.y;


            if(team != null)
            {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                String info = team.getName() + ", " +
                        team.getCity() + ": " +
                        team.getRating();
                Log.d(TAG, "onMapReady: " + info);

                LatLng point = new LatLng(team.getLatitude(), team.getLongitude());
                builder.include(point);

                gMap.addMarker(new MarkerOptions().position(point)
                        .title(team.getName())
                        .snippet(team.getCity() + ": " + team.getRating()));

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,15f));
            }
            else
            {
                Log.d(TAG, "onMapReady: Team is null");
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, "onMapReady: " + e.getMessage());
        }
    }
}