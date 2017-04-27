package com.example.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.webservice.Model;
import com.example.webservice.ServiceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adi on 4/6/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
        //getWritableDatabase();

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + DBConstants.TABLE_PRODUCTS + " ("
                + DBConstants.KEY_ID + " TEXT , " + DBConstants.KEY_NAME + " TEXT , " + DBConstants.KEY_UPC + " TEXT , "
                + DBConstants.KEY_PRICE + " TEXT , " + DBConstants.KEY_IMAGE_URL + " TEXT , " + DBConstants.KEY_PURCHASE_URL + " TEXT , " + DBConstants.KEY_TYPE + " TEXT , PRIMARY KEY ( " + DBConstants.KEY_UPC + "," + DBConstants.KEY_TYPE + " ) )";

        try {
            db.execSQL(CREATE_PRODUCTS_TABLE);
            System.out.println("table created");
        } catch (Exception e) {
            Log.d("DatabaseHandler", e.getMessage());
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_PRODUCTS);
// Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addItem(Model product) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.KEY_ID, product.getItemId());
        values.put(DBConstants.KEY_NAME, product.getName());
        values.put(DBConstants.KEY_UPC, product.getItemUPC());
        values.put(DBConstants.KEY_PRICE, product.getSalePrice());
        values.put(DBConstants.KEY_IMAGE_URL, product.getThumbnailImage());
        values.put(DBConstants.KEY_PURCHASE_URL, product.getItemPurchaseURL());
        values.put(DBConstants.KEY_TYPE, String.valueOf((product.getType())));

// Inserting Row
        long result;
        try {
            result = db.insertOrThrow(DBConstants.TABLE_PRODUCTS, null, values);
            if (result == -1)
                throw new Exception(new String("Data not saved"));
        } catch (SQLiteConstraintException e) {
            throw new Exception(new String("Item already added"));
        } finally {
            db.close();
        }
    }

    public Model getitem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DBConstants.TABLE_PRODUCTS, new String[]{DBConstants.KEY_ID,
                        DBConstants.KEY_NAME, DBConstants.KEY_UPC, DBConstants.KEY_PRICE, DBConstants.KEY_IMAGE_URL, DBConstants.KEY_PURCHASE_URL, DBConstants.KEY_TYPE}, DBConstants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Model product = new Model((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), (cursor.getString(6).equals("AMAZON") ? ServiceType.AMAZON : ServiceType.WALMART));

        return product;

    }

    // Getting All
    public List<Model> getAllProducts(ServiceType type) {
        List<Model> ProductList = new ArrayList<Model>();
// Select All Query
        String selectQuery = "SELECT * FROM " + DBConstants.TABLE_PRODUCTS + " WHERE " + DBConstants.KEY_TYPE + " = '" + (type == ServiceType.WALMART ? DBConstants.TYPE_WALMART : DBConstants.TYPE_AMAZON) + "' ORDER BY " + DBConstants.KEY_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model product = new Model();
                product.setItemId(cursor.getString(0));
                product.setName(cursor.getString(1));
                product.setItemUPC(cursor.getString(2));
                product.setSalePrice(cursor.getString(3));
                product.setThumbnailImage(cursor.getString(4));
                product.setItemPurchaseURL(cursor.getString(5));
                product.setType(cursor.getString(6).equals("AMAZON") ? ServiceType.AMAZON : ServiceType.WALMART);

// Adding to list
                ProductList.add(product);
            } while (cursor.moveToNext());
        }
// close inserting data from database
        db.close();

        return ProductList;

    }

    // Updating single contact
    public int updateProduct(Model product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConstants.KEY_NAME, product.getName());
        values.put(DBConstants.KEY_UPC, product.getItemUPC());
        values.put(DBConstants.KEY_PRICE, product.getSalePrice());
        values.put(DBConstants.KEY_IMAGE_URL, product.getThumbnailImage());
        values.put(DBConstants.KEY_PURCHASE_URL, product.getItemPurchaseURL());
        values.put(DBConstants.KEY_TYPE, String.valueOf(product.getType()));
// updating row
        return db.update(DBConstants.TABLE_PRODUCTS, values, DBConstants.KEY_ID + " = ?",
                new String[]{String.valueOf(product.getItemId())});

    }

    // Deleting single contact
    public void deleteProduct(Model product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBConstants.TABLE_PRODUCTS, DBConstants.KEY_UPC + " = ? AND " + DBConstants.KEY_TYPE + " = ?",
                new String[]{product.getItemUPC(), (product.getType() == ServiceType.AMAZON ? DBConstants.TYPE_AMAZON : DBConstants.TYPE_WALMART)});
        db.close();
    }

    // Getting contacts Count
  /*  public int getContactsCount() {
        String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }*/
}
