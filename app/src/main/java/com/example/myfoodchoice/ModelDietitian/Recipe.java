package com.example.myfoodchoice.ModelDietitian;

import androidx.annotation.NonNull;

// Recipe.java
public class Recipe
{
    private int imageId;
    private String name;
    private String description;

    // fixme: only for adapter here not for actual model for API, must use API.
    public Recipe()
    {

    }

    public Recipe(int imageId, String name, String description)
    {
        this.imageId = imageId;
        this.name = name;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Recipe{" +
                "thumbnailResId=" + imageId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}