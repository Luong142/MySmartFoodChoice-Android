package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

public class HealthCondition
{
    private int id;
    private String name;
    private String description;


    @NonNull
    @Override
    public String toString()
    {
        return "HealthCondition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
