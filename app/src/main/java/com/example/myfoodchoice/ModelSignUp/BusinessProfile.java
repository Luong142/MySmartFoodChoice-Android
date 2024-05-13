package com.example.myfoodchoice.ModelSignUp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class BusinessProfile extends CommonProfile implements Parcelable
{
    private String role;

    private String businessKey;

    private int businessImage;

    private int contactNumber;

    public BusinessProfile()
    {
        super();
        this.role = "Dietitian";
        this.businessImage = 0;
        this.contactNumber = 9999;
    }

    public BusinessProfile(String role, int businessImage)
    {
        super();
        this.role = role;
        this.businessImage = businessImage;
    }

    public BusinessProfile(@NonNull Parcel in)
    {
        super(in);
        role = in.readString();
        businessKey = in.readString();
        businessImage = in.readInt();
        contactNumber = in.readInt();
    }

    public String getDietitianInfo()
    {
        return "Meet " + super.getFirstName() + " " +
                super.getLastName() + ", your dietitian guide.\n" +
                "Contact Number " + contactNumber + "\n";
    }


    public String getBusinessKey()
    {
        return businessKey;
    }

    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(role);
        dest.writeString(businessKey);
        dest.writeInt(businessImage);
        dest.writeInt(contactNumber);
    }

    public static final Creator<BusinessProfile> CREATOR = new Creator<BusinessProfile>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public BusinessProfile createFromParcel(Parcel in)
        {
            return new BusinessProfile(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public BusinessProfile[] newArray(int size)
        {
            return new BusinessProfile[size];
        }
    };
}
