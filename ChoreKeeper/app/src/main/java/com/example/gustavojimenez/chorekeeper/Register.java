package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;
import android.util.*;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RadioButton joinHouse, createHouse;
    EditText joinHouseID;
    Button registerButton;
    EditText uname;
    private FirebaseUser mfirebaseuser;
    private DatabaseReference mdbref;
    private FirebaseAuth mfirebaseauth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.registerButton);
        joinHouse = findViewById(R.id.joinHouse);
        joinHouseID = findViewById(R.id.enterHouseID);
        createHouse = findViewById(R.id.createHouse);

        mfirebaseauth = FirebaseAuth.getInstance();

        joinHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                joinHouseID.setVisibility(View.VISIBLE);
            }
        });

        createHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                joinHouseID.setVisibility(View.GONE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String username = findViewById(R.id.enterUsername).toString();
                String pass = findViewById(R.id.enterPassword).toString();
                String mail = findViewById(R.id.enterEmail).toString();

                createNewUser(username,pass,mail);

                Intent intent = new Intent(Register.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void createNewUser(String username, String password, String email)
    {


        //create user with firebase's authentication system

        mfirebaseauth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mfirebaseauth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }



                    }
                });


        FirebaseUser user = mfirebaseauth.getCurrentUser();

        // Updates the user attributes:

        if(user!=null)
        {
            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(updates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });


            //add the additional attributes to the database
            //need someway to genrate the housecode then put it there
            User User = new User(user.getUid(),"housecode");
            mdbref.child("users").child(User.getID()).setValue(User);
        }






    }
}


