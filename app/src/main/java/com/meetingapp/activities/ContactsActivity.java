package com.meetingapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        final ArrayAdapter contactsAdapter = new ContactAdaptor(this, contactList);
        // get the listview from the layout
        ListView contactListView = (ListView) findViewById(R.id.contact_list_view);
        //Set the adapter to adapt the listview
        contactListView.setAdapter(contactsAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(ContactsActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + contactList.get(position).getLastFirstName());
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contactList.remove(positionToRemove);
                        try {
                            delete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Unable to delete contact.", Toast.LENGTH_LONG).show();
                        }
                        contactsAdapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
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
        if (contactsJson.equals("")) contactsJson = "{'contacts':[]}";
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

    public void delete() throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.contacts_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONObject jsonObject = new JSONObject("{'contacts':[]}");
        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
        JSONObject newContactJSONObject = new JSONObject();


        for(int i = 0; i < contactList.size(); i++)
        {
            Gson contactGson = new Gson();
            String jsonString = contactGson.toJson(contactList.get(i), Contact.class);

            jsonArray.put(jsonString);
        }

        newContactJSONObject.put("contacts", jsonArray);

        editor.putString(getString(R.string.contacts_key), newContactJSONObject.toString());
        editor.apply();
    }

}
