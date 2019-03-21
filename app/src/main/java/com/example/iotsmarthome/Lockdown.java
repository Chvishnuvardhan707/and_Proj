package com.example.iotsmarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Lockdown extends AppCompatActivity {
Switch s1;
    String personId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockdown);
        Bundle extras = getIntent().getExtras();
        personId = extras.getString("userid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(personId).child("Lockdown");
        ref.child("lockdown").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                s1 =(Switch)findViewById(R.id.switch1);
                s1.setChecked(snapshot.getValue(boolean.class));
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

    }

    public void savelock(View view) {
        final long[] passchk = new long[1];
        final long[] pass=new long[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Password");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("hi");
                passchk[0] = (long) snapshot.getValue(long.class);
                System.out.println(passchk[0]);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("something went wrong");

            }
        });
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Password required!!");
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Enter your password to continue");
        final EditText password = new EditText(this);
        builder.setView(password);
        builder.setPositiveButton("Check", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 pass[0] = Long.parseLong(password.getText().toString().trim());
               if(pass[0]==passchk[0]){

                   confirm();
               }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "lockdown mode cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();


    }

    private void confirm() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Lockdown mode ON ?");
        builder1.setMessage("You are about to lockdown your home. Do you really want to proceed ?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference tv = FirebaseDatabase.getInstance().getReference(personId).child("Lockdown").child("lockdown");
                s1 =(Switch)findViewById(R.id.switch1);
                tv.setValue(s1.isChecked());
                Toast.makeText(getApplicationContext(), "home set to LOCKDOWN state!!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),Lockdown.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("userid",personId);
                startActivity(intent);
            }
        });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "lockdown mode cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder1.show();

    }

    public void home(View view) {
    Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("userid",personId);
        startActivity(intent);
    }
}