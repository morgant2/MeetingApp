package com.meetingapp.BusinessObjects;

public class Location
{
    private String Address;
    private String City;
    private String State;
    private String ZipCode;

    public Location(String address, String city, String state, String zipCode)
    {
        this.Address = address;
        this.City = city;
        this.State = state;
        this.ZipCode = zipCode;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getZipCode() {
        return ZipCode;
    }
}
