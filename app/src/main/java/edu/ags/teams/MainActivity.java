package edu.ags.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "myDebug";
    ArrayList<Team> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teams = new ArrayList<Team>();
        teams.add(new Team(1,"Packers","Green Bay"));
        teams.add(new Team(2,"Vikings","Minnesota"));
        teams.add(new Team(3,"Lions","Detroit"));
        teams.add(new Team(4,"Bears","Chicago"));

        Log.d(TAG, "onCreate: ");
    }
}