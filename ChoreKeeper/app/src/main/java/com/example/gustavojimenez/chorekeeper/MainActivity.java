package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button registerButton;
    Button login;
    Button resetPass;
    DatabaseReference dref;
    //these two are for authentication system
    FirebaseAuth firebaseauth;
    FirebaseUser user;
    String housecode = null;

    private static final String TAG = "Main Activity: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebaseauth.getInstance();
        user = firebaseauth.getInstance().getCurrentUser();



        //go straight to the home page if the user is already signed in
        if(user!=null)//this is sometimes working
        {
            Log.e(TAG,"user id: " + user.getUid());
            globalVariables.setUserid(user.getUid());

            Intent intent = new Intent(MainActivity.this,MainActivityFragment.class);

            startActivity(intent);
            finish();
        }

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                //finish();
            }
        });
        resetPass = findViewById(R.id.resetPassword);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivityFragment.class);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {



                String pass = ((EditText)findViewById(R.id.enterPasswordMain)).getText().toString();
                String mail = ((EditText)findViewById(R.id.enterEmailMain)).getText().toString();


                Boolean good = true;


                if(TextUtils.isEmpty(pass))
                {
                    ((EditText)findViewById(R.id.enterPasswordMain)).setError("Cannot be empty");
                    good = false;
                }
                if(TextUtils.isEmpty(mail))
                {
                    ((EditText)findViewById(R.id.enterEmailMain)).setError("Cannot be empty");
                    good = false;
                }
                if(good)
                {
                    signIn(mail, pass);
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                    user = firebaseauth.getCurrentUser();

                    //if the sign in works, go to home page
                    if(user!=null)
                    {
                        globalVariables.setUserid(user.getUid());

                        Intent intent = new Intent(MainActivity.this, MainActivityFragment.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Username or Password incorrect",
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }



    private void signIn(String email, String password)
    {
        firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = firebaseauth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(),
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Sign In failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.actionBarSettings)
            Toast.makeText(this,"works",Toast.LENGTH_SHORT);
        return super.onOptionsItemSelected(item);
    }
}

