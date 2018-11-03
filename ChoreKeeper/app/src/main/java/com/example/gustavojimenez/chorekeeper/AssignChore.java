package com.example.gustavojimenez.chorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.ArrayAdapter;

public class AssignChore extends AppCompatActivity {

    TextView textElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chore);

        textElement = (TextView) findViewById(R.id.chore_name);

        //NewText is where we need to pass in the name of the chore.
        String newText;
        newText = "This is where the chore name goes";
        textElement.setText(newText);








    }
}
