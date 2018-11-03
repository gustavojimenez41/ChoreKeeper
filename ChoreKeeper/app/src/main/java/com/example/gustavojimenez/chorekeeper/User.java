package com.example.gustavojimenez.chorekeeper;

//used only to store the user house code and list of chores
//firebase authentication system takes care of everything else
//connected to the firebase system through the userID, assigned by firebase

public class User {

    private String userID;
    private String houseCode;

    //array of chores assigned to the user identified by an ID
    //private int[] chores; pretty sure this isn't necessary, Ill know when I implement addChore
    private int points = 0;
    //private String authID;


    User(){};

    User(String ID, String code)
    {
        this.userID = ID;
        this.houseCode = code;
        //this.authID = authID;
    }

    //must have a getter for every attribute that we want to save in the database
    public String getID()
    {
        return userID;
    }
   // public String getAuthID() { return authID; }
    public int getPoints() {return points;}
    public String getHouseCode() { return houseCode;}
}
