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

import com.example.gustavojimenez.chorekeeper.CreateChore;
import com.example.gustavojimenez.chorekeeper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
