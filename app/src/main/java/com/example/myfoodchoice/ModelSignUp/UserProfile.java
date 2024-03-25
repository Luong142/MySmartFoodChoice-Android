package com.example.myfoodchoice.ModelSignUp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends CommonProfile implements Parcelable // way more efficient to use Parcelable.
{
    private String height;

    private String weight;

    private String gender;
    private int age;

    private int points;

    private String dietType; // for diet to recommend

    private int dietTypeImage;

    private ActivityLevel activityLevel;

    private int activityLevelImage;

    private String allergies;

    public UserProfile()
    {
        // this constructor is required for Firebase to be able to deserialize the object
        super();
        points = 50; // put the default point here
    }

    protected UserProfile(@NonNull Parcel in)
    {
        super();
        setFirstName(in.readString());
        setLastName(in.readString());
        height = in.readString();
        weight = in.readString();
        setProfileImageUrl(in.readString());
        gender = in.readString();
        age = in.readInt();
        dietType = in.readString();
        points = in.readInt();
        activityLevel = (ActivityLevel) in.readSerializable();
        allergies = in.readString();
    }

    public UserProfile(String dietType, int dietTypeImage)
    {
        this.dietType = dietType;
        this.dietTypeImage = dietTypeImage;
    }

    public UserProfile(ActivityLevel activityLevel, int activityLevelImage)
    {
        this.activityLevel = activityLevel;
        this.activityLevelImage = activityLevelImage;
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
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
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(getProfileImageUrl());
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(dietType);
        dest.writeInt(points);
        dest.writeSerializable(activityLevel);
        dest.writeString(allergies);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Profile\n");
        sb.append("-------------------\n");
        sb.append("First Name: ").append(getFirstName()).append("\n");
        sb.append("Last Name: ").append(getLastName()).append("\n");
        sb.append("Height: ").append(height).append(" cm\n");
        sb.append("Weight: ").append(weight).append(" kg\n");
        sb.append("Gender: ").append(gender).append("\n");
        sb.append("Age: ").append(age).append("\n");
        sb.append("Diet Type: ").append(dietType).append("\n");
        sb.append("Activity Level: ").append(activityLevel).append("\n");
        sb.append("Allergies: ").append(allergies).append("\n");

        sb.append("Points: ").append(points).append("\n");
        sb.append("-------------------");
        return sb.toString();
    }

    public String getActivityLevelDisplayName()
    {
        return activityLevel.getDisplayName();
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getActivityLevelImage() {
        return activityLevelImage;
    }

    public void setActivityLevelImage(int activityLevelImage) {
        this.activityLevelImage = activityLevelImage;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDietTypeImage() {
        return dietTypeImage;
    }

    public void setDietTypeImage(int dietTypeImage) {
        this.dietTypeImage = dietTypeImage;
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

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }
}
