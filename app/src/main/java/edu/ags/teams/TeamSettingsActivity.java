package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TeamSettingsActivity extends AppCompatActivity {
    public static final String TAG = "myDebug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_settings);

        initListButton();
        initMapButton();
        initSettingsButton();
        initSortByClick();
        initSortOrderClick();
        initSettings();
        this.setTitle("Settings");
    }

    private void initSettings() {
        String sortField = getSharedPreferences("TeamsPreferences",
                Context.MODE_PRIVATE).getString("sortField","name");
        String sortOrder = getSharedPreferences("TeamsPreferences",
                Context.MODE_PRIVATE).getString("sortOrder","ASC");

        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbCity = findViewById(R.id.radioCity);
        RadioButton rbRating = findViewById(R.id.radioRating);

        if(sortField.equalsIgnoreCase("name"))
            rbName.setChecked(true);
        else if (sortField.equalsIgnoreCase("city"))
            rbCity.setChecked(true);
        else
            rbRating.setChecked(true);

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDecending);

        if(sortOrder.equalsIgnoreCase("ASC"))
            rbAscending.setChecked(true);
        else
            rbDescending.setChecked(true);




    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);


                String OrderByField="";

                if(rbAscending.isChecked()) {
                    OrderByField = "ASC";

                }
                else{
                    OrderByField = "DESC";
                }

                getSharedPreferences("TeamsPreferences",
                        Context.MODE_PRIVATE)
                        .edit()
                        .putString("sortOrder", OrderByField)
                        .apply();

                Log.d(TAG, "onCheckedChanged: Order" + OrderByField);

            }
        });




    }

    private void initSortByClick() {

        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbName = findViewById(R.id.radioName);
                RadioButton rbCity = findViewById(R.id.radioCity);


                String sortField="";

                if(rbName.isChecked()) {
                   sortField = "name";

                }
                else if(rbCity.isChecked()){
                    sortField = "city";
                }
                else{
                    sortField = "rating";
                }

                getSharedPreferences("TeamsPreferences",
                        Context.MODE_PRIVATE)
                        .edit()
                        .putString("sortField", sortField)
                        .apply();

                Log.d(TAG, "onCheckedChanged: SortField" + sortField);

            }
        });
    }

    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamSettingsActivity.this, TeamSettingsActivity.class);
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
                Intent intent = new Intent(TeamSettingsActivity.this, TeamMapActivity.class);
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
                Intent intent = new Intent(TeamSettingsActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}