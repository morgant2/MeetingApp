package com.meetingapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.meetingapp.businessObjects.Contact;
import com.meetingapp.businessObjects.Location;
import com.meetingapp.businessObjects.Meeting;
import com.meetingapp.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateMeetingActivity extends BaseActivity {

    Calendar startDate;
    Calendar endDate;
    Calendar date;
    Context context;
    ArrayList<Contact> possibleContacts = new ArrayList<>();
    ArrayList<Contact> actualContacts;
    
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
        /*
          We will not use setContentView in this activty
         Rather than we will use layout inflater to add view in FrameLayout of our base activity layout
         Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_create_meeting, mFrameLayout);

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
        for(Contact contact : possibleContacts)
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
                newMeeting.setContactsAttending(actualContacts);
                newMeeting.setCenterLocation();
                newMeeting = getAddressFromGeoLocation(newMeeting);

                try {
                    newMeeting.save(getSharedPreferences(getString(R.string.meetings_key), Context.MODE_PRIVATE), getString(R.string.meetings_key));

                    //TODO: Bug - Send Email and Create Meeting Intents are Out of Sequence
                    sendEmail(newMeeting);

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
                addContact();

            }
        });
    }

    private void sendEmail(Meeting meeting) {
        ArrayList<Contact> contacts = meeting.getContactsAttending();
        String[] emails = new String[contacts.size()];

        for(int i = 0; i < emails.length; i++)
        {
            emails[i] = contacts.get(i).getEmail();
        }

        Intent sendEmail = new Intent(Intent.ACTION_SEND);

        sendEmail.setType("plain/text");
        sendEmail.putExtra(Intent.EXTRA_EMAIL, emails);
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Meeting Notification: " + meeting.getSubject());
        sendEmail.putExtra(Intent.EXTRA_TEXT, constructMeetingBody(meeting));

        CreateMeetingActivity.this.startActivity(sendEmail);
    }

    private void addContact() {
        ListView lvAttendees = (ListView) findViewById(R.id.lvAttendees);
        Spinner spinAddAttendee = (Spinner) findViewById(R.id.spinAddAttendee);

        if(spinAddAttendee.getCount() > 0)
        {
            int selectedItem = spinAddAttendee.getSelectedItemPosition();

            if(actualContacts == null) actualContacts = new ArrayList<Contact>();

            actualContacts.add((Contact) possibleContacts.get(selectedItem));
            possibleContacts.remove(selectedItem);

            spinAddAttendee.setAdapter(getArrayAdapter(R.layout.support_simple_spinner_dropdown_item, getLastFirstNameArray(possibleContacts)));
            lvAttendees.setAdapter(getArrayAdapter(android.R.layout.simple_list_item_1, getLastFirstNameArray(actualContacts)));
        }

    }

    private String constructMeetingBody(Meeting meeting)
    {
        String msg = "";

        msg += "Hello,\n\n";
        msg += "You have been invited to discuss " + meeting.getSubject() + ".\n";
        msg += "Meeting Details:\n" + meeting.toString();
        msg += "\n\nSee you there!";

        return msg;
    }

    private ArrayList<String> getLastFirstNameArray(ArrayList<Contact> contacts)
    {
        ArrayList<String> lastFirstNameList = new ArrayList<String>();
        for(Contact contact : contacts)
        {
            lastFirstNameList.add(contact.getLastFirstName());
        }

        return lastFirstNameList;
    }

    private ArrayAdapter<String> getArrayAdapter( int layout, ArrayList<String> arrayList)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), layout, arrayList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        return adapter;
    }

    public void setContacts() throws JSONException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().create();

        String contactsJson = getSharedPreferences(getString(R.string.contacts_key), Context.MODE_PRIVATE).getString(getString(R.string.contacts_key), "{'possibleContacts':[]}");
        if (contactsJson == "") contactsJson = "{'contacts':[]}";
        JSONObject jsonObject = new JSONObject(contactsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
        Type type = new TypeToken<Contact>(){}.getType();

        if(possibleContacts == null)
        {
            possibleContacts = new ArrayList<Contact>();
        }

        for(int i = 0; i < jsonArray.length(); i++)
        {

            possibleContacts.add((Contact) gson.fromJson(jsonArray.get(i).toString(), type));
        }

        if(possibleContacts == null || !(possibleContacts.size() > 0))
        {
            throw new JSONException("No possibleContacts");
        }

    }

    private Meeting getAddressFromGeoLocation(Meeting meeting){
            if(meeting.getCenterLocation() == null) {
                //TODO: handle if coordinates are empty
            } else {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses;
                    double lat = meeting.getCenterLocation().latitude;
                    double lon = meeting.getCenterLocation().longitude;
                    addresses = geocoder.getFromLocation(lat, lon, 1);

                    if(addresses != null && addresses.size() > 0) {

                        //String address = addresses.get(0).getAddressLine(0);
                        String address = addresses.get(0).getFeatureName();
                        String road = addresses.get(0).getThoroughfare();
                        address = address + " " + road;
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String zipCode = addresses.get(0).getPostalCode();

                        Location location = new Location(address, city, state, zipCode);
                        meeting.setMeetingLocation(location);
                    }
                }catch(IOException ioe) {
                    ioe.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        return  meeting;
    }


}
