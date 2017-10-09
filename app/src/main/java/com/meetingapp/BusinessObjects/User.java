package com.meetingapp.BusinessObjects;

public class User
{
    public String FirstName;
    public String LastName;
    public String Username;
    public Location UserHomeLocation;

    public User(String firstName, String lastName, String username, Location location)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Username = username;
        this.UserHomeLocation = location;
    }
}
