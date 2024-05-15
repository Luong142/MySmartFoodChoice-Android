package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class CheckInDay
{
    private String day;

    private String imageURL;

    public CheckInDay(String day)
    {
        this.day = day;
    }

    public CheckInDay()
    {

    }

    public CheckInDay(int imageId, String day)
    {
        this.day = day;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "CheckInDay{" +
                ", day='" + day + '\'' +
                ", URL='" + imageURL + '\'' +
                '}';
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
