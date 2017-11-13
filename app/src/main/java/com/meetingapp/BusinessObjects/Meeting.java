package com.meetingapp.BusinessObjects;

import java.util.Date;

public class Meeting
{
    private String Subject;
    private String Description;
    private Date StartTime;
    private Date EndTime;
    private Location MeetingLocation;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public Location getMeetingLocation() {
        return MeetingLocation;
    }

    public void setMeetingLocation(Location meetingLocation) {
        MeetingLocation = meetingLocation;
    }

    public Contact[] getContactsAttending() {
        return contactsAttending;
    }

    public void setContactsAttending(Contact[] contactsAttending) {
        this.contactsAttending = contactsAttending;
    }

    private Contact[] contactsAttending;
}

