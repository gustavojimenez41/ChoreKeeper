package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Register extends AppCompatActivity {
    RadioButton joinHouse, createHouse;
    EditText joinHouseID;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.registerButton);
        joinHouse = findViewById(R.id.joinHouse);
        joinHouseID = findViewById(R.id.enterHouseID);
        createHouse = findViewById(R.id.createHouse);
        joinHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                joinHouseID.setVisibility(View.VISIBLE);
            }
        });
        createHouse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View view)
            {
                joinHouseID.setVisibility(View.GONE);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Register.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
