package com.example.notesintern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
GoogleSignInClient mGoogleSignInClient;
private static int RC_SIGN_IN=100;
    String personName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPreferences();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Fragment fragment= getSupportFragmentManager().findFragmentById(R.id.fl);
        if (fragment!=null){
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }else{
            super.onBackPressed();
        }

    }

    void checkPreferences(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

       if (!sh.getBoolean("isSignedIn", false)){
           findViewById(R.id.IdSignOut).setVisibility(View.INVISIBLE);
       }
       else
       {
           findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
           getSupportActionBar().setTitle(""+sh.getString("name","")+"-Notes");
           HomeFragment homeFragment=new HomeFragment();
           getSupportFragmentManager()
                   .beginTransaction()
                   .replace(R.id.fl, homeFragment, null)
                   //.addToBackStack(null)
                   .commit();
       }

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                 personName = acct.getDisplayName();
//                String personGivenName = acct.getGivenName();
//                String personFamilyName = acct.getFamilyName();
//                String personEmail = acct.getEmail();
//                String personId = acct.getId();
//                Uri personPhoto = acct.getPhotoUrl();
            }
            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.IdSignOut).setVisibility(View.VISIBLE);
            updatingPreference();
            startNotes();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("msg", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    void startNotes(){
        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl, homeFragment, null)
                //.addToBackStack(null)
                .commit();
    }

    public void addNew(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl, new AddNote(), null)
                //.addToBackStack(null)
                .commit();
    }

    void updatingPreference(){
        getSupportActionBar().setTitle(personName+"-Notes");
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("name", ""+personName);
        myEdit.putBoolean("isSignedIn", true);
// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.commit();
    }

    public void signOutV(View view) {
        Log.d("msg23","out");
        getSupportActionBar().setTitle("" + getIntent().getStringExtra("name") + "-" + "Notes");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signOut();
        updatingOutPreference();
    }
    private void signOut(){
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();// ...
                        }
                    });
        onBackPressed();
        }
    void updatingOutPreference(){
        getSupportActionBar().setTitle("Notes");
        Log.d("msg","we are out");
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("name", ""+"");
        myEdit.putBoolean("isSignedIn", false);
        findViewById(R.id.IdSignOut).setVisibility(View.INVISIBLE);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        myEdit.commit();
    }

    public void viewNotes(View view) {
        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl, homeFragment, null)
                //.addToBackStack(null)
                .commit();
    }
}
