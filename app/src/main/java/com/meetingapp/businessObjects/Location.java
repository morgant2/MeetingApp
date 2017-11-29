package com.meetingapp.businessObjects;

import com.google.android.gms.maps.model.LatLng;

public class Location
{
    private String Address;
    private String City;
    private String State;
    private String ZipCode;

    public LatLng getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.Coordinates = coordinates;
    }

    private LatLng Coordinates;

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

    public String getFullAddress()
    {
        return this.Address + " " + this.City + ", " + this.State + " " + this.ZipCode;
    }
}
