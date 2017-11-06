package com.meetingapp.BusinessObjects;

public class Contact
{
    public String FirstName;
    public String LastName;
    public String Email;
    public Location UserHomeLocation;

    public Contact(String firstName, String lastName, String email, Location location)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.UserHomeLocation = location;
    }
}
