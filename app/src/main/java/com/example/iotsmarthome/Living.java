package com.example.iotsmarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Living extends AppCompatActivity {
    String personId;
    Switch s1;Switch s2;Switch s3;Switch s4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        Bundle extras = getIntent().getExtras();
        personId = extras.getString("userid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(personId).child("Living_Room");
        ref.child("fan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s3 =(Switch)findViewById(R.id.switch3);
                s3.setChecked(snapshot.getValue(boolean.class));
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("smalllight").addListenerForSingleValueEvent(new ValueEventListener() {
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
        ref.child("television").addListenerForSingleValueEvent(new ValueEventListener() {
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
                // data available in snapshot.value()
                s1 =(Switch)findViewById(R.id.switch1);
                s1.setChecked(snapshot.getValue(boolean.class));
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        Button button = (Button) findViewById(R.id.saveliving);
        button.setBackgroundColor(Color.MAGENTA);
            }

    public void home(View view) {
        Intent intent =new Intent(this,MainActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("userid",personId);
        startActivity(intent);
    }

    public void saveliving(View view) {
        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference tbl = fbd.getReference(personId).child("Living_Room").child("tubelight");
        DatabaseReference sml = FirebaseDatabase.getInstance().getReference(personId).child("Living_Room").child("smalllight");
        DatabaseReference f = FirebaseDatabase.getInstance().getReference(personId).child("Living_Room").child("fan");
        DatabaseReference tv = FirebaseDatabase.getInstance().getReference(personId).child("Living_Room").child("television");
        s1 =(Switch)findViewById(R.id.switch1);
        s2 =(Switch)findViewById(R.id.switch2);
        s3 =(Switch)findViewById(R.id.switch3);
        s4 =(Switch)findViewById(R.id.switch4);

        tbl.setValue(s1.isChecked());
        sml.setValue(s2.isChecked());
        f.setValue(s3.isChecked());
        tv.setValue(s4.isChecked());
        Button button = (Button) findViewById(R.id.saveliving);
        button.setBackgroundColor(Color.GREEN);
        tv.setValue(s4.isChecked()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                refresh();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Button button = (Button) findViewById(R.id.saveliving);
                        button.setBackgroundColor(Color.RED);
                        Toast.makeText(Living.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void refresh() {
        try {
            Thread.sleep(1500);
            Toast.makeText(Living.this, "changed your preferences!!", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(),Living.class);
            intent1.setFlags(intent1.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent1.putExtra("userid",personId);
            startActivity(intent1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
