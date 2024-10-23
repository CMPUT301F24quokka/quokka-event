package com.example.quokka_event;

import java.util.ArrayList;

public class Organizer {

    //array list of Facilities
    private ArrayList<Facility> facilityProfile = new ArrayList<Facility>();
    private int maxNumberOfEntrantsAllowed;
    private int sampleNumberOfEntrants;

    //a constructor for an Organizer object
    public Organizer(){}

    public int getMaxNumberOfEntrantsAllowed() { return maxNumberOfEntrantsAllowed;}
    public void setMaxNumberOfEntrantsAllowed(int maxNumberOfEntrantsAllowed) { this.maxNumberOfEntrantsAllowed = maxNumberOfEntrantsAllowed; }

    public int getSampleNumberOfEntrants() { return sampleNumberOfEntrants; }
    public void setSampleNumberOfEntrants(int sampleNumberOfEntrants) { this.sampleNumberOfEntrants = sampleNumberOfEntrants; }

    //creates new event based on values passed in arguments
    //return true if created successfully, false if not
    //could also make it return the Event instead (still haven't decided)
    protected boolean createNewEvent() { return false; }

    //allows organizer to edit the event details
    //specified event passed through parameters
    //either return boolean or the edited Event (haven't decided yet)
    protected boolean editEventDetails() { return false; }

    //calls QR code generator
    //links QR code to the event description and event poster
    protected boolean QRCodeLinking() { return false; }

    //displays or retrieves the list of entrants on the waiting list of an event object
    //pass event object thru parameters
    void viewEventEntrants(){}

    //displays the list of entrants who have been selected and are invited to apply
    //Lottery class deals w/ who was selected
    void listOfSelectedEntrants(){}

    void listOfCanceledEntrants(){}

    void listOfEnrolledEntrants(){}

    //cancel an Entrant that didn't sign up in time for the Event
    //pass waitlist thru parameters, cancel/ remove whichever entrant(s) you want
    //true or false if it worked or not
    boolean cancelEntrants(){ return false; }

    //pass a waitlist thru parameter to draw a specific Entrant after a previously selected Entrant cancels/doesn't sign up
    Entrant drawReplacementEntrant(){ return null; }

    //sends notifications to all entrants in a specified event's waiting list
    //pass event object through parameter
    //pass a string argument that represents the message of the notification to be sent
    /*the message can vary, depending on if you want to send the notification to
        -all entrants that are on the event's waiting list
        -the selected entrants from the waiting list (the Lottery class deals w/ entrant selection)
        -the cancelled entrants from the waiting list
    */
    boolean sendNotificationToEntrants() { return false; }

    //to add a new Facility to ArrayList
    //pass facility to be added thru parameters
    protected void addNewFacility(){}

    //to remove facility from ArrayList
    //pass specified facility thru parameters
    protected void removeFacility(){}

    //to edit the details and such of a specified facility
    //pass the specified facility thru parameters
    protected void editSpecifiedFacility(){}
}