package com.example.myfoodchoice.ModelBusiness;

import androidx.annotation.NonNull;

public class HealthTips
{
    private String name;

    private String description;

    public HealthTips()
    {

    }

    public HealthTips(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "HealthTips{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
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
}
