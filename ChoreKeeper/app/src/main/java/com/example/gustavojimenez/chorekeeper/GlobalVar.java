package com.example.gustavojimenez.chorekeeper;
import android.app.Application;
import android.content.Context;

import com.example.gustavojimenez.chorekeeper.database;

import java.util.List;

public class GlobalVar extends Application
{
    private String housecode = null;
    private String userid = null;
    private List<Chore> chores;

    public String gethousecode()
    {

        return housecode;
    }

    //is set at login time
    public String getUserid()
    {
        return userid;
    }

    public List<Chore> getChores()
    {
        return chores;
    }





    public void setHousecode(String code)
    {
        housecode = code;
    }
    public void setUserid(String id)
    {
        userid = id;
    }
    public void addHouseChore(Chore c)
    {
        chores.add(c);
    }







    private static Context sContext;

    //needed to use this class in Database
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }
}
