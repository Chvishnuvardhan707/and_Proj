package com.example.iotsmarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity {
String personId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        personId = extras.getString("userid");
    }

    public void Bed_room(View view) {
        Intent intent1 = new Intent(this,Bed.class);
        intent1.putExtra("userid",personId);
        startActivity(intent1);
    }

    public void Dining_room(View view) {
        Intent intent1 = new Intent(this,Dining.class);
        intent1.putExtra("userid",personId);
        startActivity(intent1);

    }

    public void KItchen(View view) {
        Intent intent1 = new Intent(this,Kitchen.class);
        intent1.putExtra("userid",personId);
        startActivity(intent1);
    }

    public void Living_room(View view) {
        Intent intent1 = new Intent(this,Living.class);
        intent1.putExtra("userid",personId);
        startActivity(intent1);
    }

    public void lockdownn(View view) {
        Intent intent1 = new Intent(this,Lockdown.class);
        intent1.putExtra("userid",personId);
        startActivity(intent1);
    }
    @Override
    public void onBackPressed(){

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to exit ?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


    }
}
