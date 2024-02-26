package com.example.myfoodchoice.Model;

public class User
{
    private String firstName, lastName;

    // prototype for user profile
    // TODO: user can have multiple user profile.
    // TODO: user profile contains first name, last name, dob, gender, profile picture, etc...
    public User(String textFirstName, String textLastName)
    {
        this.firstName = textFirstName;
        this.lastName = textLastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
