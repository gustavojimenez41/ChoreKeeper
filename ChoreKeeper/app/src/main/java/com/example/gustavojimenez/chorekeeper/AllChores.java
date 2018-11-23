package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;


public class AllChores extends AppCompatActivity {

    String housecode = null;
    Button allChores, myChores, rewards,createchore, house;
    ArrayList<String> chore_arr = new ArrayList<String>();
    ArrayList<String> points_arr = new ArrayList<String>();
    ArrayList<String> descript_arr = new ArrayList<String>();



    private static final String TAG = "AllChores:";

    DatabaseReference dref;
    ListView listview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chores);

        allChores = findViewById(R.id.all_chores2);
        myChores = findViewById(R.id.my_chores2);
        rewards = findViewById(R.id.rewards2);
        createchore = findViewById(R.id.createChore2);
        house = findViewById(R.id.house_button2);

        //Added on sunday to help listview
        Intent editIntent = new Intent(AllChores.this, AssignChore.class);
        //..................

        listview = findViewById(R.id.allChoresListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
        housecode = globalVariables.gethousecode();
        //Log.e(TAG,"housecode global variable testing: "+housecode);
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllChores.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        rewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });

        createchore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, CreateChore.class);
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

                Log.e(TAG, housecode);



                //once added, is triggered for every child
                //then again every time a new child is added.
                dref = FirebaseDatabase.getInstance().getReference("Chores");
                dref.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        //the snapshot is the chore object
                        //if the chore belongs to the current house, then we want the data
                        //if not, ignore it
                        String chorehousecode = dataSnapshot.child("housecode").getValue(String.class);


                        if(chorehousecode != null && chorehousecode.equals(housecode))
                        {



                            //retrieve all the attributes
                            String id = dataSnapshot.getKey();
                            String name = (String) dataSnapshot.child("name").getValue();
                            String comments = (String) dataSnapshot.child("comments").getValue();
                            long points = (long)dataSnapshot.child("points").getValue();
                            String owner = (String) dataSnapshot.child("owner").getValue();
                            String stringPoints = Long.toString(points);

                            Chore newchore = new Chore(name,comments,(int)points,id,housecode);


                            //adds the chores belonging to the house to the global variable
                            final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
                            if(globalVariables.getChores()== null || !containsChore(globalVariables.getChores(),newchore))
                            {
                                globalVariables.addHouseChore(newchore);
                            }


                            //add the Id to the list

                            list.add("\n"+name + "\n"+ stringPoints +"pts"+"\n"+comments+"\n");
                            chore_arr.add(name);
                            points_arr.add(stringPoints);
                            descript_arr.add(comments);





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

    //returns true if the user list contains that user
    //returns false if it does not
    public boolean containsChore(List<Chore> list, Chore c)
    {
        int i = 0;
        while(i<list.size())
        {
            if(list.get(i).getID().equals(c.getID()))
            {
                return true;
            }
            i++;
        }

        return false;


    }



}
