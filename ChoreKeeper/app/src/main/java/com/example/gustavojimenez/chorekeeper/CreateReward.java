package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CreateReward extends AppCompatActivity {
    Button saveButton, cancelButton;
    private DatabaseReference dbref;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    private static final String TAG = "Register:";


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


                String name = ((EditText)findViewById(R.id.rewardName)).getText().toString();
                int points = Integer.parseInt(((EditText)findViewById(R.id.reward)).getText().toString());
                String comments = ((EditText)findViewById(R.id.RewardComments)).getText().toString();

                //change this to generate a unique ID
                createReward(name, points, "1", comments);


                Intent intent = new Intent(CreateReward.this, Rewards.class);
                startActivity(intent);
                finish();
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

    public void createReward(String name, int points, String ID, String comments)
    {

        //will change this so that it creates the reward in the path of the house

        dbref = db.getReference("rewards");

        Reward r = new Reward(name, comments, points, ID);
        dbref = db.getReference("rewards");
        dbref.child(r.getID()).setValue(r);

    }
}

