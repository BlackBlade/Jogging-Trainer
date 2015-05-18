package com.example.luca.firstprojectapp.DatabaseManagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luca on 05/05/15.
 */
public class SqlLiteHelper extends SQLiteOpenHelper {

    /*
    here we put table names, followed by columns name for each table
    this is only a simple example!
     */

    //general name of a table index column
    public static final String COLUMN_ID = "_id";

    //strings relarive to table route
    public static final String TABLE_ROUTE = "route";

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LAT  = "latitude";
    public static final String COLUMN_LNG = "longitude";

    //strings relatibe to table planning
    public static final String TABLE_PLANNING = "planning";

    public static final String COLUMN_DATE_PLANNING = "dateplanning";

    //strings relative to table weight
    public static final String TABLE_WEIGHT = "weight";

    public static final String COLUMN_WEIGHT = "actualWeight";

    //strings relative to table stats
    public static final String TABLE_STATS = "stats";

    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_TIME = "time";

    private static final String DATABASE_NAME = "JoggingTrainerDatabase.db";
    private static final int DATABASE_VERSION = 2;

    /*
    here we put database creation query string
     */
    private static final String DATABASE_CREATE_STATS = "create table " + TABLE_STATS
            + "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_CALORIES + " integer not null," + COLUMN_DISTANCE +" integer not null,"
            + COLUMN_TIME +" integer not null);";

    private static final String DATABASE_CREATE_ROUTE = "create table " + TABLE_ROUTE
            + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DATE + " integer not null,"
            + COLUMN_LAT + " integer not null,"
            + COLUMN_LNG + " integer not null);";

    private static final String DATABASE_CREATE_PLANNING = "create table " + TABLE_PLANNING
            + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DATE_PLANNING +" integer not null);";

    private static final String DATABASE_CREATE_WEIGHT = "create table " + TABLE_WEIGHT
            + "(" + COLUMN_ID + " integer primary key, "
            + COLUMN_WEIGHT + " integer not null);";


    public SqlLiteHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_STATS);
        db.execSQL(DATABASE_CREATE_PLANNING);
        db.execSQL(DATABASE_CREATE_ROUTE);
        db.execSQL(DATABASE_CREATE_WEIGHT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANNING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        onCreate(db);

    }
}
