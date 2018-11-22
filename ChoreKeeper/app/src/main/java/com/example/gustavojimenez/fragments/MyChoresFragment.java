package com.example.gustavojimenez.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.gustavojimenez.chorekeeper.CreateChore;
import com.example.gustavojimenez.chorekeeper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChoresFragment extends Fragment {

    Button allChores, rewards, home,CreateChore;
    private static final String TAG = "MyChores:";
    public MyChoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_chores, container, false);
        CreateChore = view.findViewById(R.id.createChore);
        CreateChore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), CreateChore.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
