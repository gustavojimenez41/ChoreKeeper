package com.example.gustavojimenez.chorekeeper;

public class Chore
{

    private String name;
    private String comments;
    private int pointValue;
    private String ID;
    //hold the autID of the user who is assigned this chore
    private String owner = "none";


    Chore(String name, String comments, int points, String ID)
    {
        this.name = name;
        this.comments = comments;
        this.pointValue = points;
        this.ID = ID;
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

    public void setOwner(String ID)
    {
        owner = ID;
    }
}
