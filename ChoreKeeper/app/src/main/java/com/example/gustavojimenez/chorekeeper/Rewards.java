package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rewards extends AppCompatActivity {
    Button allChores, myChores, home, createRewards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        home = findViewById(R.id.house_button);
        allChores = findViewById(R.id.allChores);
        myChores = findViewById(R.id.myChores);
        createRewards = findViewById(R.id.createRewards);
        createRewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, CreateReward.class);
                startActivity(intent);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
        myChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Rewards.this, MyChores.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

