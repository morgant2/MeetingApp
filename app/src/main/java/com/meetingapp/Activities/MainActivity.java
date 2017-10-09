package com.meetingapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.meetingapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtonEvents();
    }

    private void setupButtonEvents()
    {
        Button profile = (Button) findViewById(R.id.Profile);
        Button contacts = (Button) findViewById(R.id.Contacts);
        Button createNewMeeting = (Button) findViewById(R.id.CreateMeeting);
        Button ScheduledMeetings = (Button) findViewById(R.id.MeetingList);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                MainActivity.this.startActivity(profileIntent);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewMeetingIntent = new Intent(MainActivity.this, CreateNewUserActivity.class);
                MainActivity.this.startActivity(createNewMeetingIntent);
            }
        });

        ScheduledMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scheduledMeetingsIntent = new Intent(MainActivity.this, ScheduledMeetingsActivity.class);
                MainActivity.this.startActivity(scheduledMeetingsIntent);
            }
        });

        createNewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewMeetingIntent = new Intent(MainActivity.this, CreateNewUserActivity.class);
                MainActivity.this.startActivity(createNewMeetingIntent);
            }
        });
    }
}
