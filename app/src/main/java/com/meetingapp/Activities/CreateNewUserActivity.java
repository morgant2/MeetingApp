package com.meetingapp.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meetingapp.BusinessObjects.Location;
import com.meetingapp.BusinessObjects.User;
import com.meetingapp.R;

public class CreateNewUserActivity extends AppCompatActivity {

    private User newUser = null;
    Location userLocation = null;

    private EditText etFirstName = null;
    private EditText etLastName = null;
    private EditText etUsername = null;
    private EditText etAddress = null;
    private EditText etZipCode = null;
    private EditText etCity = null;
    private EditText etState = null;
    EditText [] etFields = null;

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
                    userLocation = new Location(etAddress.getText().toString(), etCity.getText().toString(), etState.getText().toString(), etZipCode.getText().toString());
                    newUser = new User(etFirstName.getText().toString(), etLastName.getText().toString(), etUsername.getText().toString(), userLocation);

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
        msg += newUser.FirstName + " " + newUser.LastName + "\n";
        msg += "Username: " + newUser.Username + "\n";

        msg += "Address: " + newUser.UserHomeLocation.Address + " " + newUser.UserHomeLocation.City + ", ";
        msg += newUser.UserHomeLocation.State + " " + newUser.UserHomeLocation.ZipCode;

        return msg;
    }

    private void createTextEventListener() {
        for(EditText et : etFields)
        {
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
        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etUsername = (EditText) findViewById(R.id.username);
        etAddress = (EditText) findViewById(R.id.address);
        etZipCode = (EditText) findViewById(R.id.zipcode);
        etCity = (EditText) findViewById(R.id.city);
        etState = (EditText) findViewById(R.id.state);

        etFields = new EditText[7];

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

        for(EditText et : etFields)
        {
            if(et.getText().length() == 0)
            {
                isFilled = false;
            }
        }

        return isFilled;
    }


}

