package com.example.gustavojimenez.chorekeeper;

//used only to store the user house code and list of chores
//firebase authentication system takes care of everything else
//connected to the firebase system through the userID, assigned by firebase

public class User {

    private String userID;
    private String houseCode;
    private int points = 0;
    private String uname;



    User(){};

    public User( String ID, String code, int p,String n)
    {
        this.userID = ID;
        this.houseCode = code;
        this.points = p;
        this.uname = n;
    }

    User(String ID, String code, String n)
    {
        this.userID = ID;
        this.houseCode = code;
        this.uname = n;

    }

    //must have a getter for every attribute that we want to save in the database
    public String getID()
    {
        return userID;
    }
    public int getPoints() {return points;}
    public String getHouseCode() { return houseCode;}
    public String getUname() {return uname;}



}
