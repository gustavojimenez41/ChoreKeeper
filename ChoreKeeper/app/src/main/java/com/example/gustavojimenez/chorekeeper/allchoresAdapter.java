package com.example.gustavojimenez.chorekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class allchoresAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] all_chores;
    String[] all_chores_scores;
    String[] all_chores_scores_descriptions;

    public allchoresAdapter(Context c, String[] i, String[] p, String[] d){
        all_chores = i;
        all_chores_scores = p;
        all_chores_scores_descriptions = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return all_chores.length;
    }

    @Override
    public Object getItem(int i) {
        return all_chores[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.all_chores_listview_detail, null);
        TextView allchoreTextView = (TextView) v.findViewById(R.id.chorestextView);
        TextView desciptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
        TextView scoresTextview = (TextView) v.findViewById(R.id.scoreTextView);

        String chore = all_chores[i];
        String desc = all_chores_scores_descriptions[i];
        String points = all_chores_scores[i];

        allchoreTextView.setText(chore);
        desciptionTextView.setText(desc);
        scoresTextview.setText(points);
        return v;
    }
}
