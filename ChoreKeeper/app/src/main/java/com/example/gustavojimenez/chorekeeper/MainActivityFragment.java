package com.example.gustavojimenez.chorekeeper;


import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.gustavojimenez.chorekeeper.R;
import com.example.gustavojimenez.fragments.AllChoresFragment;
import com.example.gustavojimenez.fragments.HomeFragment;
import com.example.gustavojimenez.fragments.MyChoresFragment;
import com.example.gustavojimenez.fragments.RewardsFragment;

public class MainActivityFragment extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private RewardsFragment rewardsFragment;
    private AllChoresFragment allChoresFragment;
    private MyChoresFragment myChoresFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        mMainFrame = (FrameLayout) findViewById(R.id.fragment_container);
        mMainNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        rewardsFragment = new RewardsFragment();
        allChoresFragment = new AllChoresFragment();
        myChoresFragment = new MyChoresFragment();
        setFragment(homeFragment);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.nav_home :
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_chores :
                        setFragment(allChoresFragment);
                        return true;
                    case R.id.nav_myChores :
                        setFragment(myChoresFragment);
                        return true;
                    case R.id.nav_rewards :
                        setFragment(rewardsFragment);
                        return true;
                        default:
                            return false;
                }
            }




        });
    }
    private void setFragment(Fragment homeFragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,homeFragment);
        fragmentTransaction.commit();
    }
}
