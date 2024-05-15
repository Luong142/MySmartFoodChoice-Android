package com.example.myfoodchoice.ModelDietitian;

import androidx.annotation.NonNull;

public class HealthTips
{
    private String userKey;

    private String dietitianKey;

    private String title;
    private String content;

    private String dietitianProfileImage;

    private String dietitianInfo;

    public HealthTips()
    {

    }

    public HealthTips(String userKey, String title, String content)
    {
        this.userKey = userKey;
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "HealthTips{" +
                "userKey='" + userKey + '\'' +
                ", dietitianKey='" + dietitianKey + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", dietitianProfileImage='" + dietitianProfileImage + '\'' +
                ", dietitianInfo='" + dietitianInfo + '\'' +
                '}';
    }

    public String getDietitianInfo() {
        return dietitianInfo;
    }

    public void setDietitianInfo(String dietitianInfo) {
        this.dietitianInfo = dietitianInfo;
    }

    public String getDietitianProfileImage() {
        return dietitianProfileImage;
    }

    public void setDietitianProfileImage(String dietitianProfileImage) {
        this.dietitianProfileImage = dietitianProfileImage;
    }

    public String getDietitianKey() {
        return dietitianKey;
    }

    public void setDietitianKey(String dietitianKey) {
        this.dietitianKey = dietitianKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
