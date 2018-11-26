package com.example.gustavojimenez.chorekeeper;
import android.app.Application;
import android.content.Context;

import com.example.gustavojimenez.chorekeeper.database;

import java.util.ArrayList;
import java.util.List;

public class GlobalVar extends Application
{
    private String housecode = null;
    private String userid = null;
    private List<Chore> chores;
    private List<User> users;

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
    public List<User> getUsers()
    {
        return users;
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
        if(chores==null)
        {
            chores = new ArrayList<Chore>();
        }
        chores.add(c);
    }
    public void addHouseUser(User u)
    {
        if(users==null)
        {
            users = new ArrayList<User>();
        }
        if(!users.contains(u))
        {
            users.add(u);
        }

    }

    public void removeChore(String id)
    {
        if(chores==null)
            return;

        for(int i=0;i<chores.size();i++)
        {
            if(chores.get(i).getID().equals(id))
            {
                chores.remove(i);
                return;
            }
        }
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
