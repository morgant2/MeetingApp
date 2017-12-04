package com.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Comparator;

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
        Collections.sort(contactList, new Comparator<Contact>() {
            public int compare(Contact c1, Contact c2) {
                return c1.getLastFirstName().compareTo(c2.getLastFirstName());
            }
        });

        // get an adapter for the contact list
        ArrayAdapter contactsAdapter = new ContactAdaptor(this, contactList);
        // get the listview from the layout
        ListView contactListView = (ListView) findViewById(R.id.contact_list_view);
        //Set the adapter to adapt the listview
        contactListView.setAdapter(contactsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_create_contact:
                startActivity(new Intent(getApplicationContext(), CreateContactActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * method to read contacts from SharedPreferences into ArrayList for populating ListvVew
     */
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
