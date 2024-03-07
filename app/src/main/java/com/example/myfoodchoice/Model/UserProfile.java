package com.example.myfoodchoice.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class UserProfile implements Parcelable // way more efficient to use Parcelable.
{
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String height;

    private String weight;

    private String profileImageUrl;
    private String gender;
    private int age;
    private String dob;

    public UserProfile()
    {
        // this constructor is required for Firebase to be able to deserialize the object
    }

    public UserProfile(String email, String password)
    {
        this.email = email;
        this.password = password;

    }

    protected UserProfile(Parcel in)
    {
        email = in.readString();
        password = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        height = in.readString();
        weight = in.readString();
        profileImageUrl = in.readString();
        gender = in.readString();
        age = in.readInt();
        dob = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>()
    {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents()
    {
        return  0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(profileImageUrl);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(dob);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserProfile{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", profileImage=" + profileImageUrl +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", dob='" + dob + '\'' +
                '}';
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
