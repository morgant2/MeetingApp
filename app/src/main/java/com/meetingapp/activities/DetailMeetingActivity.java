package com.meetingapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.meetingapp.R;
import com.meetingapp.businessObjects.Meeting;

/**
 * Created by Jacob on 12/6/2017.
 */

public class DetailMeetingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String attendees = "";
    private Meeting meeting;
    Intent i;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);

        i = getIntent();
        extras = i.getExtras();
        //meeting = (Meeting) i.getSerializableExtra("Meeting");

        setContentView(R.layout.activity_detail_meeting);

        toolbar = (Toolbar) findViewById(R.id.toolbar_detail_meeting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*detailMeetingIntent.putExtra("Subject", meeting.getSubject());
        detailMeetingIntent.putExtra("Description", meeting.getDescription());
        detailMeetingIntent.putExtra("Start", meeting.getStartTime().toString());
        detailMeetingIntent.putExtra("End", meeting.getEndTime().toString());
        detailMeetingIntent.putExtra("Location", meeting.getMeetingLocation().getFullAddress());
        detailMeetingIntent.putExtra("Contacts", meeting.getContactsAttending());*/

        TextView subject = (TextView) findViewById(R.id.detail_meeting_subject);
        //TextView description = (TextView) findViewById(R.id.detail_meeting_description);
        TextView startDate = (TextView) findViewById(R.id.detail_meeting_start);
        TextView endDate = (TextView) findViewById(R.id.detail_meeting_end);
        //TextView attendeesText = (TextView) findViewById(R.id.detail_meeting_attendees);
        TextView location = (TextView) findViewById(R.id.detail_meeting_location);



        subject.setText(extras.getString("Subject"));
        //description.setText(extras.getString("Description"));
        startDate.setText(extras.getString("Start"));
        endDate.setText(extras.getString("End"));
        location.setText(extras.getString("Location"));

        //getAttendees();

    }

    private void getAttendees() {
        //ArrayList<Contact> contacts = extras.getA

        int x = 0;
    }

}
