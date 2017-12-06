package com.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
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
import java.util.Locale;

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
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

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
                   //userLocation.convertAddressToGeoLocation();

                   getGeoLocationFromAddress();


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

    private void getGeoLocationFromAddress() {
        //Get the longitude and latitude for the address

        if(userLocation.getAddress() == null || userLocation.getCity() == null || userLocation.getState() == null || userLocation.getZipCode() == null) {
            //TODO: handle if any part of the address is empty
        } else {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                addresses = geocoder.getFromLocationName(userLocation.getFullAddress(), 1);
                while(addresses.size() == 0) {
                    addresses = geocoder.getFromLocationName(userLocation.getFullAddress(), 1);
                }
                if(addresses != null && addresses.size() > 0) {
                    double lat = addresses.get(0).getLatitude();
                    double lon = addresses.get(0).getLongitude();

                    LatLng coords = new LatLng(lat,lon);
                    userLocation.setCoordinates(coords);

                }
            } catch(IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

