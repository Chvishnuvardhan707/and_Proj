package com.example.iotsmarthome;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_in extends AppCompatActivity {
private SignInButton signin;
String personId;
GoogleApiClient mGoogleApiClient;
private static final int RC_SIGN_IN=1;
    private FirebaseAuth mAuth;
    GoogleSignInClient mgoogleSignInClient;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();
         signin = (SignInButton)findViewById(R.id.sign_in_button);
        signin.setSize(SignInButton.SIZE_STANDARD);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
}
private void signIn()
{
    Intent intent = mgoogleSignInClient.getSignInIntent();
    startActivityForResult(intent,RC_SIGN_IN);
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Sign_in", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Sign_in", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign_in", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("hello");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sign_in", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Sign_in.this, "failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
             personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(personId);
            ref.child("Living_Room").child("fan").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String data = snapshot.getValue(boolean.class).toString();
                    if (data == null ) {
                        SetPredefinedFunctions();
                        openHomePage();
                    }
if(!data.isEmpty()){                        openHomePage();
                    }
                }
                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }
    }

    private void openHomePage() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("userid",personId);
        startActivity(intent);

    }

    private void SetPredefinedFunctions() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(personId);
        boolean dont=false;
        ref.child("Bed").child("airconditioner").setValue(dont);
        ref.child("Bed").child("bedlamp").setValue(dont);
        ref.child("Bed").child("fan").setValue(dont);
        ref.child("Bed").child("television").setValue(dont);
        ref.child("Bed").child("tubelight").setValue(dont);

        ref.child("Dining").child("fan").setValue(dont);
        ref.child("Dining").child("smalllight").setValue(dont);
        ref.child("Dining").child("tubelight").setValue(dont);

        ref.child("Kitchen").child("fan").setValue(dont);
        ref.child("Kitchen").child("fridge").setValue(dont);
        ref.child("Kitchen").child("smalllight").setValue(dont);
        ref.child("Kitchen").child("tubelight").setValue(dont);

        ref.child("Living_Room").child("fan").setValue(dont);
        ref.child("Living_Room").child("smalllight").setValue(dont);
        ref.child("Living_Room").child("television").setValue(dont);
        ref.child("Living_Room").child("tubelight").setValue(dont);

        ref.child("Lockdown").child("lockdown").setValue(dont);
        ref.child("Password").setValue("1234");






    }
}
