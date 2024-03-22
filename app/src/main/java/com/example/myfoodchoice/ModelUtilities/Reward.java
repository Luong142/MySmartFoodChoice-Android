package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class Reward
{
    private String id;
    private String name;
    private String description;
    private int imageId;

    private int points;


    public Reward()
    {

    }

    public Reward(String name, String description, int imageId, int points)
    {
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.points = points;
    }

    public Reward(String id, String name, String description, int imageId, int points)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.points = points;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Reward{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageId=" + imageId +
                ", points=" + points +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
