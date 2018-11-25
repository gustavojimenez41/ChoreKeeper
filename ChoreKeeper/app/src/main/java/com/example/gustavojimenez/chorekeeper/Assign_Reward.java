package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Assign_Reward extends AppCompatActivity {

    private static final String TAG = "AssignRewards:";

    TextView textElement,pointelement,commentelement;

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




        //NewText is where we need to pass in the name of the chore.

        String newText;
        String point, comment;
        newText = receivedTent.getStringExtra("name");
        point = receivedTent.getStringExtra("points");
        comment = receivedTent.getStringExtra("comment");
        textElement.setText(newText);
        pointelement.setText(point);
        commentelement.setText(comment);


        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    //assigns the chore to a user
    public void updateChoreOwner(String choreid, String ownerid) {

        DatabaseReference dref;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference("Users/"+ownerid+"/Chores");

        //adds the chore to the user list
        dref.child(choreid).setValue(true);
        //comment
        //add the owner to the chore
        dref = FirebaseDatabase.getInstance().getReference("Chores/"+choreid);
        dref.child(ownerid).setValue(ownerid);
    }


}
