package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class CheckInDay
{
    private int imageId;
    private String day;

    public CheckInDay()
    {

    }

    public CheckInDay(int imageId, String day)
    {
        this.imageId = imageId;
        this.day = day;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "CheckInDay{" +
                "imageId=" + imageId +
                ", day='" + day + '\'' +
                '}';
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
