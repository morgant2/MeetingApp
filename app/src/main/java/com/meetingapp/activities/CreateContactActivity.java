package com.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.meetingapp.businessObjects.Location;
import com.meetingapp.businessObjects.Contact;
import com.meetingapp.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class CreateContactActivity extends AppCompatActivity {

    private Contact newContact = null;
    Location userLocation = null;

    private Toolbar toolbar;
    private View etFirstName = null;
    private View etLastName = null;
    private View etUsername = null;
    private View etAddress = null;
    private View etZipCode = null;
    private View etCity = null;
    private View etState = null;
    View [] etFields = null;

    Button btnCreateUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
//        /*
//          We will not use setContentView in this activty
//         Rather than we will use layout inflater to add view in FrameLayout of our base activity layout
//         Adding our layout to parent class frame layout.
//         */
//        getLayoutInflater().inflate(R.layout.activity_create_contact, mFrameLayout);

        btnCreateUser = (Button) findViewById(R.id.btnCreateButton);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_contacts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setETFields();
        createTextEventListener();

        btnCreateUser.setEnabled(false);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormFilled())
                {

                    userLocation = new Location(((EditText)etAddress).getText().toString(),
                            ((EditText)etCity).getText().toString(),
                            ((Spinner)etState).getSelectedItem().toString(),
                            ((EditText)etZipCode).getText().toString());
                    newContact = new Contact(
                            ((EditText)etFirstName).getText().toString(),
                            ((EditText)etLastName).getText().toString(),
                            ((EditText)etUsername).getText().toString(),
                            userLocation);


                    //userLocation.setCoordinates(getLocation(getApplicationContext(), userLocation)); //TODO: DO I need this anymore?
                    userLocation.convertAddressToGeoLocation();


                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.contacts_key), Context.MODE_PRIVATE);
                        newContact.save(sharedPreferences, getString(R.string.contacts_key));

                        Intent scheduledMeetingsIntent = new Intent(CreateContactActivity.this, ContactsActivity.class);
                        CreateContactActivity.this.startActivity(scheduledMeetingsIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to save new contact.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isValidLocation() {
        //TODO: Add Location Validation
        Location location = new Location(((EditText)etAddress).getText().toString(),
                ((EditText)etCity).getText().toString(),
                ((Spinner)etState).getSelectedItem().toString(),
                ((EditText)etZipCode).getText().toString());

        return getLocation(getApplicationContext(), location) != null;
    }

    private void createTextEventListener() {
        for(View view : etFields)
        {
            if(view instanceof EditText)
            {
                EditText et = (EditText) view;
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        toggleButtonEnabledState();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            else if(view instanceof Spinner)
            {
                Spinner spin = (Spinner) view;
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        toggleButtonEnabledState();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        toggleButtonEnabledState();
                    }
                });
            }
        }
    }

    private void toggleButtonEnabledState() {
        boolean setEnabled = false;
        if(isFormFilled())
        {
            //Do not want to run this method everytime a user types
            if(isValidLocation())
            {
                setEnabled = true;
            }
        }
        btnCreateUser.setEnabled(setEnabled);
    }

    private void setETFields() {
        etFirstName = findViewById(R.id.firstName);
        etLastName =  findViewById(R.id.lastName);
        etUsername = findViewById(R.id.username);
        etAddress = findViewById(R.id.address);
        etZipCode =  findViewById(R.id.zipcode);
        etCity =  findViewById(R.id.city);
        etState = findViewById(R.id.states);

        etFields = new View[7];

        etFields[0] = etFirstName;
        etFields[1] = etLastName;
        etFields[2] = etUsername;
        etFields[3] = etAddress;
        etFields[4] = etZipCode;
        etFields[5] = etCity;
        etFields[6] = etState;
    }

    private boolean isFormFilled()
    {
        boolean isFilled = true;

        for(View view : etFields)
        {
            if(view instanceof EditText) {
                EditText et = (EditText) view;

                if (et.getText().length() == 0) {
                    isFilled = false;
                }
            }
            else if(view instanceof Spinner)
            {
                Spinner spin = (Spinner) view;
                if(spin.getSelectedItemPosition() < 0)
                {
                    isFilled = false;
                }
            }
        }

        return isFilled;
    }

    private LatLng getLocation(Context context, Location userLocation){
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(userLocation.getFullAddress(), 5);
            if (address == null) {
                return null;
            }
            if(address.size() > 0)
            {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}

