package edu.ags.teams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teams.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_TEAM= "CREATE table team "
            + " (id integer primary key autoincrement, "
            + "name text not null, "
            + "city text not null, "
            + "rating float not null, "
            + "imgid int, "
            + "cellnumber text, "
            + "isfavorite int); ";

    public TeamDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
