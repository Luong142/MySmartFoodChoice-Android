package com.example.myfoodchoice.ModelBusiness;

import androidx.annotation.NonNull;

public class Exercise
{
    private String name;
    private int sets;
    private int reps;
    private int restInterval;

    public Exercise()
    {

    }

    public Exercise(String name, int sets, int reps, int restInterval)
    {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.restInterval = restInterval;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", restInterval=" + restInterval +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRestInterval() {
        return restInterval;
    }

    public void setRestInterval(int restInterval) {
        this.restInterval = restInterval;
    }
}
