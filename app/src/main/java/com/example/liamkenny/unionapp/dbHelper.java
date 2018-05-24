package com.example.liamkenny.unionapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
        createProductTable(sqLiteDatabase);
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

    public void addProductToBasket(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String id = p.getProductID();
        String name = p.getProductName();
        String type = p.getProductType();
        double price = p.getProductPrice();
        int quantity = 1;
        String sql = "INSERT INTO " + Product_Table_Name + "(productID, productName, productType, productPrice, productQuantity) VALUES ('" +
                id + "','" + name + "','" + type + "','" + price + "','" + quantity + "');";
        db.execSQL(sql);
        db.close();




    }


    public Basket extractBasketFromDB(){

        Basket basket;
        ArrayList<Basket_Product> products = new ArrayList<Basket_Product>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + Product_Table_Name;
        Cursor cr = db.rawQuery(sql, null);

        if(!cr.moveToFirst() || cr.getCount() == 0){    //return null if results are empty
            basket = new Basket(new ArrayList<Basket_Product>(null));
            return basket;
        }
        do{

            int quantity = cr.getInt(cr.getColumnIndex("productQuantity"));
            for(int i = 0; i < quantity; i++){

                String id = cr.getString(cr.getColumnIndex("productID"));
                String name = cr.getString(cr.getColumnIndex("productName"));
                String type = cr.getString(cr.getColumnIndex("productType"));
                double price = cr.getDouble(cr.getColumnIndex("productPrice"));

                Product p = new Product(id, name, type, price);
                products.add(new Basket_Product(p, 1));

            }


        }while(cr.moveToNext());

        basket = new Basket(products);

        return basket;
    }


    public void removeProductFromBasket(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String id = p.getProductID();
        String sql = "DELETE FROM " + Product_Table_Name + " WHERE productID = " + id + ";";
        db.execSQL(sql);
        db.close();

    }



    public void updateProductQuantity(Product p, boolean increment) {

        SQLiteDatabase db;
        String id = p.getProductID();

        if(increment){
            int quantity = 0;

            quantity = this.getProductQuantity(p);
            db = this.getWritableDatabase();
            quantity ++;
            String sql = "UPDATE " + Product_Table_Name + " SET productQuantity = " +quantity + " WHERE productID = '" + id + "'" ;

            db.execSQL(sql);
            db.close();

        }else{
            if(this.getProductQuantity(p) != 0){
                int quantity = 0;
                quantity = this.getProductQuantity(p);
                 db = this.getWritableDatabase();
                quantity --;
                String sql = "UPDATE " + Product_Table_Name + " SET productQuantity = " +quantity + " WHERE productID = '" + id + "'";
                db.execSQL(sql);
                db.close();

            }else {
                removeProductFromBasket(p);
            }
        }


    }


    public int getProductQuantity(Product p){

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + Product_Table_Name + " WHERE productID = '" + p.getProductID() + "'";

        Cursor cr = db.rawQuery(sql, null);
        int quant = 0;

        if(!cr.moveToFirst() || cr.getCount() == 0){    //return null if cursor is empty

        }

        do{
            quant = cr.getInt(cr.getColumnIndex("productQuantity"));

        }while(cr.moveToNext());

        db.close();



        return quant;


    }



    public boolean isProductInDB(Product p){
        boolean ret = true;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + Product_Table_Name + " WHERE productID = '" + p.getProductID() + "'";
        Cursor cr = db.rawQuery(sql, null);
        if(!cr.moveToFirst() || cr.getCount() == 0){
            ret = false;

        }
        db.close();
        return ret;

    }

    public void clearAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS " + Product_Table_Name + " ;";
        db.execSQL(sql);
        createProductTable(db);
        db.close();
    }



}



