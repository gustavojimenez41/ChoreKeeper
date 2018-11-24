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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gustavojimenez.chorekeeper.User;
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


import java.util.ArrayList;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TextView H_code;
    ArrayList<String> users = new ArrayList<String>();

    DatabaseReference dref;
    String housecode = null;
    int i = 0;
    private static final String TAG = "Home:";

    ListView listview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

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
        H_code = (TextView) view.findViewById(R.id.H_code);
        listview = view.findViewById(R.id.fragmentMyHomeListView);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        //instance of the global variables
        final GlobalVar globalVariables = (GlobalVar) getActivity().getApplicationContext();

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

                H_code.setText("HouseCode:"+housecode);

/*
                ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
                for (ExportedUserRecord user : page.iterateAll()) {
                    System.out.println("User: " + user.getUid());
                }
                */





                //once added, is triggered for every child, then once every time a new child is added
                //then again every time a new child is added.
                dref = FirebaseDatabase.getInstance().getReference("Users");
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {


                        String userhousecode = dataSnapshot.child("houseCode").getValue(String.class);
                        Log.e(TAG, "checking  user houseCode: " + userhousecode);

                        if(userhousecode != null && userhousecode.equals(housecode)) {

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();

                            //retrieve all the attributes

                            String userid = dataSnapshot.getKey();
                            long points = (long) dataSnapshot.child("points").getValue();
                            String username = user.getDisplayName();

                            if(!containsUsername(list, username))
                            {
                                list.add(username);
                            }



                            User newuser = new User(userid,housecode,(int)points);


                            //adds the list of users to the global variable so that the list can be used
                            //elsewhere

                            final GlobalVar globalVariables = (GlobalVar) getActivity().getApplicationContext();
                            List<User> users = globalVariables.getUsers();

                            //tests if the list already contains that user
                            if(globalVariables.getUsers()== null || !containsUser(users,newuser))
                            {
                                globalVariables.addHouseUser(newuser);
                            }



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



    //returns true if the user list contains that user
    //returns false if it does not
    public boolean containsUser(List<User> list, User u)
    {
        int i = 0;
        while(i<list.size())
        {
            if(list.get(i).getID().equals(u.getID()))
            {
                return true;
            }
            i++;
        }

        return false;

    }

    public boolean containsUsername(List<String> list,String name)
    {
        int i = 0;
        while(i<list.size())
        {
            if(list.get(i).equals(name))
            {
                return true;
            }
            i++;
        }

        return false;
    }



}

