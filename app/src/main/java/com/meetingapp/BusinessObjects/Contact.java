package com.meetingapp.BusinessObjects;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Contact
{
    private String FirstName;
    private String LastName;
    private String Email;
    private Location UserHomeLocation;

    public Contact(String firstName, String lastName, String email, Location location)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.UserHomeLocation = location;
    }

    public String getFirstName(){
        return this.FirstName;
    }

    public String getLastName()
    {
        return this.LastName;
    }
    public String getEmail()
    {
        return this.Email;
    }

    public Location getUserHomeLocation() {
        return UserHomeLocation;
    }

    public String getLastFirstName()
    {
        return LastName + ", " + FirstName;
    }

}
