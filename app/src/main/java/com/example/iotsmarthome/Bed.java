package com.example.iotsmarthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bed extends AppCompatActivity {
    Switch s1;Switch s2;Switch s3;Switch s4;Switch s5;

    String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed);
        Bundle extras = getIntent().getExtras();
         personId= extras.getString("userid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(personId).child("Bed");
        ref.child("fan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s3 =(Switch)findViewById(R.id.switch3);
                s3.setChecked(snapshot.getValue(boolean.class));
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("bedlamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s2 =(Switch)findViewById(R.id.switch2);
                s2.setChecked(snapshot.getValue(boolean.class));
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("airconditioner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s4 =(Switch)findViewById(R.id.switch4);
                s4.setChecked(snapshot.getValue(boolean.class));
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("tubelight").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s1 =(Switch)findViewById(R.id.switch1);
                s1.setChecked(snapshot.getValue(boolean.class));
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("television").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s5 =(Switch)findViewById(R.id.switch5);
                s5.setChecked(snapshot.getValue(boolean.class));
            }


            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

        Button button = (Button) findViewById(R.id.savebed);
        button.setBackgroundColor(Color.MAGENTA);
    }

    public void home(View view) {
        Intent intent =new Intent(this,MainActivity.class);
        intent.putExtra("userid",personId);
        startActivity(intent);
    }


    public void savebed(View view) {
        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference tbl = fbd.getReference(personId).child("Bed").child("tubelight");
        DatabaseReference bdl = FirebaseDatabase.getInstance().getReference(personId).child("Bed").child("bedlamp");
        DatabaseReference f = FirebaseDatabase.getInstance().getReference(personId).child("Bed").child("fan");
        DatabaseReference tv = FirebaseDatabase.getInstance().getReference(personId).child("Bed").child("television");
        DatabaseReference air = FirebaseDatabase.getInstance().getReference(personId).child("Bed").child("airconditioner");
        s1 =(Switch)findViewById(R.id.switch1);
        s2 =(Switch)findViewById(R.id.switch2);
        s3 =(Switch)findViewById(R.id.switch3);
        s4 =(Switch)findViewById(R.id.switch4);
        s5 =(Switch)findViewById(R.id.switch5);

        tbl.setValue(s1.isChecked());
        bdl.setValue(s2.isChecked());
        f.setValue(s3.isChecked());
        tv.setValue(s4.isChecked());
        air.setValue(s5.isChecked());
        air.setValue(s5.isChecked()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Button button = (Button)findViewById(R.id.savebed);
                button.setBackgroundColor(Color.GREEN);
                Toast.makeText(Bed.this, "changed your preferences!!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(),Bed.class);
                intent1.setFlags(intent1.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent1.putExtra("userid",personId);
                startActivity(intent1);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Button button = (Button)findViewById(R.id.savebed);
                        button.setBackgroundColor(Color.RED);
                        Toast.makeText(Bed.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
