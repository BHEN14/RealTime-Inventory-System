package com.development.abdulraheedautos;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class tableHelper {

    //DECLARATIONS
    Context c;
    private String[] productHeaders ={"Serial#","Name","Cat","Price"};
    private String[][] productProbes;

    //CONSTRUCTOR
    public tableHelper(Context c) {
        this.c = c;
    }

    public String[] getProductHeaders()
    {
        return productHeaders;
    }
    public String[][] getFilterProbes(String q){
        ArrayList<product> products=new DBAdapter(c).filterProduct(q);
        product s;
        productProbes = new String[products.size()][4];
        for (int i=0;i<products.size();i++) {
            s=products.get(i);
            productProbes[i][0] = s.getSerialNo();
            productProbes[i][1]=s.getProductName();
            productProbes[i][2]=s.getCategory();
            productProbes[i][3]=String.valueOf(s.getPrice());
        }
        return productProbes;
    }
    public  String[][] getProductProbes()
    {
        try {
            ArrayList<product> products = new DBAdapter(c).retrieveProduct();
            product s;
            productProbes = new String[products.size()][4];
            for (int i = 0; i < products.size(); i++) {
                s = products.get(i);
                productProbes[i][0] = s.getSerialNo();
                productProbes[i][1] = s.getProductName();
                productProbes[i][2] = s.getCategory();
                productProbes[i][3] = String.valueOf(s.getPrice());
            }
            return productProbes;
        }
        catch(Exception e){
            Log.d("E67",e.getMessage());
        }
     return null;
    }
}