package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class UserDetails extends AppCompatActivity {

    TextView textElement,pointelement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        final GlobalVar globalVariables = (GlobalVar) getApplicationContext();
        List<User> users =globalVariables.getUsers();
        Intent receivedTent = getIntent();
        textElement = (TextView) findViewById(R.id.chore_name);
        pointelement = (TextView) findViewById(R.id.points);




        //NewText is where we need to pass in the name of the chore.
        String newText;
        String point, comment;
        newText = receivedTent.getStringExtra("name");
        point = receivedTent.getStringExtra("points");
        textElement.setText(newText);
        pointelement.setText("Points: " + point);





    }
}
