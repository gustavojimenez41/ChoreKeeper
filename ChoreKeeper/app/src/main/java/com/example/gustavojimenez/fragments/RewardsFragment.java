package com.example.gustavojimenez.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gustavojimenez.chorekeeper.AssignChore;
import com.example.gustavojimenez.chorekeeper.Assign_Reward;
import com.example.gustavojimenez.chorekeeper.Chore;
import com.example.gustavojimenez.chorekeeper.CreateReward;
import com.example.gustavojimenez.chorekeeper.GlobalVar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.gustavojimenez.chorekeeper.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {

    Button allChores, myChores, home, createRewards;
    String housecode;
    ArrayList<String> rewards_arr = new ArrayList<String>();
    ArrayList<String> points_arr = new ArrayList<String>();
    ArrayList<String> description_arr = new ArrayList<String>();

    DatabaseReference dref;

    private static final String TAG = "Rewards:";
    ListView listview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    public RewardsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        Intent editIntent = new Intent(getContext(), Assign_Reward.class);
        createRewards = view.findViewById(R.id.fragmentCreateRewards);
        listview = view.findViewById(R.id.rewardsListView);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);
        createRewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), CreateReward.class);
                startActivity(intent);
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
                            Chore newchore = dataSnapshot.getValue(Chore.class);

                            final GlobalVar globalVariables = (GlobalVar) getActivity().getApplicationContext();
                            if(globalVariables.getChores()!= null && !globalVariables.getChores().contains(newchore))
                            {
                                globalVariables.addHouseChore(newchore);
                            }
                            String rewardID = dataSnapshot.getKey();

                            //retrieve all the attributes
                            String name = (String) dataSnapshot.child("name").getValue();
                            String comments = (String) dataSnapshot.child("comments").getValue();
                            long points = (long)dataSnapshot.child("points").getValue();
                            String stringPoints = Long.toString(points);


                            //this is where you do whatever you need to do with the data
                           //list.add("\n"+name);
                            if(!containsUsername(list,"\n"+name+ "\n"+ stringPoints +"pts"+"\n"+comments+"\n"))
                            {
                                list.add("\n"+name+ "\n"+ stringPoints +"pts"+"\n"+comments+"\n");

                            }
//

                            rewards_arr.add(name);
                            points_arr.add(stringPoints);
                            description_arr.add(comments);
                            adapter.notifyDataSetChanged();
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    String name = rewards_arr.get(i);
                                    String points2 = points_arr.get(i);
                                    String comment = description_arr.get(i);
                                    editIntent.putExtra("name",name);
                                    editIntent.putExtra("points", points2);
                                    editIntent.putExtra("comment",comment);
                                    startActivity(editIntent);
                                }
                            });

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
        // Inflate the layout for this fragment
        return view;
    }

    void redeemReward(String rewardId)
    {
        //deciding now to leave the reward in the database because of time constraints
        //remove point value from the user
        //when the chore is completed, remove the chore from the database
        dref = FirebaseDatabase.getInstance().getReference("Rewards/"+rewardId);

        //add the point value of the chore to the users points
        dref.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int rpoints = dataSnapshot.child("points").getValue(int.class);

                DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        int upoint = dataSnapshot.child("points").getValue(int.class);
                        if(upoint>=rpoints)
                        {
                            upoint -= rpoints;
                            userref.child("points").setValue(upoint);

                        }
                        else
                        {

                            Toast.makeText(getActivity(), "Insufficient points",
                                    Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError Error)
            {
                Toast.makeText(getActivity(), "Redeem Reward failed",
                        Toast.LENGTH_LONG).show();

            }
        });

    }
    public boolean containsUsername(List<String> list, String name)
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
