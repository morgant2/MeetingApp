package com.meetingapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.meetingapp.BusinessObjects.Contact;
import com.meetingapp.BusinessObjects.Meeting;
import com.meetingapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateMeetingActivity extends AppCompatActivity {

    Calendar startDate;
    Calendar endDate;
    Calendar date;
    Context context;
    ArrayList<Contact> contacts;

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
        Button btnSetEndTime = (Button)     findViewById(R.id.btnEndTime);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        try {
            setContacts();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spinner spinPossibleAttendees = (Spinner) findViewById(R.id.spinAddAttendee);

        ArrayList<String> possibleAttendeesList = new ArrayList<>();
        for(Contact contact : contacts)
        {
            possibleAttendeesList.add(contact.getLastFirstName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, possibleAttendeesList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinPossibleAttendees.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meeting newMeeting = new Meeting();
                newMeeting.setSubject(((EditText) findViewById(R.id.subject)).getText().toString());
                newMeeting.setStartTime(startDate.getTime());
                newMeeting.setEndTime(endDate.getTime());

                try {
                    newMeeting.save(getSharedPreferences(getString(R.string.meetings_key), Context.MODE_PRIVATE), getString(R.string.meetings_key));

                    Intent scheduledMeetingsIntent = new Intent(CreateMeetingActivity.this, ScheduledMeetingsActivity.class);
                    CreateMeetingActivity.this.startActivity(scheduledMeetingsIntent);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unable to save meeting!", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
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



    public void setContacts() throws JSONException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().create();

        String contactsJson = getSharedPreferences(getString(R.string.contacts_key), Context.MODE_PRIVATE).getString(getString(R.string.contacts_key), "{'contacts':[]}");
        if (contactsJson == "") contactsJson = "{'contacts':[]}";
        JSONObject jsonObject = new JSONObject(contactsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
        Type type = new TypeToken<Contact>(){}.getType();

        if(contacts == null)
        {
            contacts = new ArrayList<Contact>();
        }

        for(int i = 0; i < jsonArray.length(); i++)
        {
            contacts.add((Contact) gson.fromJson(jsonArray.get(i).toString(), type));
        }

        if(contacts == null || !(contacts.size() > 0))
        {
            throw new JSONException("No contacts");
        }

    }

    public String loadGSON(Context context, String fileName)
    {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e){
            e.printStackTrace();
        }

        return json;
    }

}
