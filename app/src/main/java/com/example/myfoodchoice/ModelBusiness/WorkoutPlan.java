package com.example.myfoodchoice.ModelBusiness;

import androidx.annotation.NonNull;

import java.util.List;

public class WorkoutPlan
{
    private String name;

    private String description;

    private int duration;


    private List<Exercise> exercises;


    public WorkoutPlan()
    {

    }

    public WorkoutPlan(String name, String description, int duration)
    {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public WorkoutPlan(String name, String description, int duration, List<Exercise> exercises)
    {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "WorkoutPlan{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", exercises=" + exercises +
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
