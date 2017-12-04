package com.meetingapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meetingapp.R;
import com.meetingapp.businessObjects.Contact;

import java.util.ArrayList;

/**
 * An custom adaptor class to allow displaying custom layout in listview item
 * refer to https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
 */

public class ContactAdaptor extends ArrayAdapter<Contact>{

    /**
     * Public constructor
     * @param context - the activity context
     * @param contacts - an arraylist of contacts to be adapted
     */
    public ContactAdaptor(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    /**
     * Override the getView method of ArrayAdaptor in order to display a custom listview item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }
        // Lookup view for data population
        TextView contactNameTV = (TextView) convertView.findViewById(R.id.contactName);
        TextView contactAddressTV = (TextView) convertView.findViewById(R.id.contactAddress);
        // Populate the data into the template view
        contactNameTV.setText(contact.getLastFirstName());
        contactAddressTV.setText(contact.getUserHomeLocation().getFullAddress());
        // Return the completed view to render on screen
        return convertView;
    }


}
