package com.development.abdulraheedautos;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class dashboard extends AppCompatActivity {


    SearchView query;
    EditText nameEditText, propellantEditTxt, destEditTxt;
    Button saveBtn;

    TextView sample;
    TableLayout tb;
    tableHelper table_Helper;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

            if (ContextCompat.checkSelfPermission(dashboard.this,
                    Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(dashboard.this, "You have already granted this permission!",
                        Toast.LENGTH_SHORT).show();
            } else {
                requestStoragePermission();
            }


            table_Helper = new tableHelper(this);
            String[][] dat = table_Helper.getProductProbes();
            tb = findViewById(R.id.tableData);
            sample = findViewById(R.id.sample);
            loadData();
            query = findViewById(R.id.query);
            query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterData(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterData(newText);
                    return false;
                }
            });


        } catch (Exception e) {
            toast(e.getMessage());
            Log.d("dashboard", e.getMessage());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(dashboard.this,
                                    new String[]{Manifest.permission.INTERNET}, 1);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate(String s) {
        return s.trim().length() > 0;
    }

    public void up(View v) {
        Dialog d = new Dialog(this);
        d.setTitle("Update Product");
        d.setContentView(R.layout.update);
        //INITIALIZE VIEWS
        EditText serialTEditText = d.findViewById(R.id.serialEditText);
        nameEditText = d.findViewById(R.id.nameEditTxt);
        propellantEditTxt = d.findViewById(R.id.propEditTxt);
        destEditTxt = d.findViewById(R.id.destEditTxt);
        saveBtn = d.findViewById(R.id.saveBtn);
        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(serialTEditText.getText().toString()) && validate(nameEditText.getText().toString()) && validate(propellantEditTxt.getText().toString()) && validate(destEditTxt.getText().toString())) {
                    product s = new product();
                    s.setSerialNo(serialTEditText.getText().toString().length() == 0 ? "N/A" : serialTEditText.getText().toString());
                    s.setProductName(nameEditText.getText().toString().length() == 0 ? "N/A" : nameEditText.getText().toString());
                    s.setCategory(propellantEditTxt.getText().toString().length() == 0 ? "N/A" : propellantEditTxt.getText().toString());
                    s.setPrice(Integer.valueOf(destEditTxt.getText().toString().length() == 0 ? "0" : destEditTxt.getText().toString()));


                        if (new DBAdapter(dashboard.this).updateItem(s)) {
                            nameEditText.setText("");
                            propellantEditTxt.setText("");
                            destEditTxt.setText("");
                            loadData();
                            d.dismiss();
                        } else {
                            Toast.makeText(dashboard.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }

                } else {
                    toast("Information  Cannot Be Empty");
                }
            }
        });
        //SHOW DIALOG
        d.show();
    }

    public void nw(View v) {
        displayDialog();
    }

    public void del(View v) {

        Dialog d = new Dialog(this);
        d.setTitle("Delete Product");
        d.setContentView(R.layout.dialog_delete);
        nameEditText = d.findViewById(R.id.nameEditTxt);
        saveBtn = d.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(nameEditText.getText().toString())) {
                    product s = new product();
                    s.setSerialNo(nameEditText.getText().toString().length() == 0 ? "N/A" : nameEditText.getText().toString());


                        if (new DBAdapter(dashboard.this).deleteItem(s)) {
                            nameEditText.setText("");
                            loadData();
                            d.dismiss();
                        } else {
                            Toast.makeText(dashboard.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }

                } else {
                    toast("Information Cannot Be Empty");
                }
            }
        });

        //SHOW DIALOG
        d.show();
    }

    public void filterData(String q) {
            try {

                String[][] data = table_Helper.getFilterProbes(q);
                //tb.removeAllViews();
                refresh(data.length);

                for (int i = 0; i < data.length; i++) {
                    TableRow tableRow = new TableRow(getApplicationContext());
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    layoutParams.width = TableRow.LayoutParams.MATCH_PARENT;
                    layoutParams.height = TableRow.LayoutParams.WRAP_CONTENT;
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    tableRow.setLayoutParams(layoutParams);
                    TextView textView = new TextView(getApplicationContext());

                    textView.setLayoutParams(sample.getLayoutParams());

                    textView.setTextSize(14);
                    // textView.setBackgroundColor(R.color.green);
                    textView.setTextColor(Color.WHITE);
                    textView.setText(data[i][0]);
                    textView.setPadding(5, 5, 5, 5);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(textView, 0);


                    TextView textView1 = new TextView(getApplicationContext());
                    textView1.setLayoutParams(sample.getLayoutParams());
                    textView1.setTextSize(14);
                    textView1.setTextColor(Color.WHITE);
                    textView1.setText(data[i][1]);
                    textView1.setPadding(5, 5, 5, 5);
                    textView1.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(textView1, 1);

                    TextView textView2 = new TextView(getApplicationContext());
                    textView2.setLayoutParams(sample.getLayoutParams());
                    textView2.setTextSize(14);
                    textView2.setTextColor(Color.WHITE);
                    textView2.setText(data[i][2]);
                    textView2.setPadding(5, 5, 5, 5);
                    textView2.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(textView2, 2);

                    TextView textView3 = new TextView(getApplicationContext());
                    textView3.setLayoutParams(sample.getLayoutParams());
                    textView3.setTextSize(14);
                    textView3.setTextColor(Color.WHITE);
                    textView3.setText(data[i][3]);
                    textView3.setPadding(5, 5, 5, 5);
                    textView3.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(textView3, 3);

                    tb.addView(tableRow);


                    Log.d("D70", Arrays.toString(data[i]));
                }

            } catch (Exception e) {
                Toast.makeText(this, "\"" + q + "\" NOT FOUND", Toast.LENGTH_SHORT);
            }

    }

    private void refresh(int l) {
        int childs = tb.getChildCount();
        if (childs > 0) {
            tb.removeViews(0, childs);
        }
    }

    private void loadData() {

            String[][] data = table_Helper.getProductProbes();
            refresh(data.length);
            //tb.removeAllViews();

            for (int i = 0; i < data.length; i++) {
                TableRow tableRow = new TableRow(getApplicationContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                layoutParams.width = TableRow.LayoutParams.MATCH_PARENT;
                layoutParams.height = TableRow.LayoutParams.WRAP_CONTENT;
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                tableRow.setLayoutParams(layoutParams);
                TextView textView = new TextView(getApplicationContext());


                textView.setLayoutParams(sample.getLayoutParams());
                textView.setTextSize(14);
                textView.setTextColor(Color.WHITE);
                textView.setText(data[i][0]);
                textView.setPadding(5, 5, 5, 5);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.addView(textView, 0);


                TextView textView1 = new TextView(getApplicationContext());
                textView1.setLayoutParams(sample.getLayoutParams());
                textView1.setTextSize(14);
                textView1.setTextColor(Color.WHITE);
                textView1.setText(data[i][1]);
                textView1.setPadding(5, 5, 5, 5);
                textView1.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.addView(textView1, 1);

                TextView textView2 = new TextView(getApplicationContext());
                textView2.setLayoutParams(sample.getLayoutParams());
                textView2.setTextSize(14);
                textView2.setTextColor(Color.WHITE);
                textView2.setText(data[i][2]);
                textView2.setPadding(5, 5, 5, 5);
                textView2.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.addView(textView2, 2);

                TextView textView3 = new TextView(getApplicationContext());
                textView3.setLayoutParams(sample.getLayoutParams());
                textView3.setTextSize(14);
                textView3.setTextColor(Color.WHITE);
                textView3.setText(data[i][3]);
                textView3.setPadding(5, 5, 5, 5);
                textView3.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.addView(textView3, 3);
                tb.addView(tableRow);
                Log.d("D70", Arrays.toString(data[i]));
            }

    }

    private void toast(String value) {
        Toast.makeText(dashboard.this, value, Toast.LENGTH_LONG).show();
    }

    private void displayDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("New Product");
        d.setContentView(R.layout.layout_dialog);

        //INITIALIZE VIEWS
        EditText serialNo = d.findViewById(R.id.serialEditText);
        nameEditText = d.findViewById(R.id.nameEditTxt);
        propellantEditTxt = d.findViewById(R.id.propEditTxt);
        destEditTxt = d.findViewById(R.id.destEditTxt);

        saveBtn = d.findViewById(R.id.saveBtn);


        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(serialNo.getText().toString()) && validate(nameEditText.getText().toString()) && validate(propellantEditTxt.getText().toString()) && validate(destEditTxt.getText().toString())) {
        Toast.makeText(dashboard.this, serialNo.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        product s = new product();
                        s.setSerialNo(serialNo.getText().toString().length() == 0 ? "N/A" : serialNo.getText().toString());
                        s.setProductName(nameEditText.getText().toString().length() == 0 ? "N/A" : nameEditText.getText().toString());
                        s.setCategory(propellantEditTxt.getText().toString().length() == 0 ? "N/A" : propellantEditTxt.getText().toString());
                        s.setPrice(Integer.valueOf(destEditTxt.getText().toString().length() == 0 ? "0" : destEditTxt.getText().toString()));


                        if (new DBAdapter(dashboard.this).saveItem(s)) {

                            nameEditText.setText("");
                            propellantEditTxt.setText("");
                            destEditTxt.setText("");
                            loadData();
                            //       tb.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), table_Helper.getProductProbes()));
                            //  tb.setHeaderAdapter(new SimpleTableHeaderAdapter(getApplicationContext(), table_Helper.getProductProbes()[0]));
                            d.dismiss();
                        } else {
                            Toast.makeText(dashboard.this, "Not Saved", Toast.LENGTH_SHORT).show();
                        }

                } else {
                    toast("Information  Cannot Be Empty");

                }
            }
        });

        //SHOW DIALOG
        d.show();


    }
}