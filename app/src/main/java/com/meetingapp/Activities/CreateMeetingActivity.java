package com.meetingapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.meetingapp.BusinessObjects.Contact;
import com.meetingapp.BusinessObjects.Location;
import com.meetingapp.BusinessObjects.Meeting;
import com.meetingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateMeetingActivity extends AppCompatActivity {

    Calendar startDate;
    Calendar endDate;
    Calendar date;
    Context context;
    public void showDateTimePicker(final boolean isStartTime) {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        context = CreateMeetingActivity.this;

        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        setDate(isStartTime);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void setDate(boolean isStartTime) {
        TextView tvSetStartDate = (TextView) findViewById(R.id.lblStartTime);
        TextView tvSetEndDate = (TextView) findViewById(R.id.lblEndTime);

        if(isStartTime)
        {
            startDate = date;
            tvSetStartDate.setText(String.format("%1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", startDate));
        }
        else
        {
            endDate = date;
            tvSetEndDate.setText(String.format("%1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", endDate));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        Button btnAddAttendee = (Button) findViewById(R.id.btnAddAttendee);
        Button btnSetStartTime = (Button) findViewById(R.id.btnStartTime);
        Button btnSetEndTime = (Button) findViewById(R.id.btnEndTime);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meeting newMeeting = new Meeting();
                newMeeting.Subject = ((EditText) findViewById(R.id.subject)).getText().toString();
                newMeeting.StartTime = startDate.getTime();
                newMeeting.EndTime = endDate.getTime();

                String address = ((TextView) findViewById(R.id.address)).getText().toString();
                String city = ((TextView) findViewById(R.id.city)).getText().toString();
                String state = ((Spinner) findViewById(R.id.states)).getSelectedItem().toString();
                String zipCode = ((TextView) findViewById(R.id.zipcode)).getText().toString();

                Location loc = new Location(address, city, state, zipCode);
                newMeeting.MeetingLocation = loc;

                Toast.makeText(getApplicationContext(), newMeeting.toString(),
                        Toast.LENGTH_LONG).show();

                ListView lvContactsAttending = (ListView) findViewById(R.id.lvAttendees);

            }
        });

        btnSetStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = getApplicationContext();
                showDateTimePicker(true);
            }
        });

        btnSetEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = getApplicationContext();
                showDateTimePicker(false);
            }
        });

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
