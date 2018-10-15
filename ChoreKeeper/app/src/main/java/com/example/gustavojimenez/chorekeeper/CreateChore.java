package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    Button createChore;
   // String chorename = ((EditText)findViewById(R.id.chorename)).getText().toString();
   // float point_value = ((EditText)findViewById(R.id.points)).getAlpha();
    //String description = ((EditText)findViewById(R.id.description)).getText().toString();

    private DatabaseReference dbref;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chore);

        createChore = findViewById(R.id.createChoreButton);

        createChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = ((EditText)findViewById(R.id.chorename)).getText().toString();
                int points = ((EditText)findViewById(R.id.points)).getInputType();
                String comments = ((EditText)findViewById(R.id.description)).getText().toString();

                createChore(name, points, comments);

                Intent intent = new Intent(CreateChore.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
    }




    public void createChore(final String name, final int points, final String comments)
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uname = user.getDisplayName();
        final String ID = Integer.toString(Math.abs(name.hashCode()));






        //needs to reference the house code which is retrieved through the user
        //so we need to read from the database, hence the listener
        ValueEventListener addToHouse = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //should be the house code
                String housecode  = dataSnapshot.getValue(String.class);
                //create the new chore
                final Chore c = new Chore(name, comments, points, ID, housecode);

                //adds the chore to the list of chores
                dbref = db.getReference("Chores");
                dbref.child(c.getID()).setValue(c);

                //adds the chore to the house
                dbref = db.getReference("Houses/"+housecode+"/Chores");
                dbref.child(c.getID()).setValue(TRUE);


                Toast.makeText(CreateChore.this, "Chore added",
                        Toast.LENGTH_LONG).show();

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
