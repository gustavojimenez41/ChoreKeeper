package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class AllChores extends AppCompatActivity {
    Button myChores, rewards, home,CreateChore;
    ListView allChoresListView;
    String[] all_chores;
    String[] all_chores_scores;
    String[] all_chores_scores_descriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chores);

        //Creating the listview to hold all the chores
        Resources res = getResources();
        allChoresListView = (ListView) findViewById(R.id.allChoresListView);
        all_chores = res.getStringArray(R.array.all_chores);
        all_chores_scores = res.getStringArray(R.array.all_chores_scores);
        all_chores_scores_descriptions = res.getStringArray(R.array.all_chores_scores_desciptions);

        allchoresAdapter all_choresAdapter= new allchoresAdapter(this, all_chores, all_chores_scores, all_chores_scores_descriptions);
        allChoresListView.setAdapter(all_choresAdapter);





        home = findViewById(R.id.house_button);
        rewards = findViewById(R.id.rewards);
        myChores = findViewById(R.id.myChores);
        CreateChore = findViewById(R.id.createChore);

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        rewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });
        CreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AllChores.this, CreateChore.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
