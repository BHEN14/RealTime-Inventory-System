package com.development.abdulraheedautos;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMING = 3000;
    Animation topAnim;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

           super.onCreate(savedInstanceState);
           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
           setContentView(R.layout.activity_main);
           textView = (TextView) findViewById(R.id.sptext);
           topAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
           textView.setAnimation(topAnim);
        try {
           SQLiteDatabase mydatabase = openOrCreateDatabase(Constants.DB_NAME, MODE_PRIVATE, null);
           Cursor resultSet = mydatabase.rawQuery("SELECT * FROM "+Constants.TB_NAME,null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this,dashboard.class);
                    startActivity(i);
                    finish();
                }
            },SPLASH_SCREEN_TIMING);
       }catch(Exception e){
            Dialog d = new Dialog(this);
            d.setTitle("Login Check");
             d.setContentView(R.layout.login);
            EditText serialTEditText = d.findViewById(R.id.serialEditText);
            Button saveBtn = d.findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (serialTEditText.getText().toString().equals("Atif786")) {

                                Intent i = new Intent(MainActivity.this,dashboard.class);
                                startActivity(i);
                                finish();

                    } else {
                        Toast.makeText(MainActivity.this, "Password Invalid", Toast.LENGTH_SHORT).show();
                       //finish();
                    }
                }
            });
            d.show();
       }

    }
    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), perms[0]);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), perms[1]);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(perms[0])) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(perms,
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}

