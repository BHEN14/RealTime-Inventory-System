package com.development.abdulraheedautos;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DBAdapter {

    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }
    public boolean updateItem(product prod){
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Constants.ROW_ID,prod.getSerialNo());
            cv.put(Constants.PRODUCT, prod.getProductName());
            cv.put(Constants.CATEGORY,prod.getCategory());
            cv.put(Constants.PRICE,prod.getPrice());
            long result = db.update(Constants.TB_NAME,cv,Constants.ROW_ID+"=?",new String[] {prod.getSerialNo()});

            if (result > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

        return false;
    }

    public boolean deleteItem(product prod){
        try {
            db = helper.getWritableDatabase();
            long result = db.delete(Constants.TB_NAME,Constants.ROW_ID+"=?",new String[] {prod.getSerialNo()});
            if (result > 0) {

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }

    public boolean saveItem(product prod) {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Constants.ROW_ID,prod.getSerialNo());
            cv.put(Constants.PRODUCT, prod.getProductName());
            cv.put(Constants.CATEGORY,prod.getCategory());
            cv.put(Constants.PRICE,prod.getPrice());
            long result = db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
            if (result > 0) {

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

        return false;
    }

    public ArrayList<product> filterProduct(String q){
        ArrayList<product> products=new ArrayList<>();

        String[] columns={Constants.ROW_ID,Constants.PRODUCT,Constants.CATEGORY,Constants.PRICE};

        try
        {
            db = helper.getWritableDatabase();
            String sql = "SELECT * FROM "+Constants.TB_NAME+" WHERE  serialNo LIKE '%"+q+"%' OR product LIKE '%"+q+"%' OR category LIKE '%"+q+"%' order by "+Constants.ROW_ID;
            Log.i("SQL", sql);
            Cursor c= db.rawQuery(sql,null);
            Log.d("E63", String.valueOf(c.getCount()));
            product s;

            if(c != null)
            {
                while (c.moveToNext())
                {
                    String s_name=c.getString(1);
                    String s_category=c.getString(2);
                    int s_price=c.getInt(3);
                    s=new product();
                    s.setSerialNo(c.getString(0));
                    s.setProductName(s_name);
                    s.setCategory(s_category);
                    s.setPrice(s_price);
                    products.add(s);
                    Log.d("filterData", c.getString(0)+":"+s.getProductName());
                }
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


        return products;
    }
    public ArrayList<product> retrieveProduct()
    {
        ArrayList<product> products=new ArrayList<>();
        try
        {
            db = helper.getWritableDatabase();
            Cursor c=db.rawQuery("SELECT * from "+Constants.TB_NAME+ " Order By "+Constants.ROW_ID,null);
            product s;
            if(c != null)
            {
                while (c.moveToNext())
                {
                    Log.d("data110",c.getString(0)+","+c.getString(1)+","+c.getString(2)+","+c.getString(3));
                    String s_name=c.getString(1);
                    String s_category=c.getString(2);
                    int s_price=c.getInt(3);
                    s=new product();
                    s.setSerialNo(c.getString(0));
                    s.setProductName(s_name);
                    s.setCategory(s_category);
                    s.setPrice(s_price);
                    products.add(s);
                    Log.d("DBAdapter", c.getString(0)+":"+s.getProductName());
                }
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


        return products;
    }

}