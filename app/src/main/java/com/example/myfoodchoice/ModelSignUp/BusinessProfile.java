package com.example.myfoodchoice.ModelSignUp;

import androidx.annotation.NonNull;

public class BusinessProfile extends CommonProfile
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
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Business Profile\n");
        sb.append("-------------------\n");
        sb.append("First Name: ").append(super.getFirstName()).append("\n");
        sb.append("Last Name: ").append(super.getLastName()).append("\n");
        sb.append("Role: ").append(role).append("\n");
        sb.append("Contact Number: ").append(contactNumber).append("\n");
        return sb.toString();
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
