package com.example.quokka_event.models.entrant;

// Refactor:
// Remove User functionality and repurpose as an 'EventManager' class (idk about the naming) where
// we deal with all the interactions between User and Event (joining waitlists, responding to
// invites, etc.)
public class EventManager {

    // Constructor
    public EventManager() {
    }


    // Refactor: this can stay here as long as this class doesn't become coupled to a single Event
    // I think. Otherwise we could move it to a separate class for dealing with QR Codes. Idk let's
    // wait till we figure out how the library we're using for this works maybe it's all built in
    // nicely for us.
    public void scanQRCode(){
        // needs implementation
    }

    public void joinWaitlist(){
        // needs implementation
    }

    // Refactor: I'm not sure we need any system for viewing notifications within the app. Besides
    // organizers needing to send notifications from within the app, the rest of it will just be
    // handled by the OS I think?
    // Either way, I would move notifications to a separate 'NotificationManager' class or something
    public void viewNotifications(){
        // needs implementation
    }

    public void respondToInvite(){
        // needs implementation
    }

    public void cancelWaitlistSpot(){
        // needs implementation
    }
}