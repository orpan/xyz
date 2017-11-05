package com.example.sacchianand.testingnavigation.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.text.NumberFormat.Field.INTEGER;

/**
 * Created by sacchianand on 10/28/17.
 */

public class DbHelperActivity extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="sqlitedb";
    public static final String TABLE_NAME="stdtable";
    public static final Integer DATABASE_VERSION=7;
    public static final String COLOUMN_ID="id";
    public static final String COLOUMN_NAME="name";
    public static final String NEW_TABLE_NAME="newstdtable";
    public static final String NEW_COLOUMNID="new_id";
    public static final String NEW_COLOUMN_NAME="new_name";
    public static final String COLOUMN_ADDRESS="address";

Context context;

    public DbHelperActivity(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

        Toast.makeText(context,"constructor callled",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createquery = " CREATE TABLE " + TABLE_NAME
                + "(" + COLOUMN_ID + " INTEGER PRIMARY KEY," + COLOUMN_NAME + " TEXT" + ")";
        String createnewquery =" CREATE TABLE " + NEW_TABLE_NAME + " ( " + NEW_COLOUMNID + " INTEGER , " + NEW_COLOUMN_NAME + " TEXT " + " ) ";
        try {
            db.execSQL(createquery);
            db.execSQL(createnewquery);
            Toast.makeText(context,"oncreate callled",Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String updated = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String newupdated ="DROP TABLE IF EXISTS " + NEW_TABLE_NAME ;
        String newcoloumnadd="ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + COLOUMN_ADDRESS + "TEXT ; "  ;

        try {
            db.execSQL(updated);
            db.execSQL(newupdated);
            db.execSQL(newcoloumnadd);
            Toast.makeText(context,"onupgrade callled",Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);


    }



    public List<String> getAllContacts() {
        List<String> contactList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


               String id= cursor.getString(0);
               String name= cursor.getString(1);
                // Adding contact to list

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}
