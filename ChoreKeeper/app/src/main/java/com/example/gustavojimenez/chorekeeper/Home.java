package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    DatabaseReference dref;
    String housecode = null;
    int i = 0;
    private static final String TAG = "Home:";

    ListView userListView;
    String[] users;
    String[] scores;
    Button allChores, myChores, rewards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources res = getResources();
        userListView = (ListView) findViewById(R.id.userListView);
        users = res.getStringArray(R.array.users);

        scores = res.getStringArray(R.array.scores);
        allChores = findViewById(R.id.all_chores);
        myChores = findViewById(R.id.my_chores);
        rewards = findViewById(R.id.Rewards);
        userAdapter userAdapter = new userAdapter(this, users, scores);
        userListView.setAdapter(userAdapter);

        //instance of the global variables
        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();

        Log.e(TAG,globalVariables.gethousecode());


        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), UserDetails.class);
                showDetailActivity.putExtra("com.example.gustavojimenez.USER_INDEX",i);
                startActivity(showDetailActivity);
            }
        });

        rewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Home.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Home.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });

        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Home.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });



        //getting information of the users to display
        //get the current housecode
        ValueEventListener sethousecode = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                housecode  = dataSnapshot.getValue(String.class);
                globalVariables.setHousecode(housecode);
                Log.e(TAG, "current house code is: "+housecode);
                Log.e(TAG, "setting global varibale");




                //once added, is triggered for every child, then once every time a new child is added
                //then again every time a new child is added.
                dref = FirebaseDatabase.getInstance().getReference("Users");
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {


                        String userhousecode = dataSnapshot.child("houseCode").getValue(String.class);
                        Log.e(TAG, "checking  user houseCode: " + userhousecode);

                        if(userhousecode != null && userhousecode.equals(housecode))
                        {

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();

                            //retrieve all the attributes

                            String userid = dataSnapshot.getKey();
                            long points = (long)dataSnapshot.child("points").getValue();
                            String username = user.getDisplayName();



                            //this is where you display the data
                        }
                        else
                        {
                            Log.e(TAG, "Not the current house");
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
        Log.e(TAG, "uesrid is :"+ user.getUid());
        dref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        dref.addListenerForSingleValueEvent(sethousecode);



    }

    public void goToSettings(View view) {
        Intent intent = new Intent(Home.this, SettingsActivity.class);
        startActivity(intent);
    }
}