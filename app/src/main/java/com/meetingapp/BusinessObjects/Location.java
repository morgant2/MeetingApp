package com.meetingapp.BusinessObjects;

public class Location
{
    public String Address;
    public String City;
    public String State;
    public String ZipCode;

    public Location(String address, String city, String state, String zipCode)
    {
        this.Address = address;
        this.City = city;
        this.State = state;
        this.ZipCode = zipCode;
    }

}
