package com.example.gustavojimenez.chorekeeper;

//used only to store the user house code and list of chores
//firebase authentication system takes care of everything else
//connected to the firebase system through the userID, assigned by firebase

public class User {

    private String userID;
    private String houseCode;
    private int points = 0;



    User(){};

    User(String ID, String code, int p)
    {
        this.userID = ID;
        this.houseCode = code;
        this.points = p;
    }

    User(String ID, String code)
    {
        this.userID = ID;
        this.houseCode = code;
    }

    //must have a getter for every attribute that we want to save in the database
    public String getID()
    {
        return userID;
    }
    public int getPoints() {return points;}
    public String getHouseCode() { return houseCode;}



}
