package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class Reward
{
    private String name;
    private String description;
    private String rewardImageUrl;

    private int points;


    public Reward()
    {

    }

    public Reward(String name, String description, String rewardImageUrl, int points)
    {
        this.name = name;
        this.description = description;
        this.rewardImageUrl = rewardImageUrl;
        this.points = points;
    }

    public Reward(String name, String description, int points)
    {
        this.name = name;
        this.description = description;
        this.points = points;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Reward{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rewardImageUrl='" + rewardImageUrl + '\'' +
                ", points=" + points +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRewardImageUrl() {
        return rewardImageUrl;
    }

    public void setRewardImageUrl(String rewardImageUrl) {
        this.rewardImageUrl = rewardImageUrl;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
