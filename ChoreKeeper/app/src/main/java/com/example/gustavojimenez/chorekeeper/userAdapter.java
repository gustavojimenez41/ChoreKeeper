package com.example.gustavojimenez.chorekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class userAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] users;
    String[] scores;

    public userAdapter(Context c, String[] i,String[] p){
        users = i;
        scores = p;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.length;
    }

    @Override
    public Object getItem(int i) {
        return users[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.user_listview_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView scoreTextView = (TextView) v.findViewById(R.id.scoreTextView);

        String name = users[i];
        String score = scores[i];

        nameTextView.setText(name);
        scoreTextView.setText(score);

        return v;
    }
}
