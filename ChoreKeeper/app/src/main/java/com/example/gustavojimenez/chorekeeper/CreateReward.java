package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


public class CreateReward extends AppCompatActivity {
    Button saveButton, cancelButton;
    private DatabaseReference dbref;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    private static final String TAG = "CreateReward";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                int points;

                String name = ((EditText)findViewById(R.id.rewardName)).getText().toString();
                String pointsTemp = ((EditText)findViewById(R.id.reward)).getText().toString();
                if(pointsTemp.matches(""))
                {
                    points = 0;
                }
                else
                {
                    points = Integer.parseInt(pointsTemp);
                }
                String comments = ((EditText)findViewById(R.id.RewardComments)).getText().toString();


                //here we are checking to make sure user entered all information
                //else we do not create new user
                Boolean good = true;
                //note: TextUtils.isEmpty(String) returns true if string is null or 0-length
                if(TextUtils.isEmpty(name))
                {
                    ((EditText)findViewById(R.id.rewardName)).setError("Cannot be empty");
                    good = false;
                }
                if(TextUtils.isEmpty(comments))
                {
                    ((EditText)findViewById(R.id.RewardComments)).setError("Cannot be empty");
                    good = false;
                }
                if(good)
                {
                    createReward(name, points, comments);
                    Intent intent = new Intent(CreateReward.this, Rewards.class);
                    startActivity(intent);
                    finish();
                }







            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CreateReward.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createReward(final String name, final int points, final String comments)
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
                //create the new reward
                final Reward r = new Reward(name, comments, points, ID, housecode);

                //adds the reward to the list of chores
                dbref = db.getReference("Rewards");
                dbref.child(r.getID()).setValue(r);

                //adds the reward to the house
                dbref = db.getReference("Houses/"+housecode+"/Rewards");
                dbref.child(r.getID()).setValue(TRUE);


                Toast.makeText(CreateReward.this, "reward created",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError Error)
            {
                Toast.makeText(CreateReward.this, "create reward failed",
                        Toast.LENGTH_LONG).show();

            }
        };




        dbref = db.getReference("Users/"+user.getUid()+"/houseCode");
        dbref.addListenerForSingleValueEvent(addToHouse);

    }

}

