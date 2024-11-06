package com.example.quokka_event.models.admin;


import androidx.annotation.DrawableRes;

import java.util.Date;

/*
 * A class to handle individual profiles
 * int for the image id
 * */
public class ProfileSystem {
    @DrawableRes
    private int profileImage;
    private String name;
    private String email;
    private int phoneNumber;
    private Date birthday;
    private String address;

    // Constructor for profilesystem class
    public ProfileSystem(@DrawableRes int profileImage, String name, String email, int phoneNumber, Date birthday, String address){
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
    }

    // Temporary empty constructor - there might be a better way to do this but not all fields are
    // required so I need an easy way to do that for now
    public ProfileSystem(){}

    // Set profile image should upload the image to firebase as well
    public void setImage(@DrawableRes int newImage){
        profileImage = newImage;
    }

    // Get profile picture from the profile
    @DrawableRes
    public int getImage(){
        return profileImage;
    }

    // Set the name for profile
    public void setName(String newName){
        name = newName;
    }

    // Get name of profile
    public String getName(){
        return name;
    }

    // Set an email. should check if the email is valid
    public void setEmail(String newEmail){
        email = newEmail;
    }

    // Get email
    public String getEmail(){
        return email;
    }

    // set phone number also check the valid length
    public void setPhoneNumber(int newPhoneNumber){
        phoneNumber = newPhoneNumber;
    }

    // get phone number
    public int getPhoneNumber(){
        return phoneNumber;
    }

    // Set birthday check for age
    public void setBirthday(Date newBirthday) {
        birthday = newBirthday;
    }

    // Get birthday of profile user
    public Date getBirthday(){
        return birthday;
    }

    // Set an address, have specific place to write house number, city, province in UI
    public void setAddress(String newAddress){
        address = newAddress;
    }

    // Get address
    public String getAddress(){
        return address;
    }

}