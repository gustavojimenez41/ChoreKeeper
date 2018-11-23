package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Boolean.TRUE;

public class MyChores extends AppCompatActivity
{
    Button allChores, rewards, home, CreateChore;
    private static final String TAG = "MyChores:";
    DatabaseReference dref;
    FirebaseAuth firebaseauth;
    FirebaseUser user;
    String housecode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chores);
        home = findViewById(R.id.house_button);
        allChores = findViewById(R.id.allChores);
        rewards = findViewById(R.id.rewards);
        CreateChore = findViewById(R.id.createChore);

        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChores.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChores.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChores.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        CreateChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChores.this, CreateChore.class);
                startActivity(intent);
                finish();
            }
        });


        //once added, is triggered for every child
        //then again every time a new child is added.
        dref = FirebaseDatabase.getInstance().getReference("Chores");
        dref.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {


                final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
                housecode = globalVariables.gethousecode();

                FirebaseUser user = firebaseauth.getInstance().getCurrentUser();

                String userId = user.getUid();



                //the snapshot is the chore object
                //if the chore belongs to the current house, and the user then we want the data
                //if not, ignore it
                String chorehousecode = dataSnapshot.child("housecode").getValue(String.class);
                String choreuser = dataSnapshot.child("owner").getValue(String.class);


                if(chorehousecode != null && chorehousecode.equals(housecode)&&choreuser.equals(userId))
                {


                    //retrieve all the attributes
                    String id = dataSnapshot.getKey();
                    String name = (String) dataSnapshot.child("name").getValue();
                    String comments = (String) dataSnapshot.child("comments").getValue();
                    long points = (long)dataSnapshot.child("points").getValue();
                    String owner = (String) dataSnapshot.child("owner").getValue();
                    String stringPoints = Long.toString(points);

                    Chore newchore = new Chore(name,comments,(int)points,id,housecode);


                    //data for the chores that belong to the user

                    /*




                    //add the Id to the list


                    list.add("\n"+name + "\n"+ stringPoints +"pts"+"\n"+comments+"\n");
                    chore_arr.add(name);
                    points_arr.add(stringPoints);
                    descript_arr.add(comments);




                    //Added Code on Sunday to add intent to listview
                    //editIntent.putExtra("name",name);
                    //editIntent.putExtra("points", stringPoints);
                    //editIntent.putExtra("comment",comments);
                    //startActivity(editIntent);
                    //....................................................
                    adapter.notifyDataSetChanged();

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            String name = chore_arr.get(i);
                            String points2 = points_arr.get(i);
                            String comment = descript_arr.get(i);
                            editIntent.putExtra("name",name);
                            editIntent.putExtra("points", points2);
                            editIntent.putExtra("comment",comment);
                            startActivity(editIntent);
                        }




                    });

                    */
                }
                else
                {
                    //Log.e(TAG, "housecode is null");
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



void completeChore(String choreID)
{
    //when the chore is completed, remove the chore from the database
    dref = FirebaseDatabase.getInstance().getReference("Chores/"+choreID);

    //add the point value of the chore to the users points
    dref.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                FirebaseUser user = firebaseauth.getInstance().getCurrentUser();
                int points = dataSnapshot.child("points").getValue(int.class);
                DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        int upoint = dataSnapshot.child("points").getValue(int.class);
                        upoint += points;

                        userref.child("points").setValue(upoint);

                        //remove the chore from the user
                        userref.child(choreID).removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //remove the chore from chores
                dref.removeValue();

            }

            @Override
            public void onCancelled(DatabaseError Error)
            {
                Toast.makeText(MyChores.this, "Complete Chore failed",
                        Toast.LENGTH_LONG).show();

            }
        });






}





}

