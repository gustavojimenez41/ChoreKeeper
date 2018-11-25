package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavojimenez.fragments.RewardsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Assign_Reward extends AppCompatActivity {

    private static final String TAG = "AssignRewards:";
    DatabaseReference dref;

    TextView textElement,pointelement,commentelement;
    Button claim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign__reward);
        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
        List<User> users =globalVariables.getUsers();
        Intent receivedTent = getIntent();
        textElement = (TextView) findViewById(R.id.chore_name);
        pointelement = (TextView) findViewById(R.id.points);
        commentelement = (TextView)findViewById(R.id.comment);
        claim = (Button)findViewById(R.id.claimRewardButton);




        //NewText is where we need to pass in the name of the chore.

        String newText;

        String point, comment, id;
        newText = receivedTent.getStringExtra("name");
        point = receivedTent.getStringExtra("points");
        comment = receivedTent.getStringExtra("comment");
        id = receivedTent.getStringExtra("id");

        textElement.setText(newText);
        pointelement.setText(point);
        commentelement.setText(comment);


        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        claim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                redeemReward(id, point);


            }
        });


    }

    void redeemReward(String rewardId, String rpointsInt)
    {

        int rpoints = Integer.parseInt(rpointsInt);
        //deciding now to leave the reward in the database because of time constraints
        //remove point value from the user
        //when the chore is completed, remove the chore from the database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //add the point value of the chore to the users points
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
                    Toast.makeText(Assign_Reward.this, "Reward redeemed, points remaining: "+upoint,
                            Toast.LENGTH_LONG).show();

                    //deletes the reward from the database
                    DatabaseReference rref = FirebaseDatabase.getInstance().getReference("Rewards/"+rewardId);
                    rref.removeValue();

                    Intent intent = new Intent(Assign_Reward.this, MainActivityFragment.class);
                    startActivity(intent);
                    finish();

                }
                else
                {

                    Toast.makeText(Assign_Reward.this, "Insufficient points",
                            Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }




}
