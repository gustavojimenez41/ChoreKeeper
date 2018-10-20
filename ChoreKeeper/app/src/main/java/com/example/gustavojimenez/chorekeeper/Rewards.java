package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Rewards extends AppCompatActivity {
    Button allChores, myChores, home, createRewards;
    String housecode;
    DatabaseReference dref;

    private static final String TAG = "Rewards:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        home = findViewById(R.id.house_button);
        allChores = findViewById(R.id.allChores);
        myChores = findViewById(R.id.myChores);
        createRewards = findViewById(R.id.createRewards);
        createRewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, CreateReward.class);
                startActivity(intent);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });




        //get the current housecode
        ValueEventListener sethousecode = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                housecode  = dataSnapshot.getValue(String.class);



                //once added, is triggered for every child, then once every time a new child is added
                //then again every time a new child is added.
                dref = FirebaseDatabase.getInstance().getReference("Rewards");
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //the snapshot is the chore object
                        //if the chore belongs to the current house, then we want the data
                        //if not, ignore it


                        String rewardhousecode = dataSnapshot.child("housecode").getValue(String.class);
                        Log.e(TAG, "checking housecode: " + rewardhousecode);

                        if(rewardhousecode != null && rewardhousecode.equals(housecode))
                        {


                            String rewardID = dataSnapshot.getKey();

                            //retrieve all the attributes
                            String name = (String) dataSnapshot.child("name").getValue();
                            String comments = (String) dataSnapshot.child("comments").getValue();
                            long points = (long)dataSnapshot.child("points").getValue();
                            String stringPoints = Long.toString(points);


                            //this is where you do whatever you need to do with the data
                        }
                        else
                        {
                            //Log.e(TAG, "housecode is not current housecode");
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError Error)
            {

            }
        };




        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        dref.addListenerForSingleValueEvent(sethousecode);
    }
}

