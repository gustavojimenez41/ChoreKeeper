package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyChores extends AppCompatActivity {
    Button allChores, rewards, home,CreateChore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chores);
        home = findViewById(R.id.house_button);
        allChores = findViewById(R.id.allChores);
        rewards = findViewById(R.id.rewards);
        CreateChore = findViewById(R.id.createChore);

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyChores.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        allChores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyChores.this, AllChores.class);
                startActivity(intent);
                finish();
            }
        });
        rewards.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyChores.this, Rewards.class);
                startActivity(intent);
                finish();
            }
        });
        CreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyChores.this, CreateChore.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
