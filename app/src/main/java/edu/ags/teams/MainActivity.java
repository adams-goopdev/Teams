package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "myDebug";

    RecyclerView teamList;
    ArrayList<Team> teams;
    TeamAdapter teamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teams = new ArrayList<Team>();
        teams.add(new Team(1,"Packers","Green Bay", R.drawable.packers));
        teams.add(new Team(2,"Vikings","Minnesota", R.drawable.vikings));
        teams.add(new Team(3,"Lions","Detroit", R.drawable.lions));
        teams.add(new Team(4,"Bears","Chicago", R.drawable.bears));

        Log.d(TAG, "onCreate: ");
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Log.d(TAG, "onClick: " + teams.get(position).Description);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();

        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);

        teamAdapter = new TeamAdapter(teams, this);
        teamAdapter.setOnClickListener(onItemClickListener);
        teamList.setAdapter(teamAdapter);

        for (Team t: teams)
        {
            Log.d(TAG, "onResume: " + t.Description);
        }

    }
}