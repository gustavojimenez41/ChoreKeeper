package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ProgressBar;

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

    //this spinner will spin once user clicks register to show we are trying to connect with database
    private ProgressBar spinner;

    //information needed to use Firebase
    private DatabaseReference dbref;
    private FirebaseAuth auth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //loads up screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //linking items with screen
        registerButton = findViewById(R.id.registerButton);
        joinHouse = findViewById(R.id.joinHouse);
        joinHouseID = findViewById(R.id.enterHouseID);
        createHouse = findViewById(R.id.createHouse);
        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        //if someone is currently signed in, we sign them out?
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            signOut();
        }

        //if selects "join house" radio button, app will ask for house code input
        joinHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                joinHouseID.setHint("house code");
            }
        });
        //if selects "create house" radio button, app will ask for house name input
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

                //getting information that the user entered
                String pass = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                String mail = ((EditText)findViewById(R.id.enterEmail)).getText().toString();
                String user = ((EditText)findViewById(R.id.enterUsername)).getText().toString();
                String houseID = ((EditText)findViewById(R.id.enterHouseID)).getText().toString();

                //here we are checking to make sure user entered all information
                //else we do not create new user
                Boolean good = true;
                //note: TextUtils.isEmpty(String) returns true if string is null or 0-length
                if(TextUtils.isEmpty(pass))
                {
                    ((EditText)findViewById(R.id.enterPassword)).setError("Cannot be empty");
                    good = false;
                }
                if(TextUtils.isEmpty(mail))
                {
                    ((EditText)findViewById(R.id.enterEmail)).setError("Cannot be empty");
                    good = false;
                }
                if(TextUtils.isEmpty(user))
                {
                    ((EditText)findViewById(R.id.enterUsername)).setError("Cannot be empty");
                    good = false;
                }
                if(TextUtils.isEmpty(houseID))
                {
                    ((EditText)findViewById(R.id.enterHouseID)).setError("Cannot be empty");
                    good = false;
                }
                if(good)
                {
                    createNewUser(pass,mail);
                    spinner.setVisibility(view.VISIBLE);
                }

            }
        });
    }

    //main objective of createNewUser is to put information into database
    private void createNewUser(String password, String email)
    {
        //" To handle success and failure in the same listener, attach an OnCompleteListener: "
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener
                (Register.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //Task completed successfully
                        if (task.isSuccessful())
                        {
                            //not sure what this is
                            Log.d(TAG, "createUserWithEmail:success");

                            //retrieving current user information
                            FirebaseUser user = auth.getCurrentUser();


                            //sets the global
                            final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
                            globalVariables.setUserid(user.getUid());

                            //sets the display name to the username given by the user
                            //username not set with createUserWithEmailAndPassword
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
                                    FirebaseUser user =  auth.getCurrentUser();

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
                                            Intent intent = new Intent(Register.this, MainActivityFragment.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        //the house does not exist
                                        //notify the user and do nothing
                                        else
                                        {
                                            Toast.makeText(Register.this, "Cannot find house with code " + hCode,
                                                    Toast.LENGTH_LONG).show();

                                            spinner.setVisibility(View.GONE);

                                            //the user has been created but has not joined a house
                                            //delete the user so they can register again
                                            user.delete();
                                        }
                                    }
                                    //the user wants to create a house
                                    else
                                    {

                                        if(exists)
                                        {
                                            Toast.makeText(Register.this, "A House with that name already exists",
                                                    Toast.LENGTH_LONG).show();

                                            spinner.setVisibility(View.GONE);

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
                                            Intent intent = new Intent(Register.this, MainActivityFragment.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError Error)
                                {
//                                    Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(),
//                                            Toast.LENGTH_LONG).show();

                                    spinner.setVisibility(View.GONE);

                                }
                            };



                            //adds the listener created above to the database reference, and runs it
                            dbref = db.getReference("Houses");
                            dbref.addListenerForSingleValueEvent(checkIfExists);


                        }
                        // Task failed with an exception
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                            spinner.setVisibility(View.GONE);
                         }
                    }
                });



    }

    private void signOut()
    {
        auth.signOut();
    }


    //adds the user currently signed in to the house given by hCode
    private void addUserToHouse(String hCode, String name)
    {
        //create new user
        FirebaseUser user = auth.getCurrentUser();

        User newUser = new User(user.getUid(),hCode);
        dbref = db.getReference("Users");
        dbref.child(newUser.getID()).setValue(newUser);

        //add the user to the house
        //if the house doesn't exist yet it will create it
        dbref = db.getReference("Houses/" + hCode + "/members");
        dbref.child(newUser.getID()).setValue(TRUE);

        //only add the name to the house if creating a new house
        //if hName = hCode, user is joining a house
        if(!name.equals(hCode))
        {
            dbref = db.getReference("Houses/" + hCode + "/name");
            dbref.setValue(name);
        }



    }

}


