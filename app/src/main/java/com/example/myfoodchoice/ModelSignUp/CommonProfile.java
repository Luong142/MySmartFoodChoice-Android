package com.example.myfoodchoice.ModelSignUp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class CommonProfile implements Parcelable
{
    private String firstName;

    private String lastName;

    private String profileImageUrl;

    public CommonProfile()
    {
        firstName = "Unknown";
        lastName = "Unknown";
        profileImageUrl = ""; // leave it empty here.
    }

    public CommonProfile(String firstName, String lastName, String profileImageUrl)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(profileImageUrl);
    }

    public static final Creator<CommonProfile> CREATOR = new Creator<CommonProfile>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public CommonProfile createFromParcel(Parcel in)
        {
            return new CommonProfile(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public CommonProfile[] newArray(int size)
        {
            return new CommonProfile[size];
        }
    };

    public CommonProfile(@NonNull Parcel in)
    {
        firstName = in.readString();
        lastName = in.readString();
        profileImageUrl = in.readString();
    }
    @NonNull
    @Override
    public String toString()
    {
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
