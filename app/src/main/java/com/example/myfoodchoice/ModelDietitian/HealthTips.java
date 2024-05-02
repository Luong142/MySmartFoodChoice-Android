package com.example.myfoodchoice.ModelDietitian;

import androidx.annotation.NonNull;

public class HealthTips
{
    private String userKey;
    private String title;

    private String content;

    public HealthTips(String userKey, String title, String content)
    {
        this.userKey = userKey;
        this.title = title;
        this.content = content;
    }

    public HealthTips(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "HealthTips{" +
                "userKey='" + userKey + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
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
