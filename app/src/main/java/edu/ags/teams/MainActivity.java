package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener{

    public static final String TAG = "myDebug";
    Team team;
    ArrayList<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");
       initListButton();
       initMapButton();
       initSettingsButton();
       initRatingButton();
       setForEditing(false);
       initToggleButton();
       initTextChangedEvents();
       initSaveButton();
       
       Bundle extras = getIntent().getExtras();
       
      // ReadFromTextFile();
       

        try {
            if(extras != null) {
                //Edit existing team
                Log.d(TAG, "onCreate: " + extras.getInt("teamId"));
                initTeam(extras.getInt("teamId"));
            }
            else {
                //Make a new one
                team = new Team();
                Log.d(TAG, "onCreate: new team");
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }

        this.setTitle("Mainactivity");
    }
    
    private void WriteToTextFile() {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String [teams.size()];
        for (Team t : teams) data[counter++] = t.toString();
        fileIO.writeFile(this,data);
    }
    
    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamDataSource ds = new TeamDataSource(MainActivity.this);
                try {
                    if(team.getId() == -1)
                    {
                        //new team
                        try {
                            ds.open();
                            boolean result = ds.insert(team);
                            Log.d(TAG, "SaveToDatabase: Saved " + team);
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, "SaveToDatabase: " + e.getMessage());
                        }
                    }
                    else
                    {
                        try {
                            ds.open();
                            boolean result = ds.update(team);
                            Log.d(TAG, "SaveToDatabase: Saved " + team);
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, "SaveToDatabase: " + e.getMessage());
                        }

                        //Update the team in the array
                        //Log.d(TAG, "onClick: "+ team.toString());
                        teams.set(team.getId() -1, team);
                    }

                    //WriteToTextFile();



                }
                catch (Exception e)
                {
                    Log.d(TAG, "Adding new team: Exception " + e.getMessage());
                }
            }
        });
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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                team.setName(editName.getText().toString());
                Log.d(TAG, "afterTextChanged: Name");
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

        TeamDataSource ds = new TeamDataSource(this);
        ds.open();

        //team = teams.get(teamId);
        team = ds.getTeam(teamId);
        ds.close();

        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellNumber = findViewById(R.id.editCell);
        TextView rating = findViewById(R.id.txtRating);
        ImageButton imageButtonPhoto = findViewById(R.id.imgPhoto);

        editName.setText(team.getName());
        editCity.setText(team.getCity());
        editCellNumber.setText(team.getCellNumber());
        rating.setText(String.valueOf(team.getRating()));
        imageButtonPhoto.setImageResource(team.getImgId());

    }

    private void ReadFromTextFile() {
        //Write out the teams to a file
        FileIO fileIO = new FileIO();

        Integer counter = 0;
        String[] data;// = new String [teams.size()];
        //for(Team t : teams) data[counter++] = t.toString();

        // fileIO.writeFile(this, data);


        //Read the data out of the file
        ArrayList<String> strData = fileIO.readFile(this);
        teams = new ArrayList<Team>();

        for(String s : strData)
        {
            data = s.split("\\|");
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
                Log.d(TAG, "onClick: AfterShow");
            }
        });
    }

    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
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
                //Show the list activity
                Intent intent = new Intent(MainActivity.this, TeamMapActivity.class);
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
        TextView textView = findViewById(R.id.txtRating);
        textView.setText(String.valueOf(rating));
        team.setRating(rating);
    }
}