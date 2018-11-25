package com.example.gustavojimenez.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gustavojimenez.chorekeeper.Chore;
import com.example.gustavojimenez.chorekeeper.CreateChore;
import com.example.gustavojimenez.chorekeeper.GlobalVar;
import com.example.gustavojimenez.chorekeeper.R;
import com.example.gustavojimenez.chorekeeper.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChoresFragment extends Fragment {

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_chores, container, false);
        CreateChore = view.findViewById(R.id.createChore);
        CreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), CreateChore.class);
                startActivity(intent);
            }
        });


        final GlobalVar globalVariables = (GlobalVar) getActivity().getApplicationContext();
        List<Chore> chores = globalVariables.getChores();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        int i;
        if(chores!=null)
        {
            for(i=0;i<chores.size();i++)
            {
                if(chores.get(i).getOwner().equals(userid));
                {
                    //add to the list for viewing
                    //this chore belong to the current user
                }
            }

        }




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


}
