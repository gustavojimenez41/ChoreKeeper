package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateReward extends AppCompatActivity {
    Button saveButton, cancelButton;
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
}

