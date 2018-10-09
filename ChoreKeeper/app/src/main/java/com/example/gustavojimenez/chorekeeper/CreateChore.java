package com.example.gustavojimenez.chorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Boolean.TRUE;

public class CreateChore extends AppCompatActivity {


    private DatabaseReference dbref;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chore);
    }




    public void createChore(String name, int points, String comments)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uname = user.getDisplayName();

        String ID = Integer.toString(Math.abs(name.hashCode()));

        Chore c = new Chore(name, comments, points, ID);


        //adds the chore to the list of chores
        dbref = db.getReference("Chores");
        dbref.child(c.getID()).setValue(c);

        //adds the chore to the house
        //needs to reference the house code which is retrieved through the user
        //so we need to read from the database, hence the listener
        ValueEventListener addToHouse = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //should be the house code
                String hCode  = (String)dataSnapshot.getValue();
                dbref = db.getReference("Houses/"+hCode+"Chores");
                dbref.child(c.getID()).setValue(TRUE);
            }

            @Override
            public void onCancelled(DatabaseError Error)
            {
                Toast.makeText(CreateChore.this, "Add Chore failed",
                        Toast.LENGTH_LONG).show();

            }
        };


        dbref = db.getReference("Users/"+user.getUid()+"/houseCode");
        dbref.addListenerForSingleValueEvent(addToHouse);

    }


}
