package com.example.luca.firstprojectapp.DatabaseManagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.sql.SQLException;
import java.util.Calendar;


/**
 * Created by luca on 05/05/15.
 * This class will be highly dependant on data structure!
 */
public class DatabaseManager {

    private SQLiteDatabase database;
    private SqlLiteHelper dbHelper;

    public DatabaseManager(Context context){
        dbHelper = SqlLiteHelper.getsInstance(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    /*
    here we have methods for db management.
    NOTE: for string insertion you must put the ' String ' or won't work
     */

    public void insertWeightChange(Calendar cal, double weight){

        database.execSQL("insert into " + SqlLiteHelper.TABLE_WEIGHT + "(" + SqlLiteHelper.COLUMN_ID
                + ", " + SqlLiteHelper.COLUMN_WEIGHT + ") values ("
                + cal.getTimeInMillis()
                + ", " + weight + ");");
    }

    public void insertStats(Calendar cal, double calories, long meters, long duration){
        database.execSQL("insert into " + SqlLiteHelper.TABLE_STATS
                + "(" + SqlLiteHelper.COLUMN_ID + ", " + SqlLiteHelper.COLUMN_CALORIES + ", "
                + SqlLiteHelper.COLUMN_DISTANCE + ", " + SqlLiteHelper.COLUMN_TIME + ") values ("
                + cal.getTimeInMillis() + ", " + calories + ", " + meters + ", " + duration + ");"
        );

    }

    public void querySelect(String query, IOnCursorCallback caller, int position){
        new GetAsyncSelectStatement(query, position).execute(caller);

    }

    protected class GetAsyncSelectStatement extends AsyncTask<IOnCursorCallback,Void,Cursor>{

        private IOnCursorCallback call;
        private String execQuery;
        private int position;

        public GetAsyncSelectStatement(String query,int newPosition){
            execQuery = query;
            position = newPosition;

        }

        @Override
        protected Cursor doInBackground(IOnCursorCallback... params) {
            call = params[0];
            if(params[0] == null)
                throw new UnsupportedOperationException("Null params passed when attempting to access" +
                        "database");
            Cursor newCur = database.rawQuery(execQuery, new String[]{});
            return newCur;
        }

        @Override
        protected void onPostExecute(Cursor cur){
            call.fillView(cur,position);

        }
    }

    /**
     * Interface must be implemented to acces database.
     */
    public interface IOnCursorCallback {

        public void fillView(Cursor cur,int position);

    }
}
