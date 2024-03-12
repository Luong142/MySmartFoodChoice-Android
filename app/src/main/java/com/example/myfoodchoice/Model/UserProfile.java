package com.example.myfoodchoice.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UserProfile implements Parcelable // way more efficient to use Parcelable.
{
    private String firstName;

    private String lastName;

    private String height;

    private String weight;

    private String profileImageUrl;
    private String gender;
    private int age;
    private int BMI;

    private int calories;

    private String dietType;

    private int dietTypeImage;

    private String accountType;

    public UserProfile()
    {
        // this constructor is required for Firebase to be able to deserialize the object
        this.BMI = 0;
        this.calories = 0;
        this.accountType = "User";
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    protected UserProfile(@NonNull Parcel in)
    {

        firstName = in.readString();
        lastName = in.readString();
        height = in.readString();
        weight = in.readString();
        profileImageUrl = in.readString();
        gender = in.readString();
        age = in.readInt();
        dietType = in.readString();
    }

    public UserProfile(String dietType, int dietTypeImage)
    {
        this.dietType = dietType;
        this.dietTypeImage = dietTypeImage;
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
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(profileImageUrl);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeString(dietType);
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getDietTypeImage() {
        return dietTypeImage;
    }

    public void setDietTypeImage(int dietTypeImage) {
        this.dietTypeImage = dietTypeImage;
    }

    @NonNull
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("User Profile\n");
        sb.append("-------------------\n");
        sb.append("First Name: ").append(firstName).append("\n");
        sb.append("Last Name: ").append(lastName).append("\n");
        sb.append("Height: ").append(height).append(" cm\n");
        sb.append("Weight: ").append(weight).append(" kg\n");
        sb.append("Gender: ").append(gender).append("\n");
        sb.append("Age: ").append(age).append("\n");
        sb.append("Diet Type: ").append(dietType).append("\n");
        return sb.toString();
    }

    public int getBMI()
    {
        return BMI;
    }

    public void setBMI(int BMI)
    {
        this.BMI = BMI;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
