package com.example.myfoodchoice.Model;

public class BusinessTrainerProfile
{
    private String firstName;

    private String lastName;
    private String profileImageUrl;

    private int contactNumber;

    private String accountType;

    public BusinessTrainerProfile(String firstName, String lastName, String profileImageUrl, int contactNumber, String businessLabel)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.contactNumber = contactNumber;
        this.accountType = businessLabel;
    }

    public BusinessTrainerProfile()
    {
        accountType = "Trainer";
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

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
