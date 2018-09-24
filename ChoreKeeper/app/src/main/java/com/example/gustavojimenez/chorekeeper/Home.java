package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Home extends AppCompatActivity {

    ListView userListView;
    String[] users;
    String[] scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources res = getResources();
        userListView = (ListView) findViewById(R.id.userListView);
        users = res.getStringArray(R.array.users);
        scores = res.getStringArray(R.array.scores);

        userAdapter userAdapter = new userAdapter(this, users, scores);
        userListView.setAdapter(userAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), UserDetails.class);
                showDetailActivity.putExtra("com.example.gustavojimenez.USER_INDEX",i);
                startActivity(showDetailActivity);
            }
        });








    }

    public void goToSettings(View view) {
        Intent intent = new Intent(Home.this, SettingsActivity.class);
        startActivity(intent);
    }
}
