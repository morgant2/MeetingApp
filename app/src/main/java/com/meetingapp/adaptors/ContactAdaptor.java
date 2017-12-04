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
 * Created by Graeham on 12/3/2017.
 */

public class ContactAdaptor extends ArrayAdapter<Contact>{

    public ContactAdaptor(Context context, ArrayList<Contact> users) {
        super(context, 0, users);
    }

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
        // Populate the data into the template view using the data object
        contactNameTV.setText(contact.getLastFirstName());
        contactAddressTV.setText(contact.getUserHomeLocation().getFullAddress());
        // Return the completed view to render on screen
        return convertView;
    }


}
