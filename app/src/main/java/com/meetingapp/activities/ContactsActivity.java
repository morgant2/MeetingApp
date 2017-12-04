package com.meetingapp.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.meetingapp.R;
import com.meetingapp.adaptors.ContactAdaptor;
import com.meetingapp.businessObjects.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends BaseActivity {
    ArrayList<Contact> contactList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
          We will not use setContentView in this activty
         Rather than we will use layout inflater to add view in FrameLayout of our base activity layout
         Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_contacts, mFrameLayout);

        // get array list of contacts
        try {
            setContacts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // alphabetize

        // get an adapter for the contact list
        ArrayAdapter contactsAdapter = new ContactAdaptor(this, contactList);
        // get the listview from the layout
        ListView contactListView = (ListView) findViewById(R.id.contact_list_view);
        //Set the adapter to adapt the listview
        contactListView.setAdapter(contactsAdapter);
    }

    public void setContacts() throws JSONException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().create();

        String contactsJson = getSharedPreferences(getString(R.string.contacts_key), Context.MODE_PRIVATE).getString(getString(R.string.contacts_key), "{'contacts':[]}");
        if (contactsJson == "") contactsJson = "{'contacts':[]}";
        JSONObject jsonObject = new JSONObject(contactsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
        Type type = new TypeToken<Contact>(){}.getType();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            contactList.add((Contact) gson.fromJson(jsonArray.get(i).toString(), type));
        }

        if(contactList == null || !(contactList.size() > 0))
        {
            throw new JSONException("No contacts");
        }

    }
}
