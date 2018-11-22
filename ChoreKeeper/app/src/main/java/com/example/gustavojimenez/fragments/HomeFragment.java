package com.example.gustavojimenez.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gustavojimenez.chorekeeper.GlobalVar;
import com.example.gustavojimenez.chorekeeper.R;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gustavojimenez.chorekeeper.UserDetails;
import com.example.gustavojimenez.chorekeeper.userAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    DatabaseReference dref;
    String housecode = null;
    int i = 0;
    private static final String TAG = "Home:";

    ListView userListView;
    String[] users;
    String[] scores;
    Button allChores, myChores, rewards, homeTemp;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Resources res = getResources();
        userListView = (ListView) view.findViewById(R.id.fragmentMyHomeListView);
        users = res.getStringArray(R.array.users);
        scores = res.getStringArray(R.array.scores);
        userAdapter userAdapter = new userAdapter(getActivity(), users, scores);
        userListView.setAdapter(userAdapter);
        //instance of the global variables
        final GlobalVar globalVariables = (GlobalVar) getActivity().getApplicationContext();
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), UserDetails.class);
                showDetailActivity.putExtra("com.example.gustavojimenez.USER_INDEX",i);
                startActivity(showDetailActivity);
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
        //Log.e(TAG, "uesrid is :"+ user.getUid());
        dref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        dref.addListenerForSingleValueEvent(sethousecode);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getActivity().getApplicationContext(), UserDetails.class);
                showDetailActivity.putExtra("com.example.gustavojimenez.USER_INDEX",i);
                startActivity(showDetailActivity);
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.actionBarSettings)
            Toast.makeText(getActivity(),"works",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}

