package com.example.gustavojimenez.chorekeeper;

//used only to store the user house code and list of chores
//firebase authentication system takes care of everything else
//connected to the firebase system through the userID, assigned by firebase

public class User {

    private String userID;
    private String houseCode;

    //array of chores assigned to the user identified by an ID
    private int[] chores;
    private int points = 0;


    User(){};

    User(String ID, String code)
    {
        this.userID = ID;
        this.houseCode = code;
    }

    public String getID()
    {
        return userID;
    }


}
