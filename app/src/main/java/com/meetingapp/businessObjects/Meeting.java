package com.meetingapp.businessObjects;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Meeting
{
    private String Subject;
    private String Description;
    private Date StartTime;
    private Date EndTime;
    private Location MeetingLocation;
    private ArrayList<Contact> contactsAttending;
    private LatLng CenterLocation;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public Location getMeetingLocation() {
        return MeetingLocation;
    }

    public void setMeetingLocation(Location meetingLocation) {
        MeetingLocation = meetingLocation;
    }

    public ArrayList<Contact> getContactsAttending() {
        return contactsAttending;
    }

    public void setContactsAttending(ArrayList<Contact> contactsAttending) {
        this.contactsAttending = contactsAttending;
    }

    //calculation based off of https://stackoverflow.com/questions/6671183/calculate-the-center-point-of-multiple-latitude-longitude-coordinate-pairs
    private void setCenterLocation()
    {
        double x = 0;
        double y = 0;
        double z = 0;
        int total = 0;

        for(Contact contact : this.contactsAttending)
        {
            double latitude = contact.getUserHomeLocation().getCoordinates().latitude * Math.PI / 180;
            double longitude = contact.getUserHomeLocation().getCoordinates().longitude * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
            total++;
        }

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        this.CenterLocation = new LatLng(centralLatitude * 180 / Math.PI, centralLongitude * 180 / Math.PI);
    }

    public LatLng getCenterLocation()
    {
        if (this.CenterLocation == null) setCenterLocation();
        return CenterLocation;
    }


    public void save(SharedPreferences sharedPreferences, String key) throws JSONException {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String allMeetingsJson = sharedPreferences.getString(key, "{'meetings':[]}");

        JSONObject jsonObject = new JSONObject(allMeetingsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("meetings");
        JSONObject newMeetingJSONObject = new JSONObject();

        Gson meetingGson = new Gson();
        String jsonString = meetingGson.toJson(this, Meeting.class);

        jsonArray.put(jsonString);
        newMeetingJSONObject.put("meetings", jsonArray);

        editor.putString(key, newMeetingJSONObject.toString());
        editor.apply();
    }

    @Override
    public String toString()
    {
        String msg = "";
        msg += "The meeting about " + this.Subject + " goes from " + this.StartTime + " to " + this.EndTime + ". ";
        msg += "It is located at " + this.getMeetingLocation() + ". ";
        msg += "\nAttendees: \n" + this.getStringOfContactsAttending();
        return msg;
    }

    private String getStringOfContactsAttending() {
        String msg = "";

        for(int i = 0; i < contactsAttending.size(); i++)
        {
            msg += contactsAttending.get(i).getLastFirstName() + ((i < contactsAttending.size() - 1) ? ", \n" : "");
        }

        return msg;
    }
}

