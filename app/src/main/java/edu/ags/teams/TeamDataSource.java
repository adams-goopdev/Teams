package edu.ags.teams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TeamDataSource {
    public static final String TAG = "myDebug";
    private static final String TEAM = "team";
    private SQLiteDatabase database;
    private TeamDBHelper dbHelper;

    public TeamDataSource(Context context){
        dbHelper = new TeamDBHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public boolean delete(int id){

        boolean didSucceed = false;
        try {
            didSucceed = database.delete(TEAM,"id=" + id, null) >0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "delete: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public boolean update(Team team) {
        boolean didSucceed = false;

        try {
            ContentValues updateValues = new ContentValues();

            //Set the values
            Long id = (long)team.getId();

            updateValues.put("name",team.getName());
            updateValues.put("city",team.getCity());
            updateValues.put("rating", team.getRating());
            updateValues.put("cellnumber",team.getCellNumber());
            updateValues.put("isfavorite", team.getFavorite());
            updateValues.put("imgid", team.getImgId());

            Log.d(TAG, "Update line: " + updateValues);
            didSucceed = database.update(TEAM, updateValues,"id=" + id, null) > 0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "Update line: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public boolean insert(Team team) {

        boolean didSucceed = false;

        try {
            ContentValues initialValues = new ContentValues();
            //Set the values
            initialValues.put("name",team.getName());
            initialValues.put("city",team.getCity());
            initialValues.put("rating", team.getRating());
            initialValues.put("cellnumber",team.getCellNumber());
            initialValues.put("isfavorite", team.getFavorite());
            initialValues.put("imgid", team.getImgId());

            Log.d(TAG, "insert: " + initialValues);
            didSucceed = database.insert(TEAM,null, initialValues) > 0;
        }
        catch (Exception e)
        {
            Log.d(TAG, "insert: Error " + e.getMessage());
        }
        return didSucceed;
    }

    public ArrayList<Team> getTeams(){
        ArrayList<Team> teams = new ArrayList<Team>();
        try {
            String query = "Select * from Team";
            Cursor cursor = database.rawQuery(query, null);

            Team team;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                team = new Team();
                team.setId(cursor.getInt(0));
                team.setName(cursor.getString(1));
                team.setCity(cursor.getString(2));
                team.setRating(cursor.getFloat(3));
                team.setImgId(cursor.getInt(4));
                team.setCellNumber(cursor.getString(5));
                team.setFavorite(Boolean.parseBoolean(cursor.getString(6)));
                teams.add(team);
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e)
        {
            Log.d(TAG, "getTeams: " + e.getMessage());
        }
        return teams;
    }

    public Team getTeam(int id){

        Team team = team = new Team();
        try {
            String query = "Select * from Team WHERE Id = " + id;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            if(cursor.moveToFirst()){
                team = new Team();
                team.setId(cursor.getInt(0));
                team.setName(cursor.getString(1));
                team.setCity(cursor.getString(2));
                team.setRating(cursor.getFloat(3));
                team.setImgId(cursor.getInt(4));
                team.setCellNumber(cursor.getString(5));
                team.setFavorite(Boolean.parseBoolean(cursor.getString(6)));
                cursor.close();
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "getTeams: " + e.getMessage());
        }
        return team;
    }


}
