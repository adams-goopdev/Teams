package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
    TeamAdapter teamAdapter;
    ArrayList<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        teams = new ArrayList<Team>();
        /*teams.add(new Team(1, "Packers", "Green Bay", R.drawable.packers, 4.0f, "9205697501", true));
        teams.add(new Team(2, "Vikings", "Minnesota", R.drawable.vikings, 3.0f, "8007453000", false));
        teams.add(new Team(3, "Lions", "Detroit", R.drawable.lions, 2.0f, "3131234567", false));
        teams.add(new Team(4, "Bears", "Chicago", R.drawable.bears, 2.5f, "7739874356", false));*/

        Log.d(TAG, "onCreate: ");

        initListButton();
        initMapButton();
        initSettingsButton();
        initAddTeamButton();
        initDeleteSwitch();


        // ReadFromTextFile();
        // ReadFromXMLFile();
        // WriteToTextFile();

        //for(Team team : teams)
        //{
        //    SaveToDatabase(team);
        //}

        this.setTitle("Team List");

    }

    private void SaveToDatabase(Team team) {
        TeamDataSource ds = new TeamDataSource(TeamListActivity.this);

        try{
            ds.open();
            boolean result = ds.insert(team);
            Log.d(TAG, "SaveToDatabase: Saved: " + team.toString());
        }
        catch(Exception ex)
        {
            Log.d(TAG, "SaveToDatabase: " + ex.getMessage());
        }
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                // Communicate the status to the adapter
                Log.d(TAG, "onCheckedChanged: Checked change event Switch: " + b);
                teamAdapter.setDelete(status);

                // rebind the recyclerview in the adapter
                teamAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initAddTeamButton() {
        Button btnAddTeam = findViewById(R.id.buttonAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    };

    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the List Activity
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
                // Show the List Activity
                Intent intent = new Intent(TeamListActivity.this, TeamMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void WriteToTextFile()
    {
        FileIO fileIO = new FileIO();
        Integer counter = 0;
        String[] data = new String[teams.size()];
        for (Team t : teams) data[counter++] = t.toString();
        fileIO.writeFile(this, data);
    }



    private void ReadFromTextFile()
    {
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

    private void ReadFromXMLFile()
    {
        teams = new ArrayList<Team>();

        try {
            XmlPullParser xmlPullParser = getResources().getXml(R.xml.teams);
            while(xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                if(xmlPullParser.getEventType() == XmlPullParser.START_TAG)
                {
                    if(xmlPullParser.getName().equals("team"))
                    {
                        int id = Integer.parseInt(xmlPullParser.getAttributeValue(null, "id"));
                        String description = xmlPullParser.getAttributeValue(null, "description");
                        String city = xmlPullParser.getAttributeValue(null, "city");
                        int imgid = Integer.parseInt(xmlPullParser.getAttributeValue(null, "imgid"));
                        Log.d(TAG, "ReadFromXMLFile: " + id + ":" +
                                description + ":" +
                                city +":" +
                                imgid);
                        teams.add(new Team(id, description, city, imgid, 0, "", false));
                    }
                }
                xmlPullParser.next();
            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "ReadFromXMLFile: " + e.getMessage());
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the List Activity
                Intent intent = new Intent(TeamListActivity.this, TeamListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
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
    public void onResume()
    {
        super.onResume();

        String sortField = getSharedPreferences("TeamsPreferences",
                Context.MODE_PRIVATE).getString("sortField","name");
        String sortOrder = getSharedPreferences("TeamsPreferences",
                Context.MODE_PRIVATE).getString("sortOrder","ASC");

        teams = new ArrayList<>();
/*        TeamDataSource ds = new TeamDataSource(this);
        try {
            ds.open();
            teams = ds.getTeams(sortField, sortOrder);
            Log.d(TAG, "onResume: Database is open.");

            if(teams.size() == 0)
            {
                Log.d(TAG, "onResume: Refresh");
                ds.RefreshData();
            }
            ds.close();
        }
        catch (Exception ex)
        {
            Log.d(TAG, "onResume: Open Error :" + ex.getMessage());
        }*/

        try {
            RestClient.executeGetRequest(MainActivity.VEHICLETRACKERAPI, this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {
                            for(Team t : result)
                            {
                                Log.d(TAG, "onSuccess: " + t.getName());
                            }
                            teams = result;
                            RebindTeams();
                        }
                    });
        }
        catch (Exception e)
        {
            Log.d(TAG, "onResume: "+ e.getMessage());
        }

    }

    private void RebindTeams(){
        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);

        teamAdapter = new TeamAdapter(teams, this);
        teamAdapter.setOnClickListener(onItemClickListener);
        teamList.setAdapter(teamAdapter);
    }


}