package com.example.gustavojimenez.chorekeeper;

public class Reward
{
    private String name;
    private String comments;
    private int pointValue;
    private String ID;


    Reward(String name, String comments, int points, String ID)
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
}
