package com.meetingapp.businessObjects;


import android.app.Application;
import android.location.Geocoder;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    //Get the longitude and latitude for the address
   /* public void convertAddressToGeoLocation() {
        if(this.Address == null || this.City == null || this.State == null || this.ZipCode == null) {
            //TODO: handle if any part of the address is empty
        } else {
            try {
               Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                addresses = geocoder.getFromLocationName(this.getFullAddress(), 1);
                while(addresses.size() == 0) {
                    addresses = geocoder.getFromLocationName(this.getFullAddress(), 1);
                }
                if(addresses != null && addresses.size() > 0) {
                    double lat = addresses.get(0).getLatitude();
                    double lon = addresses.get(0).getLongitude();

                    LatLng coords = new LatLng(lat,lon);
                    this.setCoordinates(coords);

                }
            } catch(IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //Get the address for the given longitude and latitude
    public void convertGeoLocationToAddress() {
        if(this.Coordinates == null) {
            //TODO: handle if coordinates are empty
        } else {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                addresses = geocoder.getFromLocation(this.Coordinates.latitude, this.Coordinates.longitude, 1);

                if(addresses != null && addresses.size() > 0) {
                    this.Address = addresses.get(0).getAddressLine(0);
                    this.City = addresses.get(0).getLocality();
                    this.State = addresses.get(0).getAdminArea();
                    this.ZipCode = addresses.get(0).getPostalCode();
                }
            }catch(IOException ioe) {
                ioe.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/
}
