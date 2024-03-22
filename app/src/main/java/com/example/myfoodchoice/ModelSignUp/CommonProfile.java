package com.example.myfoodchoice.ModelSignUp;

import androidx.annotation.NonNull;

public class CommonProfile
{
    private String firstName;

    private String lastName;

    private String profileImageUrl;

    public CommonProfile()
    {

    }

    public CommonProfile(String firstName, String lastName, String profileImageUrl)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "CommonProfile{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
