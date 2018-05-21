package com.example.liamkenny.unionapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {


    //TODO: fill in method sigs

    /**
     * Helper class holding all of the methods related to the device local SQL database:
     * The function of this database is to allow the current contents of the user's basket
     * to be stored program wide, without pushing all the data through the app in the intents.
     */


    //Init vars: make more resilient by not hard coding tables names
    private static final int DB_VERSION = 1;
    private static final String database_name = "basket_database";
    private static final String Product_Table_Name = "product";
    private static dbHelper sInstance;   //instant of this class
    private static SQLiteDatabase.CursorFactory factory = null;


    /**
     * Use of Singleton design pattern to ensure only one read/write request
     * is made at once
     *
     * @param context
     * @return
     */

    public static synchronized dbHelper getInstance(Context context) {


        if (sInstance == null) {    //create an instance if it doesn't exist
            sInstance = new dbHelper(context.getApplicationContext());    //create the instance
        }
        return sInstance;
    }


    private dbHelper(Context context) {
        super(context, database_name, factory, DB_VERSION);
    }   //private constructor for this class.


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createProductTable(SQLiteDatabase s) {


        String createSQL = "CREATE TABLE " + Product_Table_Name + "(" +
                "productID TEXT, " +
                "productName TEXT, " +
                "productType TEXT, " +
                "productPrice REAL," +
                "productQuantity INTEGER," +
                "PRIMARY KEY(productID));";
        s.execSQL(createSQL);



    }

    private void addProductToBasket() {
    }

    ;

    private void removeProductFromBasket() {
    }

    ;

    private void updateProductQuantity() {
    }

    ;

    private void clearBasket() {
    }

    ;

}



