package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

public class BusinessProfile extends UserProfile
{
    private String role;

    private int businessImage;

    private int contactNumber;

    public BusinessProfile()
    {
        super();
    }

    public BusinessProfile(String role)
    {
        super();
        this.role = role;
    }

    public BusinessProfile(String role, int businessImage)
    {
        super();
        this.role = role;
        this.businessImage = businessImage;
    }

    @NonNull
    @Override
    public String toString() {
        return "BusinessProfile{" +
                "role='" + role + '\'' +
                ", businessImage=" + businessImage +
                ", contactNumber=" + contactNumber +
                '}';
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(int businessImage) {
        this.businessImage = businessImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
