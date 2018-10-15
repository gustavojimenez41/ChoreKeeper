package com.example.gustavojimenez.chorekeeper;

public class Chore
{

    private String name;
    private String comments;
    private int pointValue;
    private String ID;
    private String housecode;
    //hold the autID of the user who is assigned this chore
    private String owner = "none";

    Chore(){};

    Chore(String name, String comments, int points, String ID, String housecode)
    {
        this.name = name;
        this.comments = comments;
        this.pointValue = points;
        this.ID = ID;
        this.housecode = housecode;
    }

    public int getPoints()
    {
        return pointValue;
    }
    public String getName()
    {
        return name;
    }
    public String getID()
    {
        return ID;
    }
    public String getComments() { return comments; }
    public String getOwner() {return owner;}
    public String getHousecode() {return housecode;}

    public void setOwner(String ID)
    {
        owner = ID;
    }
}
