package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AssignChore extends AppCompatActivity{

    TextView textElement,pointelement,commentelement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chore);

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.users, android.R.layout.simple_spinner_item);
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






