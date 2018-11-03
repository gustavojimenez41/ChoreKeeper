package com.example.gustavojimenez.chorekeeper;

import android.app.Application;

public class GlobalVar extends Application
{
    private String housecode;
    private String userid;

    public String gethousecode()
    {
        return housecode;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setHousecode(String code)
    {
        housecode = code;
    }
    public void setUserid(String id)
    {
        userid = id;
    }
}
