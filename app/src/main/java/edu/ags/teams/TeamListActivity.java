package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TeamListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        this.setTitle("Team List");
    }
}