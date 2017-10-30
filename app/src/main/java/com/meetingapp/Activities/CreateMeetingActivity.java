package com.meetingapp.Activities;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.meetingapp.Fragments.TimeFragment;
import com.meetingapp.R;

public class CreateMeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        Button btnAddAttendee = (Button) findViewById(R.id.btnAddAttendee);
        TimeFragment startTimeFragment = (TimeFragment) getSupportFragmentManager().findFragmentById(R.id.startTime);
        TimeFragment endTimeFragment = (TimeFragment) getSupportFragmentManager().findFragmentById(R.id.endTime);

        FragmentTransaction startTimeTransaction = getFragmentManager().beginTransaction();

        if(startTimeFragment != null)
        {

        }

        if(endTimeFragment != null)
        {

        }



        btnAddAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView lvAttendees = (ListView) findViewById(R.id.lvAttendees);
                Spinner spinAddAttendee = (Spinner) findViewById(R.id.spinAddAttendee);

                
                spinAddAttendee.getSelectedItem().toString();
            }
        });
    }


}
