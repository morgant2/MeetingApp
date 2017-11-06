package com.meetingapp.BusinessObjects;

import java.util.Date;

public class Meeting
{
    public String Subject;
    public String Description;
    public Date StartTime;
    public Date EndTime;
    public Location MeetingLocation;
    public Contact[] contactsAttending;
}

