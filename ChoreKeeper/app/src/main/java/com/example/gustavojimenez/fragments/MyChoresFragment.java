package com.example.gustavojimenez.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gustavojimenez.chorekeeper.AssignChore;
import com.example.gustavojimenez.chorekeeper.Chore;
import com.example.gustavojimenez.chorekeeper.CreateChore;
import com.example.gustavojimenez.chorekeeper.GlobalVar;
import com.example.gustavojimenez.chorekeeper.R;
import com.example.gustavojimenez.chorekeeper.User;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChoresFragment extends Fragment {

    ArrayList<String> chore_arr = new ArrayList<String>();
    ArrayList<String> points_arr = new ArrayList<String>();
    ArrayList<String> descript_arr = new ArrayList<String>();
    ArrayList<String> id_arr = new ArrayList<String>();

    ListView listview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    Button allChores, rewards, home,CreateChore;

    DatabaseReference dref;
    FirebaseAuth firebaseauth;
    private static final String TAG = "MyChores:";
    public MyChoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent editIntent = new Intent(getContext(), AssignChore.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_chores, container, false);
        CreateChore = view.findViewById(R.id.createChore);

        listview = view.findViewById(R.id.myChoresListView);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);

        CreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), CreateChore.class);
                startActivity(intent);
            }
        });


        ValueEventListener sethousecode = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String housecode = dataSnapshot.getValue(String.class);
                //Log.e(TAG, housecode);

                dref = FirebaseDatabase.getInstance().getReference("Chores");
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //the snapshot is the chore object
                        String chorehousecode = dataSnapshot.child("housecode").getValue(String.class);

                        //Log.e(TAG, "checking housecode: " + chorehousecode);

                        if(chorehousecode != null && chorehousecode.equals(housecode))
                        {


                            //the datasnapshot is the child that was added, a chore object, the key of which is the chore Id
                            String owner = dataSnapshot.child("owner").getValue(String.class);
                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if(userid.equals(owner))
                            {
                                String id = dataSnapshot.getKey();

                                //retrieve all the attributes
                                String name = (String) dataSnapshot.child("name").getValue();
                                String comments = (String) dataSnapshot.child("comments").getValue();
                                long points = (long)dataSnapshot.child("points").getValue();
                                String stringPoints = Long.toString(points);



                                //add the Id to the list
                                if(!containsUsername(list, "\n"+name+ "\n"+ stringPoints +"pts"+"\n"+comments+"\n"))
                                {
                                    list.add("\n"+name+ "\n"+ stringPoints +"pts"+"\n"+comments+"\n");
                                }

                                chore_arr.add(name);
                                points_arr.add(stringPoints);
                                descript_arr.add(comments);
                                id_arr.add(id);




                                adapter.notifyDataSetChanged();

                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        String name = chore_arr.get(i);
                                        String points2 = points_arr.get(i);
                                        String comment = descript_arr.get(i);
                                        String id = id_arr.get(i);
                                        editIntent.putExtra("name",name);
                                        editIntent.putExtra("points", points2);
                                        editIntent.putExtra("comment",comment);
                                        editIntent.putExtra("id", id);
                                        startActivity(editIntent);
                                    }
                                });
                            }


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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/houseCode");
        dref.addListenerForSingleValueEvent(sethousecode);





        return view;
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
                Toast.makeText(getActivity(), "Complete Chore failed",
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


