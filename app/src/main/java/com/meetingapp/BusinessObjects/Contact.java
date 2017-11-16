package com.meetingapp.BusinessObjects;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.meetingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void save(SharedPreferences sharedPreferences, String key) throws JSONException {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String allContactsJson = sharedPreferences.getString(key, "{'contacts':[]}");

        if(allContactsJson == "") allContactsJson = "{'contacts':[]}";

        JSONObject jsonObject = new JSONObject(allContactsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
        JSONObject newContactJSONObject = new JSONObject();

        Gson meetingGson = new Gson();
        String jsonString = meetingGson.toJson(this, Contact.class);

        jsonArray.put(jsonString);
        newContactJSONObject.put("contacts", jsonArray);

        editor.putString(key, newContactJSONObject.toString());
        editor.apply();
    }
}
