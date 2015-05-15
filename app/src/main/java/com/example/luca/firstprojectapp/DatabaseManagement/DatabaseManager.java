package com.example.luca.firstprojectapp.DatabaseManagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.sql.SQLException;


/**
 * Created by luca on 05/05/15.
 * This class will be highly dependant on data structure!
 */
public class DatabaseManager {

    private SQLiteDatabase database;
    private SqlLiteHelper dbHelper;

    public DatabaseManager(Context context){
        dbHelper = new SqlLiteHelper(context);

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
    //example method, further methods must be created following this pattern!
    //see execSQL for documentation
    public void createMessage(String message){
        /*
        database.execSQL("insert into " + SqlLiteHelper.TABLE_PLANNING + " (" + SqlLiteHelper.COLUMN_STRING
                +") values ("
                +" ' "+ message +" ' );");
                */
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
