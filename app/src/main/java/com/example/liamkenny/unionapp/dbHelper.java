package com.example.liamkenny.unionapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {



    private static final int DB_VERSION = 1;
    private static final String database_name = "basket_database";
    private static final String Product_Table_Name = "product";
    private static  dbHelper sInstance;   //instant of this class
    private static SQLiteDatabase.CursorFactory factory = null;

    public static synchronized dbHelper getInstance(Context context) {


        if (sInstance == null) {    //create an instance if it doesn't exist
            sInstance = new dbHelper(context.getApplicationContext());    //create the instance
        }
        return sInstance;
    }


    private dbHelper(Context context)
    {
        super(context, database_name, factory, DB_VERSION);
    }   //private constructor for this class.




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createProductTable(){}



}



