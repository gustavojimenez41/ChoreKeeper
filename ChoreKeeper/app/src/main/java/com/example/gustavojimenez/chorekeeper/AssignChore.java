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

public class AssignChore extends AppCompatActivity{

    TextView textElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chore);

        Intent receivedTent = getIntent();
        textElement = (TextView) findViewById(R.id.chore_name);


        //NewText is where we need to pass in the name of the chore.
        String newText;
        String point, comment;
        newText = receivedTent.getStringExtra("name");

        textElement.setText(newText);


        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }


}






