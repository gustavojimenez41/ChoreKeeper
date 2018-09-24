package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AllChores extends AppCompatActivity {
    Button myChores, rewards, home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chores);
        home = findViewById(R.id.house_button);
        rewards = findViewById(R.id.rewards);
        myChores = findViewById(R.id.myChores);

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
    }
}
