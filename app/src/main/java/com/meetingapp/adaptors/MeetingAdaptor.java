package com.meetingapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meetingapp.R;
import com.meetingapp.businessObjects.Meeting;

import java.util.ArrayList;

/**
 * An custom adaptor class to allow displaying custom layout in listview item
 * refer to https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
 */

public class MeetingAdaptor extends ArrayAdapter<Meeting> {

    /**
     * Public constructor
     * @param context - the activity context
     * @param meetings - an arraylist of contacts to be adapted
     */
    public MeetingAdaptor(Context context, ArrayList<Meeting> meetings) {
        super(context, 0, meetings);
    }

    /**
     * Override the getView method of ArrayAdaptor in order to display a custom listview item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Meeting meeting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_meeting, parent, false);
        }
        // Lookup view for data population
        TextView meetingTitleTV = (TextView) convertView.findViewById(R.id.meetingTitle);
        TextView meetingLocationTV = (TextView) convertView.findViewById(R.id.meetingLocation);
        // Populate the data into the template view
        meetingTitleTV.setText(meeting.getSubject());
        // This is not returning a correct string right now, showing LatLng instead of address

        meetingLocationTV.setText(meeting.getMeetingLocation().getFullAddress());
        // Return the completed view to render on screen

        //meetingLocationTV.setText(meeting.getSubject());
        return convertView;
    }


}
