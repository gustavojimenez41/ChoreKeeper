package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gustavojimenez.fragments.MyChoresFragment;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignChore extends AppCompatActivity{

    private static final String TAG = "AssignChore:";

    TextView textElement,pointelement,commentelement;
    Button claim, complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chore);
        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
        List<User> users =globalVariables.getUsers();
        Intent receivedTent = getIntent();
        textElement = (TextView) findViewById(R.id.chore_name);
        pointelement = (TextView) findViewById(R.id.points);
        commentelement = (TextView)findViewById(R.id.comment);
        claim = (Button)findViewById(R.id.claimChoreButton);
        complete = (Button)findViewById(R.id.completeChore);




        //NewText is where we need to pass in the name of the chore.
        String newText;
        String point, comment, id;
        newText = receivedTent.getStringExtra("name");
        point = receivedTent.getStringExtra("points");
        comment = receivedTent.getStringExtra("comment");
        id = receivedTent.getStringExtra("id");

        textElement.setText(newText);
        pointelement.setText("Points Worth: " + point);
        commentelement.setText(comment);



//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        claim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateChoreOwner(id, user.getUid());
                Intent intent = new Intent(AssignChore.this, MainActivityFragment.class);
                startActivity(intent);
                finish();


            }
        });

        complete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                completeChore(point, id);
                Intent intent = new Intent(AssignChore.this, MainActivityFragment.class);
                startActivity(intent);
                finish();


            }
        });


    }

    //assigns the chore to a user
    public void updateChoreOwner(String choreid, String ownerid)
    {


        DatabaseReference dref;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference("Users/"+ownerid+"/Chores");

        //adds the chore to the user list
        dref.child(choreid).setValue(true);
        //comment
        //add the owner to the chore
        dref = FirebaseDatabase.getInstance().getReference("Chores/"+choreid);
        dref.child("owner").setValue(ownerid);
    }

    public void completeChore(String points, String choreid)
    {
        int cpoints = Integer.parseInt(points);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //add the point value of the chore to the users points
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                int upoint = dataSnapshot.child("points").getValue(int.class);

                //adding the chore points to the user
                Log.e(TAG, "points "+cpoints);
                upoint += cpoints;
                userref.child("points").setValue(upoint);
                Toast.makeText(AssignChore.this, "Points now available: "+upoint,
                        Toast.LENGTH_LONG).show();

                //delete from chores
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chores/"+choreid);
                ref.removeValue();

                //delete from user
                ref = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/"+"Chores/"+choreid);
                ref.removeValue();

                //remove from global variable
                final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
                globalVariables.removeChore(choreid);

                Intent intent = new Intent(AssignChore.this, MainActivityFragment.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}






