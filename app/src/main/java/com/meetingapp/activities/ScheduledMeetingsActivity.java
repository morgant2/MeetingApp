package com.meetingapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.meetingapp.R;
import com.meetingapp.businessObjects.Meeting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduledMeetingsActivity extends BaseActivity {
    ArrayList<Meeting> meetingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
          We will not use setContentView in this activty
         Rather than we will use layout inflater to add view in FrameLayout of our base activity layout
         Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_scheduled_meetings, mFrameLayout);

        try {
            setMeetings();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        ListView list = (ListView) findViewById(R.id.listViewMeetings);
        list.setAdapter(new ArrayAdapter<Meeting>(this, android.R.layout.simple_list_item_1, meetingsList));

    }

    public void setMeetings() throws JSONException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().create();

        String meetingsJson = getSharedPreferences(getString(R.string.meetings_key), Context.MODE_PRIVATE).getString(getString(R.string.meetings_key), "{'meetings':[]");
        if (meetingsJson == "") meetingsJson = "{'meetings':[]}";
        JSONObject Jo = new JSONObject(meetingsJson);
        JSONArray jsonArray = Jo.getJSONArray("meetings");
        Type type = new TypeToken<Meeting>(){}.getType();

        for(int i = 0; i < jsonArray.length(); i++)
        {
         meetingsList.add((Meeting) gson.fromJson(jsonArray.get(i).toString(),type));
        }

        if(meetingsList == null || !(meetingsList.size() > 0))
        {
            throw new JSONException("No Meetings");
        }
    }
}
