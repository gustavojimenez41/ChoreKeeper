package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;
import android.util.*;
import android.widget.Toast;

import static java.lang.Boolean.TRUE;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register:";

    RadioButton joinHouse, createHouse;
    EditText joinHouseID;
    Button registerButton;


    private DatabaseReference dbref;
    private FirebaseAuth mfirebaseauth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.registerButton);
        joinHouse = findViewById(R.id.joinHouse);
        joinHouseID = findViewById(R.id.enterHouseID);
        createHouse = findViewById(R.id.createHouse);

        mfirebaseauth = FirebaseAuth.getInstance();

        if(mfirebaseauth.getCurrentUser()!=null)
        {
            signOut();
        }

        joinHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                joinHouseID.setHint("house code");
            }
        });

        createHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                joinHouseID.setHint("House Name");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String pass = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                String mail = ((EditText)findViewById(R.id.enterEmail)).getText().toString();

                createNewUser(pass,mail);

            }
        });
    }


    private void createNewUser(String password, String email)
    {


        mfirebaseauth.createUserWithEmailAndPassword(email,password)

                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mfirebaseauth.getCurrentUser();




                            //sets the display name to the username given by the user
                            String username = ((EditText)findViewById(R.id.enterUsername)).getText().toString();

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


                            //Adds the user to house specified by the user
                            //or creates a new house based on the name
                            ValueEventListener checkIfExists = new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    String hCode;

                                    //checks if the user entered a house code or house name
                                    //and sets hCode accordingly
                                    joinHouse = findViewById(R.id.joinHouse);
                                    String hName = ((EditText)findViewById(R.id.enterHouseID)).getText().toString();
                                    if(!joinHouse.isChecked())
                                    {
                                        //hash the name into a code
                                        hCode = Integer.toString(Math.abs(hName.hashCode()));

                                    }
                                    else
                                    {
                                        hCode = hName;
                                }


                                    //checks if there is a house with the code
                                    boolean exists = false;

                                    for(DataSnapshot data: dataSnapshot.getChildren())
                                    {

                                        if (data.getKey().equals(hCode))
                                        {

                                            exists = true;
                                        }
                                    }


                                    //now know if the given code exists or not
                                    FirebaseUser user =  mfirebaseauth.getCurrentUser();

                                    //user wants to join a house
                                    if(joinHouse.isChecked())
                                    {
                                        //the house exists
                                        if(exists)
                                        {
                                            addUserToHouse(hCode,hName);

                                            Toast.makeText(Register.this, "Account Created",
                                                    Toast.LENGTH_LONG).show();

                                            //everything is fine, go to the home page
                                            Intent intent = new Intent(Register.this, Home.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        //the house does not exist
                                        //notify the user and do nothing
                                        else
                                        {
                                            Toast.makeText(Register.this, "Cannot find house with code " + hCode,
                                                    Toast.LENGTH_LONG).show();

                                            //the user has been created but has not joined a house
                                            //delete the user so they can register again
                                            user.delete();
                                        }
                                    }
                                    //the user wants to create a house
                                    else
                                    {
                                        //that code already exists
                                        //minor issue right now:
                                        //this could be that 2 names hashed to the name code
                                        //very low probability of this
                                        if(exists)
                                        {
                                            Toast.makeText(Register.this, "A House with that name already exists",
                                                    Toast.LENGTH_LONG).show();

                                            //the user has been created but has not joined a house
                                            //delete the user so they can register again
                                            user.delete();
                                        }
                                        //hCode is a unique house code
                                        //add the user to the house and go to the home page
                                        else
                                        {
                                            addUserToHouse(hCode,hName);

                                            Toast.makeText(Register.this, "Account Created",
                                                    Toast.LENGTH_LONG).show();

                                            //everything is fine, go to the home page
                                            Intent intent = new Intent(Register.this, Home.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError Error)
                                {
                                    Toast.makeText(Register.this, "House does not exist",
                                            Toast.LENGTH_LONG).show();

                                }
                            };



                            //adds the listener created above to the database reference, and runs it
                            dbref = db.getReference("Houses");
                            dbref.addListenerForSingleValueEvent(checkIfExists);


                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                         }

                    }
                });



    }

    private void signOut()
    {
        mfirebaseauth.signOut();
    }

    //adds the user currently signed in to the house given by hCode
    private void addUserToHouse(String hCode, String name)
    {
        //create new user
        FirebaseUser user = mfirebaseauth.getCurrentUser();

        User newuser = new User(userCode(user),hCode, user.getUid());
        dbref = db.getReference("Users");
        dbref.child(newuser.getID()).setValue(newuser);

        //add the user to the house
        dbref = db.getReference("Houses/" + hCode + "/members");
        dbref.child(newuser.getID()).setValue(TRUE);

        //only add the name to the house if creating a new house
        //if hName = hCode, user is joining a house
        if(!name.equals(hCode))
        {
            dbref = db.getReference("Houses/" + hCode + "/name");
            dbref.setValue(name);
        }



    }

    //takes in a user and gives a code based on the username
    private String userCode(FirebaseUser user)
    {
        String username = user.getDisplayName();
        return Integer.toString(Math.abs(username.hashCode()));
    }
}


