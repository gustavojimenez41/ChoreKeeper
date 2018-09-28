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
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import android.support.annotation.NonNull;
import android.util.*;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private static final String TAG = "This is it:";

    RadioButton joinHouse, createHouse;
    EditText joinHouseID;
    Button registerButton;
    EditText uname;
    private FirebaseUser mfirebaseuser;
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


                String username = ((EditText)findViewById(R.id.enterUsername)).getText().toString();

                String pass = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                String mail = ((EditText)findViewById(R.id.enterEmail)).getText().toString();

                int valid = createNewUser(username,pass,mail);

                if(valid == 1)
                {
                    Intent intent = new Intent(Register.this, Home.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    //authentication failed
                    //want to empty the text fields and notify the user
                    //the reason for failure
                    //right now a toast pops up with the error message
                    //at least it is supposed to
                    //it only works sometimes haven't figured that one out
                }

            }
        });
    }


    private int createNewUser(String username, String password, String email)
    {

        //create user with firebase's authentication system
        FirebaseUser user;

        mfirebaseauth.createUserWithEmailAndPassword(email,password)

                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mfirebaseauth.getCurrentUser();

                            Toast.makeText(Register.this, "Account Created",
                                    Toast.LENGTH_LONG).show();

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

        user = mfirebaseauth.getCurrentUser();

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
            //need someway to generate the housecode then put it there
            User User = new User(user.getUid(),"housecode");
            dbref = db.getReference("users");
            dbref.child(User.getID()).setValue(User);

            return 1;
        }
        else
            return 0;


    }

    private void signOut()
    {
        mfirebaseauth.signOut();
    }
}


