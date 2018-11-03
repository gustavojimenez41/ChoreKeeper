package com.example.gustavojimenez.chorekeeper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class database
{
    private static final String TAG = "Database:";

    private DatabaseReference dbref;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();


    private FirebaseAuth firebaseauth;

    database(){};
/*
    public void getHousecode()
    {
        String housecode = null;
        final GlobalVar globalVariables = (GlobalVar) ApplicationContextProvider.getContext();

        dbref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        ValueEventListener sethousecode = new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot)
            {


                String housecode = dataSnapshot.getValue(String.class);
                Log.e(TAG, "setting global variable");
                globalVariables.setHousecode(housecode);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        dbref.addListenerForSingleValueEvent(sethousecode);

    }
    */
}
