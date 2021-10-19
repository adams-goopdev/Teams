package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity {

    public static final String TAG = "myDebug";

    RecyclerView teamList;
    ArrayList<Team> teams;
    TeamAdapter teamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        teams = new ArrayList<Team>();
/*
       teams.add(new Team(1,"Packers","Green Bay", R.drawable.packers, 4.0f, "9202796888",true));
        teams.add(new Team(2,"Vikings","Minnesota", R.drawable.vikings, 3.0f, "9202796889",false));
        teams.add(new Team(3,"Lions","Detroit", R.drawable.lions, 2.0f, "9202796890",false));
        teams.add(new Team(4,"Bears","Chicago", R.drawable.bears, 2.5f, "9202796891",false));
*/

        Log.d(TAG, "onCreate: ");

        initListButton();
        initMapButton();
        initSettingsButton();
        initAddTeam();
        initDeleteSwitch();


       // ReadFromTextFile();
       // ReadFromXMLFile();
       // WriteToTextFile();

       // for(Team team: teams)
      //  {
      //      SaveToDatabase(team);
       // }

        this.setTitle("TeamList");
    }

    private void SaveToDatabase(Team team) {
    TeamDataSource ds = new TeamDataSource(TeamListActivity.this);

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

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                //Communicate the status to the adapter
                Log.d(TAG, "onCheckedChanged: Did we make it here for Delete");
                teamAdapter.setDelete(status);
                // rebind the recyclerview in the adapter
                teamAdapter.notifyDataSetChanged();

            }
        });

    }

    private void initAddTeam() {
        Button btnAddTeam = findViewById(R.id.buttonAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void WriteToTextFile() {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String [teams.size()];
        for (Team t : teams) data[counter++] = t.toString();
        fileIO.writeFile(this,data);
    }

    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamListActivity.this, TeamSettingsActivity.class);
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
                Intent intent = new Intent(TeamListActivity.this, TeamMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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

/*    private void ReadFromXMLFile()
    {
        teams = new ArrayList<Team>();

        try {
            XmlPullParser xmlPullParser = getResources().getXml(R.xml.teams);
            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                if(xmlPullParser.getEventType() == XmlPullParser.START_TAG)
                {
                    if(xmlPullParser.getName().equals("team"))
                    {
                        int id = Integer.parseInt( xmlPullParser.getAttributeValue(null, "id"));
                        String description = xmlPullParser.getAttributeValue(null, "description");
                        String city = xmlPullParser.getAttributeValue(null, "city");
                        int imgid = Integer.parseInt( xmlPullParser.getAttributeValue(null, "imgid"));

                        Log.d(TAG, "ReadFromXMLFile: " + id + ":" + description + ":" + city + ":" + imgid);

                        teams.add(new Team(id, description, city, imgid, 0, "", false));
                    }
                }
                xmlPullParser.next();
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, "ReadFromXMLFile: " + e.getMessage());

        }
    }*/

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the list activity
                Intent intent = new Intent(TeamListActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int teamId = teams.get(position).getId();
            Log.d(TAG, "onClick: " + teams.get(position).getName());

            Intent intent = new Intent(TeamListActivity.this, MainActivity.class);
            intent.putExtra("teamId", teamId);
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        TeamDataSource ds = new TeamDataSource(this);
        try {
            ds.open();
            teams = ds.getTeams();
            Log.d(TAG, "onResume: Database is open");
        }
        catch (Exception e)
        {
            Log.d(TAG, "onResume: Open DB error " + e.getMessage());
        }

        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);

        teamAdapter = new TeamAdapter(teams, this);
        teamAdapter.setOnClickListener(onItemClickListener);
        teamList.setAdapter(teamAdapter);

        for (Team t: teams)
        {
            Log.d(TAG, "onResume: " + t.getName() + t.toString());
        }

    }
}