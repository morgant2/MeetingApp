package com.meetingapp.BusinessObjects;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.meetingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Meeting
{
    private String Subject;
    private String Description;
    private Date StartTime;
    private Date EndTime;
    private Location MeetingLocation;

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

    public Contact[] getContactsAttending() {
        return contactsAttending;
    }

    public void setContactsAttending(Contact[] contactsAttending) {
        this.contactsAttending = contactsAttending;
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

    private Contact[] contactsAttending;
}

