package com.example.gustavojimenez.chorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Register extends AppCompatActivity {
    RadioButton joinHouse;
    RadioGroup selectHouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        joinHouse = findViewById(R.id.joinHouse);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
