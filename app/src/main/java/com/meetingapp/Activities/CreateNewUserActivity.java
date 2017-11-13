package com.meetingapp.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.meetingapp.BusinessObjects.Location;
import com.meetingapp.BusinessObjects.Contact;
import com.meetingapp.R;

public class CreateNewUserActivity extends AppCompatActivity {

    private Contact newContact = null;
    Location userLocation = null;

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
        setContentView(R.layout.activity_create_new_user);

        btnCreateUser = (Button) findViewById(R.id.btnCreateButton);

        setETFields();
        createTextEventListener();

        btnCreateUser.setEnabled(false);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormFilled())
                {
                    userLocation = new Location(((EditText)etAddress).getText().toString(), ((EditText)etCity).getText().toString(), ((Spinner)etState).getSelectedItem().toString(), ((EditText)etZipCode).getText().toString());
                    newContact = new Contact(((EditText)etFirstName).getText().toString(), ((EditText)etLastName).getText().toString(), ((EditText)etUsername).getText().toString(), userLocation);

                    Context context = getApplicationContext();
                    String msg = constructToasterMsg();
                    int length = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, msg, length);
                    toast.show();
                }
            }
        });
    }

    private String constructToasterMsg() {
        String msg = "";
        msg += newContact.getFirstName() + " " + newContact.getLastName() + "\n";
        msg += "Username: " + newContact.getEmail() + "\n";

        msg += "Address: " + newContact.getUserHomeLocation().getAddress() + " " + newContact.getUserHomeLocation().getCity() + ", ";
        msg += newContact.getUserHomeLocation().getState() + " " + newContact.getUserHomeLocation().getZipCode();

        return msg;
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
                        toggleButtonEnabledState();
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        toggleButtonEnabledState();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        toggleButtonEnabledState();
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
        if(isFormFilled())
        {
            btnCreateUser.setEnabled(true);
        }
        else
        {
            btnCreateUser.setEnabled(false);
        }
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


}

